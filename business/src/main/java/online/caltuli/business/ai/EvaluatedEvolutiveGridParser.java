package online.caltuli.business.ai;

import online.caltuli.business.ConstantGridParser;
import online.caltuli.business.EvolutiveGridParser;
import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.util.HashMap;

public class EvaluatedEvolutiveGridParser extends EvolutiveGridParser {
    public static int ALTITUDE_WEIGHT = 1;
    public static long INFINITY = (long) Math.pow(10,15);
    public static long MINUS_INFINITY = (long) -Math.pow(10,15);
    public static HashMap<Coordinates[],Integer> coordinatesRowToWeight;
    private long evaluation;

    static {
        coordinatesRowToWeight = new HashMap<Coordinates[],Integer>();
        Coordinates[][][][] param =
            ConstantGridParser
                .arrayOfCoordinatesRowsStartingFromBottomWithCoordinates;
        for (int line = 0; line < 6; line++) {
            for (int column = 0; column < 7; column++) {
                for (Coordinates[] coordinatesRow :
                        param[line][column]
                ) {
                    coordinatesRowToWeight.put(
                        coordinatesRow,
                        Integer.valueOf(
                            EvaluatedEvolutiveGridParser.ALTITUDE_WEIGHT
                            *
                            (14 - coordinatesRow[0].getX() - coordinatesRow[3].getX())
                        )
                    );
                }
            }
        }
    }

    public EvaluatedEvolutiveGridParser() {
        super();
        this.evaluation = 0;
    }

    // masquage de updateWithMove(int) qui optimise l'évaluation
    // de la position en la calculant à partir de l'évaluation précédente
    // et du coup joué
    @Override
    public Coordinates updateWithMove(int column) {
        long evaluation = this.evaluation;
        int sign;
        Coordinates coordinatesPlayed = ConstantGridParser.CA[nextLine[column]][column];
        HashMap<Coordinates[],Integer>
                playedColorRowsToNbOfPlayedColorCoordinates,
                notPlayedColorRowsToNbOfNotPlayedColorCoordinates;

        if (nextColor == CellState.RED) {
            playedColorRowsToNbOfPlayedColorCoordinates = redRowsToNbOfRedCoordinates;
            notPlayedColorRowsToNbOfNotPlayedColorCoordinates = greenRowsToNbOfGreenCoordinates;
            nextColor = CellState.GREEN;
            sign = 1;
        } else {
            playedColorRowsToNbOfPlayedColorCoordinates = greenRowsToNbOfGreenCoordinates;
            notPlayedColorRowsToNbOfNotPlayedColorCoordinates = redRowsToNbOfRedCoordinates;
            nextColor = CellState.RED;
            sign = -1;
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
                    // evaluation update
                    this.evaluation +=
                        sign
                        *
                        EvaluatedEvolutiveGridParser
                                .coordinatesRowToWeight
                                .get(coordinatesRow)
                        *
                        notPlayedColorRowsToNbOfNotPlayedColorCoordinates
                                .get(coordinatesRow);
                    // remove the pair which corresponds to this coordinatesRow in "opponent dictionnary"
                    notPlayedColorRowsToNbOfNotPlayedColorCoordinates.remove(coordinatesRow);
                    // one can't win anymore in this coordinatesRow because it's bicolor
                    unWinnableCoordinatesRowsSet.add(coordinatesRow);
                } else {
                    playedColorRowsToNbOfPlayedColorCoordinates.merge(
                        coordinatesRow,
                        1,
                        //(existingValue, newValue) -> existingValue + newValue
                        (existingValue, newValue) -> existingValue + 1
                    );
                    // evaluation update
                    this.evaluation +=
                        sign
                        *
                        EvaluatedEvolutiveGridParser
                            .coordinatesRowToWeight
                            .get(coordinatesRow);
                }
            }

        }

        // extreme cases evaluation update
        if (this.detectDraw()) {
            this.evaluation = 0;
        }
        if ((this.detectWinningColor() == CellState.RED)) {
            this.evaluation =
                EvaluatedEvolutiveGridParser.INFINITY;
        }
        if ((this.detectWinningColor() == CellState.GREEN)) {
            this.evaluation =
                    EvaluatedEvolutiveGridParser.MINUS_INFINITY;
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
