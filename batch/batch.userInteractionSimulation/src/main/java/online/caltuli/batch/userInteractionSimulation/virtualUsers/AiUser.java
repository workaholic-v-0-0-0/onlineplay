package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import online.caltuli.batch.userInteractionSimulation.clients.GameWebSocketClient;
import online.caltuli.batch.userInteractionSimulation.clients.HttpClientSSLContext;
import online.caltuli.batch.userInteractionSimulation.jsonUtils.*;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.GameObserver;
import online.caltuli.business.ai.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.Map;

import online.caltuli.model.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AiUser implements HttpHandler, GameObserver {

    // network
    private String httpUrlPrefix;
    private String wsUrlPrefix;
    private String username;
    private boolean validateSSL;
    HttpClient client = null;
    GameWebSocketClient webSocketClient = null;

    // ai
    private DecisionEngine decisionEngine;

    // info about game
    private GameState gameState;

    private static final Logger logger = LogManager.getLogger(AiUser.class);

    public AiUser(String urlPrefix, String username, boolean validateSSL) {

        // network
        this.httpUrlPrefix = "https://" + urlPrefix;
        this.wsUrlPrefix = "wss://" + urlPrefix;
        this.validateSSL = validateSSL;

        // for web application
        this.username = username;

        // information regarding the progress of the game
        this.gameState = null;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        new Thread(() -> {
            try {
                HttpClientSSLContext httpClientSSLContext
                        = new HttpClientSSLContext(validateSSL);
                this.client = httpClientSSLContext.getHttpClient();

                // registrer
                httpClientSSLContext.sendGetRequest(httpUrlPrefix + "registration");
                String registrationParams =
                        "username="
                                + URLEncoder.encode(username, "UTF-8")
                                + "&password=123&email=ai.ai@ai.com&message=I'm clever";
                httpClientSSLContext.sendPostRequest(
                        httpUrlPrefix + "registration",
                        registrationParams
                );

                // authenticate
                String authParams =
                        "username="
                                + URLEncoder.encode(username, "UTF-8")
                                + "&password=123";
                httpClientSSLContext.sendPostRequest(
                        httpUrlPrefix + "authentication",
                        authParams
                );

                // fetch playerId
                User user = null;
                int userId = 0;
                try {
                    user = fetchUser(httpClientSSLContext, httpUrlPrefix);
                    if (user != null) {
                        logger.info("user.getId(): " + user.getId());
                        logger.info("user.getUsername(): " + user.getUsername());
                    }
                    userId = user != null ? user.getId() : 0;
                } catch (Exception e) {
                    logger.error("Error processing user data", e);
                }
                String playerId = String.valueOf(userId);
                logger.info("playerId:" + playerId);

                this.decisionEngine = new DecisionEngine();

                // proposer a game
                String postParams = "action=" + "new_game";
                httpClientSSLContext.sendPostRequest(
                        httpUrlPrefix + "home",
                        postParams
                );

                // fetch game id in order to request with the suitable
                // websocket url
                Game game = null;
                int gameId = 0;
                //game = fetchGame(client, urlPrefix);
                logger.info("here 4");
                game = fetchGame(httpClientSSLContext, httpUrlPrefix);
                logger.info("here 5");
                gameId = game != null ? game.getId() : 0;
                logger.info("here 6");

                // create webSocket client and connect it to the server
                // related to the game
                this.webSocketClient =
                        new GameWebSocketClient(
                                client,
                                wsUrlPrefix + "game/" + gameId
                        );

                // make it an observer
                webSocketClient.addObserver(this);

                // récupérer régulièrement l'information de game.GameState jusqu'à
                // que valle GameState.WAIT_FIRST_PLAYER_MOVE
                GameState gameState = null;
                int pollingInterval = 5000;
                do {
                    game = fetchGame(httpClientSSLContext, httpUrlPrefix);
                    if (game != null) {
                        gameState = game.getGameState();
                    } else {
                        logger.info("No game information retrieved");
                    }
                    if (gameState != GameState.WAIT_FIRST_PLAYER_MOVE) {
                        try {
                            Thread.sleep(pollingInterval);
                        } catch (InterruptedException e) {
                            logger.info("Polling interrupted", e);
                        }
                    }
                } while (gameState != GameState.WAIT_FIRST_PLAYER_MOVE);

                do {
                    if (gameState == GameState.WAIT_FIRST_PLAYER_MOVE) {

                        // play the best move
                        Column bestMove;
                        bestMove = decisionEngine.getBestMove();
                        // inform the opponent through the web application via
                        // websocket client
                        webSocketClient.getFutureWebSocket().join();
                        webSocketClient.sendMessage(
                                "{\"update\":\"colorsGrid\",\"column\":"
                                        + bestMove.getIndex()
                                        +",\"playerId\":\""
                                        +playerId
                                        +"\"}"
                        );
                    }
                    try {
                        Thread.sleep(pollingInterval);
                    } catch (InterruptedException e) {
                        logger.info("Polling interrupted", e);
                    }

                    if ((game = fetchGame(httpClientSSLContext, httpUrlPrefix)) != null) {
                        gameState = game.getGameState();
                    } else {
                        logger.info("No game information retrieved");
                    }
                } while (
                        (gameState == GameState.WAIT_FIRST_PLAYER_MOVE)
                        ||
                        (gameState == GameState.WAIT_SECOND_PLAYER_MOVE)
                );

                String response = "Task terminated!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                logger.info("Error handling request: " + e.getMessage(), e);
            } finally {
                try {
                    if (webSocketClient != null) {
                        webSocketClient.close();
                    }
                    if (client != null) {
                        client.close();
                    }
                } catch (Exception e) {
                    logger.error("Failed to clean up resources", e);
                }
                exchange.close();
            }
        }).start();
    }

    public User fetchUser(HttpClientSSLContext client, String urlPrefix) {
        String userJson = null;
        UserContainer userContainer = null;
        User user = null;

        try {
            userJson = client.sendGetRequest(urlPrefix + "who-am-i");
            logger.info("here 1 ; userJson:" + userJson);

            // deserialize JSON to UserContainer
            ObjectMapper mapper = new ObjectMapper();
            userContainer = mapper.readValue(userJson, UserContainer.class); // Désérialisation avec Jackson
            logger.info("here 2 ; userContainer:" + userContainer);
            user = userContainer != null ? userContainer.getUser() : null;
            logger.info("here 3 ; user:" + user);
        } catch (Exception e) {
            logger.error("Error handling request", e);
        }

        return user;
    }

    public Game fetchGame(HttpClientSSLContext client, String urlPrefix) {
        String gameJson = null;
        GameContainer container = null;
        Game game = null;

        try {
            // fetch game from the server
            gameJson = client.sendGetRequest(urlPrefix + "how-is-my-game");

            // configure Jackson ObjectMapper with custom deserializers
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Game.class, new CustomGameDeserializer());
            module.addDeserializer(Coordinates.class, new CustomCoordinatesDeserializer());
            module.addKeyDeserializer(Coordinates.class, new CoordinatesKeyDeserializer());
            module.addDeserializer(Map.class, new CustomColorsGridDeserializer());
            mapper.registerModule(module);

            // deserialize JSON to GameContainer
            container = mapper.readValue(gameJson, GameContainer.class);
            game = container != null ? container.getGame() : null;

        } catch (Exception e) {
            logger.error("Error handling request", e);
        }
        // return the game object or null if there was an error
        return game;
    }

    public void update(GameEvent gameEvent) {
        switch (gameEvent.getWhatToBeUpdated()) {
            case "colorsGrid":
                decisionEngine.updateWithMove(
                    ((ColorsGridUpdateDescription)
                        gameEvent
                            .getDescription())
                            .getColumn()
                );
                logger.info("colorsGrid ; column:"+((ColorsGridUpdateDescription)
                        gameEvent
                                .getDescription())
                        .getColumn());
                break;
            case "gameState":
                this.gameState =
                    ((GameStateUpdateDescription)
                        gameEvent
                            .getDescription())
                            .getGameState();
                logger.info("gameState:"+((GameStateUpdateDescription)
                        gameEvent
                                .getDescription())
                        .getGameState());
                break;
        }
    }
}
