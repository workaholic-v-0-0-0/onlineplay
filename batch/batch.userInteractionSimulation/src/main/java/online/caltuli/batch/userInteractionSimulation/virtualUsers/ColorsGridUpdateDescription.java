package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.UpdateDescription;

public class ColorsGridUpdateDescription implements UpdateDescription {
    private int column;

    public ColorsGridUpdateDescription(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
