package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;

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

                    sendGetRequest(urlPrefix + "home");
                    sendGetRequest(urlPrefix + "registration");
                    String registrationParams = "username=" + URLEncoder.encode(user, "UTF-8") +
                            "&password=123&email=ai.ai@ai.com&message=I'm clever";
                    sendPostRequest(urlPrefix + "registration", registrationParams);

                    String authParams = "username=" + URLEncoder.encode(user, "UTF-8") + "&password=123";
                    sendPostRequest(urlPrefix + "authentication", authParams);

                    logger.info("wait?");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Interruption durant l'attente.");
                    }
                    logger.info("wait?");

                    sendGetRequest(urlPrefix + "logout");

                    String response = "Task terminated!";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } catch (Exception e) {
                    // Gérer ou journaliser l'exception
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendGetRequest(String urlString) throws IOException {
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
            }
        } else {
            System.out.println("GET request not worked");
        }
    }

    private void sendPostRequest(String urlString, String postParams) throws IOException {
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
            }
        } else {
            System.out.println("POST request not worked");
        }
    }
}

