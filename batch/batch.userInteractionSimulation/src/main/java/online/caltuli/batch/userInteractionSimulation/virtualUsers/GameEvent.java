package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.UpdateDescription;

public class GameEvent {
    private String whatToBeUpdated;
    private UpdateDescription description;

    public GameEvent(
            String whatToBeUpdated,
            UpdateDescription description) {
        this.whatToBeUpdated = whatToBeUpdated;
        this.description = description;
    }

    public String getWhatToBeUpdated() {
        return whatToBeUpdated;
    }

    public void setWhatToBeUpdated(String whatToBeUpdated) {
        this.whatToBeUpdated = whatToBeUpdated;
    }

    public UpdateDescription getDescription() {
        return description;
    }

    public void setDescription(UpdateDescription description) {
        this.description = description;
    }
}
