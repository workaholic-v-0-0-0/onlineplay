package online.caltuli.model;

// a param to optimize Coordinates fetching
public class BidimensionalParametrizationOfSetOfCoordinatesFactory {
    protected static final int MAX_X = 6;
    protected static final int MAX_Y = 7;
    private static BidimensionalParametrizationOfSetOfCoordinatesFactory instance;
    private final Coordinates[][] coordinatesGrid = new Coordinates[MAX_X][MAX_Y];

    private BidimensionalParametrizationOfSetOfCoordinatesFactory() {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                coordinatesGrid[x][y] = new Coordinates(x, y);
            }
        }
    }

    public static BidimensionalParametrizationOfSetOfCoordinatesFactory getInstance() {
        if (instance == null) {
            instance = new BidimensionalParametrizationOfSetOfCoordinatesFactory();
        }
        return instance;
    }

    public Coordinates get(int x, int y) {
        if (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y) {
            return coordinatesGrid[x][y];
        } else {
            throw new IllegalArgumentException("Out of bounds");
        }
    }

    public Coordinates[][] getCoordinatesGrid() {
        return coordinatesGrid;
    }
}
