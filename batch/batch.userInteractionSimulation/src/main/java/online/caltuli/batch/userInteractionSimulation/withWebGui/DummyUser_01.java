package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class DummyUser_01 implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(DummyUser_01.class);

    private static final String DEFAULT_URL_PREFIX = "https://localhost:8443/webapp/";
    private String urlPrefix;
    private String user;

    public DummyUser_01() {
        this.urlPrefix = DEFAULT_URL_PREFIX;
        this.user = "fake-user";
    }

    public DummyUser_01(String urlPrefix, String user) {
        this.urlPrefix = urlPrefix;
        this.user = user;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

        // Gérer la requête de pré-vérification (OPTIONS)
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1); // 204 No Content, -1 signifie que le corps de la réponse est vide
            return;
        }

        String sessionId = UUID.randomUUID().toString(); // Génération d'un nouveau UUID
        String cookieValue = "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; Secure; SameSite=None";
        exchange.getResponseHeaders().set("Set-Cookie", cookieValue);

        new Thread(() -> {
            try {
                HttpClient client = createClient();

                sendGetRequest(client, urlPrefix + "home");
                sendGetRequest(client, urlPrefix + "registration");

                String registrationParams = "username=" + URLEncoder.encode(user, "UTF-8") +
                        "&password=123&email=ai.ai@ai.com&message=I'm clever";
                sendPostRequest(client, urlPrefix + "registration", registrationParams);

                String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                sendPostRequest(client, urlPrefix + "authentication", authParams);

                Thread.sleep(10000);

                sendGetRequest(client, urlPrefix + "logout");

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

    private HttpClient createClient() {
        return HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();
    }

    private String sendGetRequest(HttpClient client, String urlString) throws Exception {
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

        Thread.sleep(10000);

        jsessionId.ifPresent(jsid -> logger.info("JSESSIONID: " + jsid.substring("JSESSIONID=".length())));

        return response.body();
    }

    private String sendPostRequest(HttpClient client, String urlString, String postParams) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(postParams))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
