package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.UpdateDescription;
import online.caltuli.model.GameState;

public class GameStateUpdateDescription implements UpdateDescription {
    private GameState gameState;

    public GameStateUpdateDescription(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
