package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import online.caltuli.batch.userInteractionSimulation.config.network.ClientConfig;
import online.caltuli.batch.userInteractionSimulation.config.network.NetworkConfig;
import online.caltuli.batch.userInteractionSimulation.config.virtualUsers.VirtualUserInformationConfig;
import online.caltuli.batch.userInteractionSimulation.interfaces.GameObserver;
import online.caltuli.business.ai.DecisionEngine;
import online.caltuli.business.ai.MinMaxDecisionEngine;
import online.caltuli.model.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualUser implements HttpHandler, GameObserver {
    protected UserActionManager userActionManager;
    protected DecisionEngine decisionEngine;
    ExecutorService executor = Executors.newFixedThreadPool(1);
    protected GameState gameState;
    protected static final Logger logger = LogManager.getLogger(AiUserV2.class);

    public VirtualUser(
            VirtualUserInformationConfig virtualUserInformationConfig,
            NetworkConfig httpsNetworkConfig,
            NetworkConfig wssNetworkConfig,
            ClientConfig clientConfig)
            throws
                NoSuchAlgorithmException,
                KeyManagementException,
                KeyStoreException {

        this.userActionManager =
            new UserActionManager(
                virtualUserInformationConfig,
                httpsNetworkConfig,
                wssNetworkConfig,
                clientConfig
            );
    }

    // to be overrided
    protected void doTask() throws Exception {
        // to be implemented
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //executor.execute(() -> {
            int responseCode = 200;
            String responseText = "Task Completed";
            try {
                doTask();
            } catch (Exception e) {
                responseCode = 500;
                responseText = "Task failed : " + e.getMessage();
            }
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(
                    responseCode,
                    responseText.getBytes().length
            );
            os.write(responseText.getBytes());
            exchange.close();
        //});
    }

    public void update(GameEvent gameEvent) {
        switch (gameEvent.getWhatToBeUpdated()) {
            case "colorsGrid":
                decisionEngine.updateWithMove(
                        ((ColorsGridUpdateDescription)
                                gameEvent
                                        .getDescription())
                                .getColumn()
                );
                logger.info("colorsGrid ; column:"+((ColorsGridUpdateDescription)
                        gameEvent
                                .getDescription())
                        .getColumn());
                break;
            case "gameState":
                this.gameState =
                        ((GameStateUpdateDescription)
                                gameEvent
                                        .getDescription())
                                .getGameState();
                logger.info("gameState:"+((GameStateUpdateDescription)
                        gameEvent
                                .getDescription())
                        .getGameState());
                break;
        }
    }
}
