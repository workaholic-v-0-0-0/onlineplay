package online.caltuli.batch.userInteractionSimulation.interfaces;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.GameEvent;

public interface GameObserver {
    void update(GameEvent gameEvent);
}