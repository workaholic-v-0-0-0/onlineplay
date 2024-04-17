package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CookieManager cookieManager = new CookieManager();
                    CookieHandler.setDefault(cookieManager);

                    //String response = sendGetRequest(urlPrefix + "home");
                    String response = sendTrustedGetRequest(urlPrefix + "home");
                    //logger.info("Server response : " + response);
                    //sendGetRequest(urlPrefix + "registration");
                    sendTrustedGetRequest(urlPrefix + "registration");
                    String registrationParams = "username=" + URLEncoder.encode(user, "UTF-8") +
                            "&password=123&email=ai.ai@ai.com&message=I'm clever";
                    //sendPostRequest(urlPrefix + "registration", registrationParams);
                    sendTrustedPostRequest(urlPrefix + "registration", registrationParams);

                    String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                    //sendPostRequest(urlPrefix + "authentication", authParams);
                    sendTrustedPostRequest(urlPrefix + "authentication", authParams);

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Interruption durant l'attente.");
                    }

                    //sendGetRequest(urlPrefix + "logout");
                    sendTrustedGetRequest(urlPrefix + "logout");

                    response = "Task terminated!";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } catch (Exception e) {
                    // Gérer ou journaliser l'exception
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String sendTrustedGetRequest(String urlString) throws Exception {
        // Créer un gestionnaire de confiance qui ne valide pas les chaînes de certificats
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        // Créer une URLConnection avec le SSLContext
        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sc.getSocketFactory());

        // Reste du code...
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response.toString());
                return response.toString();
            }
        } else {
            System.out.println("GET request not worked");
            return "GET request not worked. Response Code: " + responseCode;
        }
    }

    private String sendTrustedPostRequest(String urlString, String postParams) throws Exception {
        // Créer un gestionnaire de confiance qui ne valide pas les chaînes de certificats
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Installer le gestionnaire de confiance tout-en-accordant
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        // Créer une URLConnection avec le SSLContext
        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sc.getSocketFactory());

        // Configurer la requête POST
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(postParams);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response.toString());
                return response.toString();
            }
        } else {
            System.out.println("POST request not worked");
            return "POST request not worked. Response Code: " + responseCode;
        }
    }
}

