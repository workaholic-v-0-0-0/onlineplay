package online.caltuli.model;

import java.util.HashSet;

// to optimize loop over the set of the 42 Coordinates instances
public class SetOfCoordinatesFactory {
    private static HashSet<Coordinates> instance;

    public static HashSet<Coordinates> getInstance() {
        if (instance == null) {
            int MAX_X = BidimensionalParametrizationOfSetOfCoordinatesFactory.MAX_X;
            int MAX_Y = BidimensionalParametrizationOfSetOfCoordinatesFactory.MAX_Y;
            BidimensionalParametrizationOfSetOfCoordinatesFactory param =
                    BidimensionalParametrizationOfSetOfCoordinatesFactory.getInstance();
            instance = new HashSet<Coordinates>();
            for (int x = 0; x < MAX_X; x++) {
                for (int y = 0; y < MAX_Y; y++) {
                    instance.add(param.get(x, y));
                }
            }
        }
        return instance;
    }
}
