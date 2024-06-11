package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import online.caltuli.batch.userInteractionSimulation.clients.GameWebSocketClientV2;
import online.caltuli.batch.userInteractionSimulation.clients.HttpClientSSLContextV2;
import online.caltuli.batch.userInteractionSimulation.config.network.ClientConfig;
import online.caltuli.batch.userInteractionSimulation.config.network.NetworkConfig;
import online.caltuli.batch.userInteractionSimulation.config.virtualUsers.VirtualUserInformationConfig;
import online.caltuli.batch.userInteractionSimulation.interfaces.GameObserver;
import online.caltuli.batch.userInteractionSimulation.jsonUtils.*;
import online.caltuli.business.ai.Column;
import online.caltuli.model.Coordinates;
import online.caltuli.model.Game;
import online.caltuli.model.GameState;
import online.caltuli.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserActionManager {
    private VirtualUserInformationConfig virtualUserInformationConfig;
    private NetworkConfig httpsNetworkConfig;
    private NetworkConfig wssNetworkConfig;
    private ClientConfig clientConfig;
    private HttpClientSSLContextV2 httpClientSSLContext;
    private HttpClient httpClient;
    private GameWebSocketClientV2 gameWebSocketClient;


    protected static final Logger logger = LogManager.getLogger(UserActionManager.class);

    public UserActionManager(
            VirtualUserInformationConfig virtualUserInformationConfig,
            NetworkConfig httpsNetworkConfig,
            NetworkConfig wssNetworkConfig,
            ClientConfig clientConfig)
            throws
                NoSuchAlgorithmException,
                KeyManagementException,
                KeyStoreException {

        // configuration
        this.virtualUserInformationConfig = virtualUserInformationConfig;
        this.httpsNetworkConfig = httpsNetworkConfig;
        this.wssNetworkConfig = wssNetworkConfig;
        this.clientConfig = clientConfig;

        initializeClients();
    }

    public void initializeClients()
            throws
            NoSuchAlgorithmException,
            KeyManagementException,
            KeyStoreException {
        // http client initialisation
        this.httpClientSSLContext =
            new HttpClientSSLContextV2(
                httpsNetworkConfig,
                clientConfig
            );
        this.httpClient =
                this.httpClientSSLContext.getHttpClient();

        // wss client initialisation
        gameWebSocketClient =
            new GameWebSocketClientV2(
                this.httpClient,
                wssNetworkConfig
                );
    }

    public void cleanupClients() {
        logger.info("Cleaning up UserActionManager resources...");

        // Fermeture du client WebSocket
        if (gameWebSocketClient != null) {
            gameWebSocketClient.disconnectFromGame();
            gameWebSocketClient = null;
        }
        gameWebSocketClient =
            new GameWebSocketClientV2(
                    this.httpClient,
                    wssNetworkConfig
            );

        if (httpClient != null) {
            httpClientSSLContext.cleanupCookies();
        }
    }


    public void register() throws Exception {
        this.httpClientSSLContext.sendGetRequest("registration");
        httpClientSSLContext.sendPostRequest(
            "registration",
            registrationPostParameter()
        );
    }

    public void authenticate() throws Exception {
        logger.info("authenticate");
        logger.info("authenticationPostParameter() -> "+authenticationPostParameter());
        this.httpClientSSLContext.sendGetRequest("authentication");
        httpClientSSLContext.sendPostRequest(
            "authentication",
           authenticationPostParameter()
        );
    }

    public void logout() throws Exception {
        logger.info("logout");
        this.httpClientSSLContext.sendGetRequest("logout");
    }

    public void proposeNewGame() throws Exception {
        logger.info("proposeNewGame()");
        logger.info("proposeNewGamePostParameter() -> "+ proposeNewGamePostParameter());
        this.httpClientSSLContext.sendGetRequest("home");
        httpClientSSLContext.sendPostRequest(
            "home",
            proposeNewGamePostParameter()
        );
    }

    public void makeMeObserveAndPlayGame(GameObserver player) {
        gameWebSocketClient.connectToGame(fetchGame().getId());
        gameWebSocketClient.addObserver(player);
    }

    public void waitOpponent() {
        GameState gameState = null;
        int pollingInterval = 5000;
        Game game;
        do {
            game = fetchGame();
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
    }

    public void playMove(Column column) {
        this.gameWebSocketClient.sendMessage(
            "{\"update\":\"colorsGrid\",\"column\":"
            + column.getIndex()
            + ",\"playerId\":\""
            + fetchUser().getId()
            + "\"}"
        );
    }

    private String registrationPostParameter() {
        return
                "username="
                        + virtualUserInformationConfig.getUsername()
                        + "&password="
                        + virtualUserInformationConfig.getPassword()
                        + "&email="
                        + virtualUserInformationConfig.getEmail()
                        + "&message="
                        + virtualUserInformationConfig.getMessage();
    }

    private String authenticationPostParameter() {
        return
            "username="
                    + virtualUserInformationConfig.getUsername()
                    + "&password="
                    + virtualUserInformationConfig.getPassword();
    }

    private String proposeNewGamePostParameter() {
        return "action=new_game";
    }

    private void connectGameWebSocketClient(int gameId) {

    }


    // actions related to API

    public User fetchUser() {
        String userJson = null;
        UserContainer userContainer = null;
        User user = null;
        try {
            userJson = this.httpClientSSLContext.sendGetRequest("who-am-i");
            // deserialize JSON to UserContainer
            ObjectMapper mapper = new ObjectMapper();
            userContainer = mapper.readValue(userJson, UserContainer.class); // Désérialisation avec Jackson
            user = userContainer != null ? userContainer.getUser() : null;
        } catch (Exception e) {
            logger.error("Error handling request", e);
        }
        return user;
    }

    public Game fetchGame() {
        String gameJson = null;
        GameContainer container = null;
        Game game = null;
        try {
            // fetch game from the server
            gameJson = this.httpClientSSLContext.sendGetRequest("how-is-my-game");
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

    public HttpClientSSLContextV2 getHttpClientSSLContext() {
        return httpClientSSLContext;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public GameWebSocketClientV2 getGameWebSocketClient() {
        return gameWebSocketClient;
    }
}
