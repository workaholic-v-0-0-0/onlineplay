package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import online.caltuli.batch.userInteractionSimulation.clients.GameWebSocketClient;
import online.caltuli.batch.userInteractionSimulation.clients.HttpClientSSLContext;
import online.caltuli.batch.userInteractionSimulation.jsonUtils.*;
import online.caltuli.model.Game;
import online.caltuli.model.GameState;
import online.caltuli.model.User;
import online.caltuli.model.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.Random;

public class DummyUser implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(DummyUser.class);
    private String httpUrlPrefix;
    private String wsUrlPrefix;

    private String username;
    private boolean validateSSL;

    public DummyUser(String urlPrefix, String username, boolean validateSSL) {
        this.httpUrlPrefix = "https://" + urlPrefix;
        this.wsUrlPrefix = "wss://" + urlPrefix;
        this.username = username;
        this.validateSSL = validateSSL;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        new Thread(() -> {
            try {
                HttpClientSSLContext httpClientSSLContext
                        = new HttpClientSSLContext(true);
                HttpClient client = httpClientSSLContext.getHttpClient();

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
                GameWebSocketClient webSocketClient =
                    new GameWebSocketClient(
                            client,
                             wsUrlPrefix + "game/" + gameId
                    );

                // récupérer régulièrement l'information de game.GameState jusqu'à
                // que valle GameState.WAIT_FIRST_PLAYER_MOVE
                GameState gameState = null;
                int pollingInterval = 5000;
                do {
                    //game = fetchGame(client, urlPrefix);
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
                logger.info("Game is now in state: WAIT_FIRST_PLAYER_MOVE");

                // jouer un coup au hasard à chaque fois que c'est son tour
                // jusqu'à la fin de la partie
                Random rand = null;
                int columnIndex = 0;
                String moveMessage = null;
                rand = new Random();
                do {
                    if (gameState == GameState.WAIT_FIRST_PLAYER_MOVE) {
                        columnIndex = rand.nextInt(7);
                        moveMessage = String.format(
                                "{\"update\":\"colorsGrid\", \"column\":%d, \"playerId\":\"%s\"}",
                                columnIndex,
                                playerId
                        );
                        webSocketClient.getFutureWebSocket().join();
                        webSocketClient.sendMessage(moveMessage);
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

        // return the game object or null if there was an error
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
}