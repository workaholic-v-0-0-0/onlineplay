package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import online.caltuli.model.Game;
import online.caltuli.model.GameState;
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
import java.net.http.WebSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/*
 * Sends an HTTP GET request while trusting all SSL certificates. This method is specifically
 * designed for local testing environments where the application may be using self-signed
 * certificates. In such development setups, self-signed certificates are common and not
 * validated by standard certificate authorities, typically leading to SSL validation errors.
 * By setting up an SSLContext that trusts all certificates, we bypass SSL certificate
 * verification to prevent these errors during local tests. This approach mirrors the behavior
 * of using the '-k' or '--insecure' flag in curl, allowing HTTPS connections without
 * verifying the trust chain.
 *
 * Note: This method should only be used for testing purposes in a local development environment
 * and never in production, as it removes the security assurances that SSL/TLS is supposed to provide.
 *
 * @param urlString The URL to which the GET request will be sent.
 * @return The server's response as a string, or an error message if the request fails.
 * @throws Exception If there is any issue in setting up the SSL context or during the connection.
 */
public class DummyUserWithTrust_01 implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(DummyUserWithTrust_01.class);

    private static final String DEFAULT_URL_PREFIX = "https://localhost:8443/webapp/";
    private static final String ALTERNATE_URL_PREFIX = "https://caltuli.online/webapp/";
    private static final String ALTERNATE_URL_PREFIX_02 = "https://caltuli.online/webapp_version_damien/";
    private static final String ALTERNATE_URL_PREFIX_03 = "https://caltuli.online/webapp_version_samya/";
    private static final String ALTERNATE_URL_PREFIX_04 = "https://caltuli.online/webapp_version_sylvain/";
    private static final String USER_AGENT = "Mozilla/5.0";

    private String urlPrefix;
    String user;

    DummyUserWithTrust_01() {
        String urlPrefix = DEFAULT_URL_PREFIX;
        String user = "fake-user";
    }

    DummyUserWithTrust_01(String urlPrefix, String user) {
        this.urlPrefix = urlPrefix;
        this.user = user;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        new Thread(() -> {
            try {
                logger.info("here 1");
                HttpClientSSLContext httpClientSSLContext = createTrustedClient();
                HttpClient client = httpClientSSLContext.getHttpClient();

                String homeResponse = sendTrustedGetRequest(client, urlPrefix + "home");
                sendTrustedGetRequest(client, urlPrefix + "registration");

                String registrationParams = "username=" + URLEncoder.encode(user, "UTF-8") +
                        "&password=123&email=ai.ai@ai.com&message=I'm clever";
                sendTrustedPostRequest(client, urlPrefix + "registration", registrationParams);

                String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                sendTrustedPostRequest(client, urlPrefix + "authentication", authParams);

                Thread.sleep(10000);  // Simulate delay
                //sendTrustedGetRequest(client, urlPrefix + "logout");

                // OUF MAINTENANT LA SUITE FONCTIONNE
                // MAINTENANT IL FAUT :
                // -1) proposer une partie
                // 0) récupérer l'identifiant de la partie (pour pouvoir faire des requêtes
                // websocket)
                // 1) récupérer régulièrement l'information de game.GameState jusqu'à
                // que valle GameState.WAIT_FIRST_PLAYER_MOVE
                // 2) jouer un coup au hasard jusqu'à ce que le coup soit légal
                // 3) récupérer l'information de GameState
                // 4) si GameState.DRAW ou GameState.*WON, se déconnecter
                // 5) sinon goto 1)

                // DONC IL FAUT DANS webapp.servlet.api une servlet qui retourne les infos
                // de la partie

                // DONC il faut récupérer ...??? pas la peine de récupérer colorGrid
                // car dans le module pour l'IA, on aura une dépendance avec webapp
                // qui permettra de récupérer la même instance du bean CDI
                // currentModel et par ce biais le EvolutiveGridParser concerné

                // POURQUOI PAS VIA WEBSOCKET ? -> non servler renvoie du Json c'est ok

                // DONC JE ME DÉCIDE :
                // il faut un servlet pour :
                // avec son doPost on envoie l'id du user, et il renvoie ...

                // NON C'EST BEAUCOUP PLUS SIMPLE QUE ÇA VIA LA SESSION, on se
                // de doGet finallement et dedans on récupère la session puis l'instance
                // de GameManager si celui-ci est concerné par une partie ;
                // on fait renvoyer gameManager.game

                // ou juste gameManager.game.gameState

                // -1) proposer une partie
                String postParams = "action=" + "new_game";
                sendTrustedPostRequest(client, urlPrefix + "home", postParams);

                String gameJson = null;
                Gson gson = null;
                GameContainer container = null;
                Game game = null;
                int gameId = 0;
                GameState gameState = null;

                // 0) récupérer l'identifiant de la partie (pour pouvoir faire des requêtes
                // websocket)
                gameJson = sendTrustedGetRequest(client, urlPrefix + "how-is-my-game");
                logger.info("Response JSON: " + gameJson);
                gson = new Gson();
                container = gson.fromJson(gameJson, GameContainer.class);
                game = container != null ? container.getGame() : null;
                logger.info("game: " + game);
                if (game != null) {
                    logger.info("game.getId(): " + game.getId());
                    logger.info("game.getFirstPlayer(): " + game.getFirstPlayer());
                    logger.info("game.getSecondPlayer(): " + game.getSecondPlayer());
                    logger.info("game.getGameState(): " + game.getGameState());
                }
                gameId = game.getId();

                // 1) récupérer régulièrement l'information de game.GameState jusqu'à
                // que valle GameState.WAIT_FIRST_PLAYER_MOVE
                int pollingInterval = 5000; // Par exemple, interroger toutes les 5 secondes

                // until game.gameState is not WAIT_FIRST_PLAYER_MOVE
                do {
                    gameJson = sendTrustedGetRequest(
                            client,
                            urlPrefix + "how-is-my-game"
                    );
                    logger.info("Response JSON: " + gameJson);
                    gson = new Gson();
                    container = gson.fromJson(gameJson, GameContainer.class);
                    game = container != null ? container.getGame() : null;
                    if (game != null) {
                        logger.info("game: " + game);
                        logger.info("game.getId(): " + game.getId());
                        logger.info("game.getFirstPlayer(): " + game.getFirstPlayer());
                        logger.info("game.getSecondPlayer(): " + game.getSecondPlayer());
                        logger.info("game.getGameState(): " + game.getGameState());
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

                // IL Y A UN PROBLÈME
                // quand on fait new game avec un vrai utilisateur, on a bien une nouvelle
                // session comme le dit le log avec par exemple
                // 2024-05-24 17:23:31 INFO  GameWebSocket - sessions: {208=[org.apache.tomcat.websocket.WsSession@5a91facc]}
                // mais avec ce thread, on n'a pas de nouvelle session :
                // 2024-05-24 17:23:30 INFO  Home - GameWebSocket.sessions:{}
                // POURQUOI ???


                // TEST FAIT PAR HASARD EN SE TROMPANT MAIS QUI SERA UTILE PLUS TARD
                // Effectuer une requête GET pour obtenir les informations de l'utilisateur
                /*
                [...]
                if (userDto != null && userDto.getId() > 0) {
                    // propose a game avec l'ID obtenu
                    String actionValue = "play_with";
                    String userIdValue = String.valueOf(userDto.getId());
                    logger.info("userIdValue: " + userIdValue);

                    // ICI
                    // LA REQUÊTE POST NE MARCHE PAS
                    String encodedAction = URLEncoder.encode(actionValue, StandardCharsets.UTF_8.toString());
                    String encodedUserId = URLEncoder.encode(userIdValue, StandardCharsets.UTF_8.toString());
                    String postParams = "action=" + encodedAction + "&user_id=" + encodedUserId;
                    logger.info("here 3");
                    sendTrustedPostRequest(client, urlPrefix + "home", postParams);
                    logger.info("here 4");

                    //String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                    //sendTrustedPostRequest(client, urlPrefix + "authentication", authParams);

                } else {
                    logger.info("Failed to get user details or user is not logged in.");
                    System.out.println("Failed to get user details or user is not logged in.");
                }

                logger.info("get id:" + userDto.getId());
                 */

                Thread.sleep(5000);

                logger.info("here 2");
                // Connect to WebSocket and simulate playing a move
                String websocketUri = "wss://localhost:8443/webapp/game/" + gameId;
                //String websocketUri = "wss://caltuli.online/webapp_version_sylvain/game/45";
                int columnIndex = 3; // Exemple de valeur de la colonne
                String playerId = "2"; // Exemple d'identifiant de joueur

                String moveMessage = String.format("{\"update\":\"colorsGrid\", \"column\":%d, \"playerId\":\"%s\"}", columnIndex, playerId);

                connectWebSocket(client, websocketUri, moveMessage);
                logger.info("here 3");

                Thread.sleep(30000);
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

    private HttpClientSSLContext createTrustedClient() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
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

    private String sendTrustedGetRequest(HttpClient client, String urlString) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Optional<String> jsessionId = response.headers().firstValue("Set-Cookie")
                .map(cookie -> Arrays.stream(cookie.split(";"))
                        .filter(c -> c.trim().startsWith("JSESSIONID="))
                        .findFirst()
                        .orElse(null));

        jsessionId.ifPresent(jsid -> logger.info("JSESSIONID: " + jsid.substring("JSESSIONID=".length())));

        return response.body();
    }

    private String sendTrustedPostRequest(HttpClient client, String urlString, String postParams) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(postParams))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private WebSocket connectWebSocket(HttpClient client, String websocketUri, String messageToSend) {
        logger.info("Starting WebSocket connection to URI: " + websocketUri);
        CompletableFuture<WebSocket> wsFuture = new CompletableFuture<>();

        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                logger.info("Received response: " + data);
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }
            @Override
            public void onOpen(WebSocket webSocket) {
                logger.info("WebSocket opened");
                webSocket.request(1);
                webSocket.sendText(messageToSend, true);
            }

            @Override
            public CompletionStage<Void> onClose(WebSocket webSocket, int statusCode, String reason) {
                logger.info("WebSocket closed: " + statusCode + " " + reason);
                return CompletableFuture.completedFuture(null);
            }
            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                logger.error("WebSocket connection encountered an error: " + error.getMessage());
            }
        };

        client.newWebSocketBuilder()
                .buildAsync(URI.create(websocketUri), listener)
                .thenAccept(wsFuture::complete)
                .exceptionally(e -> {
                    logger.error("Failed to establish WebSocket connection: " + e.getMessage(), e);
                    return null;
                });

        logger.info("WebSocket connection setup initiated.");
        return wsFuture.join(); // Synchronously wait for the WebSocket to be created
    }



}

