package online.caltuli.business.ai;

import online.caltuli.business.EvolutiveGridParser;
import online.caltuli.model.Coordinates;

import java.util.Map;

public class DecisionEngine {
    private static long ALTITUDE_WEIGHT = 1;
    private Tree tree;

    public DecisionEngine(Tree tree) {
        this.tree = tree;
    }

    public void updateWithMove(int column) {
        // as it's called
    }

    public long evaluation(EvaluatedEvolutiveGridParser eegp) {
        long evaluation = 0;
        for (
                Map.Entry<Coordinates[], Integer> entry :
                eegp.getRedRowsToNbOfRedCoordinates().entrySet()) {
            evaluation += weight(entry.getKey()) * entry.getValue();
        }
        for (
                Map.Entry<Coordinates[], Integer> entry :
                eegp.getGreenRowsToNbOfGreenCoordinates().entrySet()) {
            evaluation -= weight(entry.getKey()) * entry.getValue();
        }
        return evaluation;
    }

    public int getBestMove() {
        return 0; // placeholder
    }

    // the lower the better
    private long weight(Coordinates[] coordinatesRow) {
        return
            DecisionEngine.ALTITUDE_WEIGHT
            *
            (14 - coordinatesRow[0].getX() - coordinatesRow[3].getX());
    }
}


// on peut évaluer la position en fonction de l'évaluation de la position
// précédente et du coup joué

// eval =