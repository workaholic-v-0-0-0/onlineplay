package online.caltuli.batch.userInteractionSimulation;

import online.caltuli.batch.userInteractionSimulation.config.network.ClientConfig;
import online.caltuli.batch.userInteractionSimulation.config.network.NetworkConfig;
import online.caltuli.batch.userInteractionSimulation.config.virtualUsers.VirtualUserInformationConfig;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.AiUser;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

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
        HttpServer server = null;
        AiUser aiUserLocalV2;
        AiUser aiUserDistantV2;
        AiUser aiUserDistantV2Sylvain;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            logger.info("Server started on port 8000");
            aiUserLocalV2 = new AiUser(
                VirtualUserInformationConfig.AI_USER,
                NetworkConfig.LOCAL_HTTPS,
                NetworkConfig.LOCAL_WSS,
                ClientConfig.TRUST
            );
            aiUserDistantV2 = new AiUser(
                VirtualUserInformationConfig.AI_USER,
                NetworkConfig.DROPLET_HTTPS,
                NetworkConfig.DROPLET_WSS,
                ClientConfig.TRUST
            );
            aiUserDistantV2Sylvain = new AiUser(
                VirtualUserInformationConfig.AI_USER,
                NetworkConfig.DROPLET_HTTPS_VERSION_SYLVAIN,
                NetworkConfig.DROPLET_WSS_VERSION_SYLVAIN,
                ClientConfig.TRUST
            );
            server.createContext(
                    "/aiUser-v2",
                    aiUserLocalV2
            );
            server.createContext(
                    "/aiUser-v2-distant",
                    aiUserDistantV2
            );
            server.createContext(
                    "/aiUser-v2-distant-sylvain",
                    aiUserDistantV2Sylvain
            );
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("Failed to initialize AiUser", e);
        }

        server.setExecutor(null);
        server.start();
    }
}