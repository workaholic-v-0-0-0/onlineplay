package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
The DummyUser_01 class is a straightforward HTTP client designed for testing
purposes. It simulates the following user actions on the web application by
making HTTP GET and POST requests:
- Sends GET requests to access the home and registration pages.
- Submits a POST request for registration using URL-encoded parameters, including a username, password, and other formal information.
- Performs authentication via a POST request with the username and password.
- Pauses execution for a while to simulate waiting or manual user interaction.
- Initiates a GET request to log out of the application.
*/
public class DummyUser_01 implements HttpHandler {

    private final Logger logger = LogManager.getLogger(DummyUser_01.class);

    private static final String DEFAULT_URL_PREFIX = "https://localhost:8443/webapp/";
    private static final String ALTERNATE_URL_PREFIX = "https://caltuli.online/webapp/";
    private static final String ALTERNATE_URL_PREFIX_02 = "https://caltuli.online/webapp_version_damien/";
    private static final String ALTERNATE_URL_PREFIX_03 = "https://caltuli.online/webapp_version_samya/";
    private static final String ALTERNATE_URL_PREFIX_04 = "https://caltuli.online/webapp_version_sylvain/";
    private static final String USER_AGENT = "Mozilla/5.0";

    private String urlPrefix;
    String user;

    DummyUser_01() {
        String urlPrefix = DEFAULT_URL_PREFIX;
        String user = "fake-user";
    }

    DummyUser_01(String urlPrefix, String user) {
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

    private String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
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

        // Installer le gestionnaire de confiance tout-en-accordant
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


    private String sendPostRequest(String urlString, String postParams) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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

