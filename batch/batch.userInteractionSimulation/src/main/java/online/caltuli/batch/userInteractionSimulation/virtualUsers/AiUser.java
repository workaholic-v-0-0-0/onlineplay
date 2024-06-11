package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.config.network.ClientConfig;
import online.caltuli.batch.userInteractionSimulation.config.network.NetworkConfig;
import online.caltuli.batch.userInteractionSimulation.config.virtualUsers.VirtualUserInformationConfig;
import online.caltuli.business.ai.MinMaxDecisionEngine;

import online.caltuli.model.GameState;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class AiUser extends VirtualUser {

    public AiUser(
            VirtualUserInformationConfig virtualUserInformationConfig,
            NetworkConfig httpsNetworkConfig,
            NetworkConfig wssNetworkConfig,
            ClientConfig clientConfig)
            throws
                NoSuchAlgorithmException,
                KeyManagementException,
                KeyStoreException {
        super(
            virtualUserInformationConfig,
            httpsNetworkConfig,
            wssNetworkConfig,
            clientConfig
        );
        this.decisionEngine = new MinMaxDecisionEngine();
    }

    @Override
    protected void doTask() throws Exception {
        while (true) {
            userActionManager.register();
            Thread.sleep(3000);
            userActionManager.authenticate();
            Thread.sleep(3000);
            userActionManager.proposeNewGame();
            userActionManager.makeMeObserveAndPlayGame(this);
            userActionManager.waitOpponent();
            Thread.sleep(5000);
            do {
                if (this.gameState == GameState.WAIT_FIRST_PLAYER_MOVE) {
                    userActionManager.playMove(this.decisionEngine.getBestMove());
                }
                Thread.sleep(5000);
            } while (
                (gameState == GameState.WAIT_FIRST_PLAYER_MOVE)
                ||
                (gameState == GameState.WAIT_SECOND_PLAYER_MOVE)
            );
            Thread.sleep(1000);
            this.userActionManager.logout();
            this.userActionManager.cleanupClients();
            this.decisionEngine = new MinMaxDecisionEngine();
        }
    }
}
