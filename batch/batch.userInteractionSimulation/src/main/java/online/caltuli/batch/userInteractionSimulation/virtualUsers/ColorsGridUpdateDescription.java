package online.caltuli.batch.userInteractionSimulation.virtualUsers;

import online.caltuli.batch.userInteractionSimulation.interfaces.UpdateDescription;
import online.caltuli.model.Column;

public class ColorsGridUpdateDescription implements UpdateDescription {
    private Column column;

    public ColorsGridUpdateDescription(Column column) {
        this.column = column;
    }

    public Column getColumn() {
        return column;
    }
}
