package online.caltuli.model;

public class Cell {
    private Coordinates coordinates;
    private CellState cellState;

    public Cell(Coordinates coordinates, CellState... cellState) {

        this.coordinates = coordinates;
        if (cellState.length == 0)
            this.cellState = CellState.NULL;
        else
            this.cellState = cellState[0];
    }
}
