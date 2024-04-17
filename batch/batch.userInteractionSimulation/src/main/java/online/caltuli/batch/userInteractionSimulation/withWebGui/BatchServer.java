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
                new DummyUserWithTrust_01(
                        "https://localhost:8443/webapp/",
                        "fake-local-user"
                )
        );

        server.createContext(
                "/dummyUser_local_02",
                new DummyUserWithTrust_01(
                        "https://localhost:8443/webapp/",
                        "fake-local-user-02"
                )
        );

        server.createContext(
                "/dummyUser_local_03",
                new DummyUserWithTrust_01(
                        "https://localhost:8443/webapp/",
                        "fake-local-user-03"
                )
        );

        server.createContext(
                "/dummyUser_local_04",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-user-04"
                )
        );

        server.createContext(
                "/dummyUser_local_05",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-user-05"
                )
        );

        server.createContext(
                "/dummyUser_local_06",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-user-06"
                )
        );

        server.createContext(
                "/dummyUser_local_07",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-user-07"
                )
        );

        server.setExecutor(null);
        server.start();
    }
}