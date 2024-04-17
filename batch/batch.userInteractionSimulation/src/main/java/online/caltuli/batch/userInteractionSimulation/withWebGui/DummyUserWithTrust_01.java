package online.caltuli.batch.userInteractionSimulation.withWebGui;

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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

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

    private final Logger logger = LogManager.getLogger(DummyUser_01.class);

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
                HttpClient client = createTrustedClient();

                String homeResponse = sendTrustedGetRequest(client, urlPrefix + "home");
                sendTrustedGetRequest(client, urlPrefix + "registration");

                String registrationParams = "username=" + URLEncoder.encode(user, "UTF-8") +
                        "&password=123&email=ai.ai@ai.com&message=I'm clever";
                sendTrustedPostRequest(client, urlPrefix + "registration", registrationParams);

                String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                sendTrustedPostRequest(client, urlPrefix + "authentication", authParams);

                Thread.sleep(10000);  // Simulate delay

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

