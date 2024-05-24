package online.caltuli.batch.userInteractionSimulation.withWebGui;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

    private static final Logger logger = LogManager.getLogger(BatchServer.class);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(final HttpExchange exchange) {
                Logger logger = LogManager.getLogger(BatchServer.class);
                logger.info("here 1");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("here 2");
                        try {
                            logger.info("here 3");
                            String response = "Server active!";
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger.info("e.printStackTrace()");
                        } finally {
                            exchange.close();
                        }
                    }
                }).start();
            }
        });

        server.createContext(
                "/dummyUser_01",
                new DummyUser_01(
                        "https://caltuli.online/webapp_version_sylvain/",
                        "fake-user-01"
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
                "/dummyUser_local_01",
                new DummyUserWithTrust_01(
                        "https://localhost:8443/webapp/",
                        //"https://caltuli.online/webapp_version_sylvain/",
                        "fake-local-user-01"
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
                "/dummyPlayer_local_01",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-player-01"
                )
        );

        server.createContext(
                "/dummyPlayer_local_02",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-player-02"
                )
        );

        server.createContext(
                "/dummyPlayer_local_03",
                new DummyUserWithTrust_02(
                        "https://localhost:8443/webapp/",
                        "fake-local-player-03"
                )
        );

        server.setExecutor(null);
        server.start();
    }
}