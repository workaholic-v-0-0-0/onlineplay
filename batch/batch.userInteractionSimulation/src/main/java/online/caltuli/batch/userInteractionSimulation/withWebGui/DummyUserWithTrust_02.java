package online.caltuli.batch.userInteractionSimulation.withWebGui;

import online.caltuli.model.User;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DummyUserWithTrust_02 implements HttpHandler {

    private final Logger logger = LogManager.getLogger(DummyUserWithTrust_02.class);

    private static final String DEFAULT_URL_PREFIX = "https://localhost:8443/webapp/";
    private static final String ALTERNATE_URL_PREFIX = "https://caltuli.online/webapp/";
    private static final String ALTERNATE_URL_PREFIX_02 = "https://caltuli.online/webapp_version_damien/";
    private static final String ALTERNATE_URL_PREFIX_03 = "https://caltuli.online/webapp_version_samya/";
    private static final String ALTERNATE_URL_PREFIX_04 = "https://caltuli.online/webapp_version_sylvain/";
    private static final String USER_AGENT = "Mozilla/5.0";

    private String urlPrefix;
    String user;

    DummyUserWithTrust_02() {
        String urlPrefix = DEFAULT_URL_PREFIX;
        String user = "fake-user";
    }

    DummyUserWithTrust_02(String urlPrefix, String user) {
        this.urlPrefix = urlPrefix;
        this.user = user;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {


        new Thread(() -> {
            try {
                logger.info("here 1");
                HttpClient client = createTrustedClient();
                logger.info("here 2");

                String homeResponse = sendTrustedGetRequest(client, urlPrefix + "home");
                logger.info("here 3");
                sendTrustedGetRequest(client, urlPrefix + "registration");
                logger.info("here 4");

                String registrationParams = "username=" + URLEncoder.encode(user, "UTF-8") +
                        "&password=123&email=ai.ai@ai.com&message=I'm clever";
                sendTrustedPostRequest(client, urlPrefix + "registration", registrationParams);
                logger.info("here 5");

                String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                sendTrustedPostRequest(client, urlPrefix + "authentication", authParams);
                logger.info("here 6");

                // propose a game
                String postParams = "action=" + "new_game";
                sendTrustedPostRequest(client, urlPrefix + "home", postParams);
                logger.info("here 7");

                // TEST FAIT PAR HASARD EN SE TROMPANT MAIS QUI SERA UTILE PLUS TARD
                // Effectuer une requête GET pour obtenir les informations de l'utilisateur
                /*
                String userJson = sendTrustedGetRequest(client, urlPrefix + "who-am-i");
                logger.info("here 2");
                logger.info("Response JSON: " + userJson);
                Gson gson = new Gson();

                UserContainer container = gson.fromJson(userJson, UserContainer.class);
                UserDTO userDto = container != null ? container.getUser() : null;

                logger.info("userDto: " + userDto);
                if (userDto != null) {
                    logger.info("userDto.getId(): " + userDto.getId());
                    logger.info("userDto.getUsername(): " + userDto.getUsername());
                    logger.info("userDto.getPasswordHash(): " + userDto.getPasswordHash());
                    logger.info("userDto.getEmail(): " + userDto.getEmail());
                }

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

                for (int i=0 ; i<8 ; i++) {
                    Thread.sleep(5000);  // Simulate delay
                    sendTrustedGetRequest(client, urlPrefix + "home");
                }

                sendTrustedGetRequest(client, urlPrefix + "logout");

                String response = "Task terminated!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                logger.error("Error handling request: " + e.getMessage(), e);
            } finally {
                exchange.close();
            }
        }).start();
    }

    private HttpClient createTrustedClient() throws NoSuchAlgorithmException, KeyManagementException {
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

        CookieManager cookieManager = new CookieManager();  // Create a new CookieManager for each client
        return HttpClient.newBuilder()
                .sslContext(sslContext)
                .cookieHandler(cookieManager)  // Set the new CookieManager to the client
                .build();
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

}
