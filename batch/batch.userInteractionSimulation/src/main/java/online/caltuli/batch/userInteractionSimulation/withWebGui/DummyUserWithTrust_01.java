package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import online.caltuli.batch.userInteractionSimulation.withWebGui.clients.HttpClientSSLContext;
import online.caltuli.batch.userInteractionSimulation.withWebGui.jsonUtils.CoordinatesKeyDeserializer;
import online.caltuli.batch.userInteractionSimulation.withWebGui.jsonUtils.CustomColorsGridDeserializer;
import online.caltuli.batch.userInteractionSimulation.withWebGui.jsonUtils.CustomCoordinatesDeserializer;
import online.caltuli.batch.userInteractionSimulation.withWebGui.jsonUtils.CustomGameDeserializer;
import online.caltuli.model.Game;
import online.caltuli.model.GameState;
import online.caltuli.model.User;
import online.caltuli.model.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class DummyUserWithTrust_01 implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(DummyUserWithTrust_01.class);
    private String urlPrefix;
    String username;

    DummyUserWithTrust_01(String urlPrefix, String username) {
        this.urlPrefix = urlPrefix;
        this.username = username;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        new Thread(() -> {
            try {
                //HttpClientSSLContext httpClientSSLContext = createTrustedClient();
                HttpClientSSLContext httpClientSSLContext = new HttpClientSSLContext(true);
                HttpClient client = httpClientSSLContext.getHttpClient();

                // registrer
                //sendTrustedGetRequest(client, urlPrefix + "registration");
                httpClientSSLContext.sendGetRequest(urlPrefix + "registration");
                String registrationParams =
                    "username="
                    + URLEncoder.encode(username, "UTF-8")
                    + "&password=123&email=ai.ai@ai.com&message=I'm clever";
                /*
                sendTrustedPostRequest(
                        client,
                        urlPrefix + "registration",
                        registrationParams
                );

                 */
                httpClientSSLContext.sendPostRequest(
                        urlPrefix + "registration",
                        registrationParams
                );

                // authenticate
                String authParams =
                    "username="
                    + URLEncoder.encode(username, "UTF-8")
                    + "&password=123";
                /*
                sendTrustedPostRequest(
                        client,
                        urlPrefix + "authentication",
                        authParams
                );

                 */
                httpClientSSLContext.sendPostRequest(
                        urlPrefix + "authentication",
                        authParams
                );

                // fetch playerId
                User user = null;
                int userId = 0;
                try {
                    //user = fetchUser(client, urlPrefix);
                    user = fetchUser(httpClientSSLContext, urlPrefix);
                    if (user != null) {
                        logger.info("user.getId(): " + user.getId());
                        logger.info("user.getUsername(): " + user.getUsername());
                    }
                    userId = user != null ? user.getId() : 0;
                } catch (Exception e) {
                    logger.error("Error processing user data", e);
                }
                String playerId = String.valueOf(userId);

                Thread.sleep(1000);

                // proposer a game
                String postParams = "action=" + "new_game";
                /*
                sendTrustedPostRequest(
                        client,
                        urlPrefix + "home",
                        postParams
                );

                 */
                httpClientSSLContext.sendPostRequest(
                        urlPrefix + "home",
                        postParams
                );

                // fetch game id in order to request with the suitable
                // websocket url
                Game game = null;
                int gameId = 0;
                //game = fetchGame(client, urlPrefix);
                game = fetchGame(httpClientSSLContext, urlPrefix);
                gameId = game != null ? game.getId() : 0;

                // create webSocket client and connect it to the server
                // related to the game
                GameWebSocketClient webSocketClient =
                    new GameWebSocketClient(
                            client,
                            "wss://localhost:8443/webapp/game/" + gameId
                    );

                // récupérer régulièrement l'information de game.GameState jusqu'à
                // que valle GameState.WAIT_FIRST_PLAYER_MOVE
                GameState gameState = null;
                int pollingInterval = 5000;
                do {
                    //game = fetchGame(client, urlPrefix);
                    game = fetchGame(httpClientSSLContext, urlPrefix);
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

                    //if ((game = fetchGame(client, urlPrefix)) != null) {
                    if ((game = fetchGame(httpClientSSLContext, urlPrefix)) != null) {
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

    // Custom class to hold multiple objects
    /*
    public static class HttpClientSSLContext {
        private final HttpClient httpClient;
        private final SSLContext sslContext;

        public HttpClientSSLContext(HttpClient httpClient, SSLContext sslContext) {
            this.httpClient = httpClient;
            this.sslContext = sslContext;
        }

        public HttpClient getHttpClient() {
            return httpClient;
        }

        public SSLContext getSslContext() {
            return sslContext;
        }
    }

     */

    /*
    private HttpClientSSLContext createTrustedClient()
            throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[]
                getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType
                ) {

                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType
                ) {

                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpClient httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .cookieHandler(cookieManager)
                .build();

        return new HttpClientSSLContext(httpClient, sslContext);
    }

     */

    /*
    private String sendTrustedGetRequest(HttpClient client, String urlString)
            throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(urlString))
            .GET()
            .build();
        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<String> jsessionId =
            response
                .headers()
                .firstValue("Set-Cookie")
                .map(
                    cookie -> Arrays.stream(cookie.split(";"))
                        .filter(c -> c.trim().startsWith("JSESSIONID="))
                        .findFirst()
                        .orElse(null)
                );
        jsessionId.ifPresent(
            jsid -> logger.info(
                "JSESSIONID: " + jsid.substring("JSESSIONID=".length())));
        return response.body();
    }

     */

    /*
    private String sendTrustedPostRequest(
            HttpClient client,
            String urlString,
            String postParams)
            throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(postParams))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

     */

    //public User fetchUser(HttpClient client, String urlPrefix) {
    public User fetchUser(HttpClientSSLContext client, String urlPrefix) {
        String userJson = null;
        UserContainer userContainer = null;
        User user = null;

        try {
            // fetch user from the server
            /*
            userJson = sendTrustedGetRequest(
                    client,
                    urlPrefix + "who-am-i"
            );

             */
            userJson = client.sendGetRequest(urlPrefix + "who-am-i");

            // deserialize JSON to UserContainer
            ObjectMapper mapper = new ObjectMapper();
            userContainer = mapper.readValue(userJson, UserContainer.class); // Désérialisation avec Jackson
            user = userContainer != null ? userContainer.getUser() : null;
        } catch (Exception e) {
            logger.error("Error handling request", e);
        }

        // return the game object or null if there was an error
        return user;
    }

    //public Game fetchGame(HttpClient client, String urlPrefix) {
    public Game fetchGame(HttpClientSSLContext client, String urlPrefix) {
        String gameJson = null;
        GameContainer container = null;
        Game game = null;

        try {
            // fetch game from the server
            //gameJson = sendTrustedGetRequest(client, urlPrefix + "how-is-my-game");
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