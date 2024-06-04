package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.UpdateDescription;
import online.caltuli.business.ai.Column;

public class ColorsGridUpdateDescription implements UpdateDescription {
    private Column column;

    public ColorsGridUpdateDescription(Column column) {
        this.column = column;
    }

    public Column getColumn() {
        return column;
    }
}
