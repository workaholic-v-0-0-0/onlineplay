package online.caltuli.business.ai;

import online.caltuli.business.ConstantGridParser;
import online.caltuli.business.EvolutiveGridParser;
import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.util.HashMap;

public class EvaluatedEvolutiveGridParser extends EvolutiveGridParser {
    private long evaluation;

    public EvaluatedEvolutiveGridParser() {
        super();
    }

    // masquage de updateWithMove(int) qui optimise l'évaluation
    // de la position en la calculant à partir de l'évaluation précédente
    // et du coup joué
    @Override
    public Coordinates updateWithMove(int column) {
        long evaluation = this.evaluation;
        Coordinates coordinatesPlayed = ConstantGridParser.CA[nextLine[column]][column];
        HashMap<Coordinates[],Integer>
                playedColorRowsToNbOfPlayedColorCoordinates,
                notPlayedColorRowsToNbOfNotPlayedColorCoordinates;

        if (nextColor == CellState.RED) {
            playedColorRowsToNbOfPlayedColorCoordinates = redRowsToNbOfRedCoordinates;
            notPlayedColorRowsToNbOfNotPlayedColorCoordinates = greenRowsToNbOfGreenCoordinates;
            nextColor = CellState.GREEN;
        } else {
            playedColorRowsToNbOfPlayedColorCoordinates = greenRowsToNbOfGreenCoordinates;
            notPlayedColorRowsToNbOfNotPlayedColorCoordinates = redRowsToNbOfRedCoordinates;
            nextColor = CellState.RED;
        }

        for (Coordinates[] coordinatesRow :
                ConstantGridParser
                        .arrayOfCoordinatesRowsContainingCoordinates
                        [nextLine[column]]
                        [column]
        ) {
            if (!unWinnableCoordinatesRowsSet.contains(coordinatesRow)) {
                // if opponent's already played in this coordinatesRow
                if ((notPlayedColorRowsToNbOfNotPlayedColorCoordinates.get(coordinatesRow) != null)) {
                    // remove the pair which corresponds to this coordinatesRow in "opponent dictionnary"
                    notPlayedColorRowsToNbOfNotPlayedColorCoordinates.remove(coordinatesRow);
                    playedColorRowsToNbOfPlayedColorCoordinates.remove(coordinatesRow);
                    // one can't win anymore in this coordinatesRow because it's bicolor
                    unWinnableCoordinatesRowsSet.add(coordinatesRow);
                } else {
                    playedColorRowsToNbOfPlayedColorCoordinates.merge(
                            coordinatesRow,
                            1,
                            (existingValue, newValue) -> existingValue + newValue
                    );
                }
            }
        }

        nextLine[column]++;

        return coordinatesPlayed;
    }

    public long getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(long evaluation) {
        this.evaluation = evaluation;
    }
}
