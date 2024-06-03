package online.caltuli.batch.userInteractionSimulation;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.AiUser;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.DummyUser;
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
                "/dummyUser_local",
                new DummyUser(
                        "localhost:8443/webapp/",
                        "fake-local-user",
                        true
                )
        );

        server.createContext(
                "/dummyUser_distant",
                new DummyUser(
                        "caltuli.online/webapp_version_sylvain/",
                        "fake-distant-user",
                        false
                )
        );

        server.createContext(
                "/aiUser_local",
                new AiUser(
                        "localhost:8443/webapp/",
                        "ai-local-user",
                        true
                )
        );

        server.setExecutor(null);
        server.start();
    }
}