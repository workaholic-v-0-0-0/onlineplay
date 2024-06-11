package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.interfaces.UpdateDescription;

public class GameEvent {
    private final String whatToBeUpdated;
    private final UpdateDescription description;

    public GameEvent(
            String whatToBeUpdated,
            UpdateDescription description) {
        this.whatToBeUpdated = whatToBeUpdated;
        this.description = description;
    }

    public String getWhatToBeUpdated() {
        return whatToBeUpdated;
    }

    public UpdateDescription getDescription() {
        return description;
    }
}
