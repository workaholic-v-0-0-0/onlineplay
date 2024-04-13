package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
    The BatchServer class sets up a basic HTTP server intended for batch processing
    and simulation tasks within a web application context. It initializes an HTTPServer
    instance that listens on port 8000, configuring it with specific contexts for different
    simulation tasks. This server is designed to facilitate testing and automation by
    providing endpoints for triggering predefined actions, such as user behavior simulation
    through the DummyUser_01 handler and basic server responsiveness checks. It serves as a
        versatile tool for developers to interact with and test web applications in a controlled
    environment.
*/
public class BatchServer {

    private final Logger logger = LogManager.getLogger(DummyUser_01.class);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(final HttpExchange exchange) {
                // Crée un nouveau thread pour traiter chaque requête
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = "Server active!";
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } catch (IOException e) {
                            e.printStackTrace(); // Gérer l'exception comme il se doit
                        } finally {
                            exchange.close();
                        }
                    }
                }).start(); // N'oubliez pas de démarrer le thread
            }
        });

        server.createContext(
                "/dummyUser_01",
                new DummyUser_01(
                        "https://caltuli.online/webapp_version_sylvain/",
                        "fake-user"
                )
        );

        server.createContext(
                "/dummyUser_02",
                new DummyUser_01(
                        "https://caltuli.online/webapp_version_sylvain/",
                        "fake-user-02"
                )
        );

        server.createContext(
                "/dummyUser_03",
                new DummyUser_01(
                        "https://caltuli.online/webapp_version_sylvain/",
                        "fake-user-03"
                )
        );

        server.createContext(
                "/dummyUser_local",
                new DummyUser_01(
                        "https://localhost:8443/webapp/",
                        "fake-local-user"
                )
        );

        server.setExecutor(null);
        server.start();
    }

}


/*
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Configure le serveur pour utiliser un pool de threads pour les requêtes entrantes
        server.setExecutor(Executors.newCachedThreadPool()); // Utilise un pool de threads dynamique


            Root context configuration. This handler responds to requests at the root ("/")
            with a message indicating that the server is active. Useful for quickly verifying
            that the server is operational and listening on the designated port.

        server.createContext("/", new HttpHandler() {
@Override
public void handle(HttpExchange exchange) throws IOException {
        String response = "Server active! Use specific urls to trigger tasks.";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes());
        }
        }
        });


        Context configuration for simulating a mock user. This handler utilizes the
        DummyUser_01 class to simulate specific user actions, such as registration and
        authentication, on a specific version of the web application. This allows for
        the automatic testing of certain application features.

        server.createContext(
        "/dummyUser_01",
        new DummyUser_01(
        "https://caltuli.online/webapp_version_sylvain/",
        "fake-user"
        )
        );

        // tests
        server.createContext(
        "/dummyUser_02",
        new DummyUser_01(
        "https://caltuli.online/webapp_version_damien/",
        "fake-user"
        )
        );
        server.createContext(
        "/dummyUser_03",
        new DummyUser_01(
        "https://caltuli.online/webapp_version_samya/",
        "fake-user"
        )
        );
        server.createContext(
        "/dummyUser_007",
        new DummyUser_01(
        "https://caltuli.online/webapp_version_sylvain/",
        "fake-user-007"
        )
        );

        server.start();
        System.out.println("Server is listening on port 8000. Access via http://localhost:8000/");
        }
 */