package online.caltuli.business;

import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.util.HashMap;
import java.util.HashSet;

public class EvolutiveGridParser {

    // column number y to number of the lowest line x so that (x,y) can be played
    private int[] nextLine;

    // the color of the player to who it is its turn to play
    CellState nextColor;

    // map :
    // coordinates row              number of red coordinates
    // R where only red    ----->   in R
    // has played
    private HashMap<Coordinates[],Integer> redRowsToNbOfRedCoordinates;

    // map :
    // coordinates row                number of green coordinates
    // R where only green    ----->   in R
    // has played
    private HashMap<Coordinates[],Integer> greenRowsToNbOfGreenCoordinates;

    // set of Coordinates rows which contain at least one red Coordinates
    // and one green Coordinates
    private HashSet<Coordinates[]> unWinnableCoordinatesRowsSet;

    public EvolutiveGridParser() {
        nextLine = new int[7];
        for (int j = 0 ; j < 7 ; j++) {
            nextLine[j] = 0;
        }
        nextColor = CellState.RED;
        redRowsToNbOfRedCoordinates = new HashMap<Coordinates[], Integer>();
        greenRowsToNbOfGreenCoordinates = new HashMap<Coordinates[], Integer>();
        unWinnableCoordinatesRowsSet = new HashSet<Coordinates[]>();
    }

    public EvolutiveGridParser(
            int[] nextLine,
            CellState nextColor,
            HashMap<Coordinates[],Integer> redRowsToNbOfRedCoordinates,
            HashMap<Coordinates[],Integer> greenRowsToNbOfGreenCoordinates,
            HashSet<Coordinates[]> unWinnableCoordinatesRowsSet
    ) {
        this.nextLine = nextLine;
        this.nextColor = nextColor;
        this.redRowsToNbOfRedCoordinates = redRowsToNbOfRedCoordinates;
        this.greenRowsToNbOfGreenCoordinates = greenRowsToNbOfGreenCoordinates;
        this.unWinnableCoordinatesRowsSet = unWinnableCoordinatesRowsSet;
    }

    public void updateWithMove(int column) {

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
            notPlayedColorRowsToNbOfNotPlayedColorCoordinates = greenRowsToNbOfGreenCoordinates;
            nextColor = CellState.RED;
        }

        for (Coordinates[] coordinatesRow :
                ConstantGridParser
                        .arrayOfCoordinatesRowsContainingCoordinates
                                [nextLine[column]]
                                [column]
        ) {
            if (!unWinnableCoordinatesRowsSet.contains(coordinatesRow)) {
                if ((notPlayedColorRowsToNbOfNotPlayedColorCoordinates.get(coordinatesRow) != null)) {
                    notPlayedColorRowsToNbOfNotPlayedColorCoordinates.remove(coordinatesRow);
                    unWinnableCoordinatesRowsSet.add(coordinatesRow);
                } else {
                    Integer newNbOfPlayedColorCoordinatesInCoordinatesRow =
                            playedColorRowsToNbOfPlayedColorCoordinates.getOrDefault(
                                    coordinatesPlayed,
                                    0
                            );
                    playedColorRowsToNbOfPlayedColorCoordinates.put(
                            coordinatesRow,
                            newNbOfPlayedColorCoordinatesInCoordinatesRow
                    );
                }
            }
        }

        nextLine[column]++;
    }

    public int[] getNextLine() {
        return nextLine;
    }

    public void setNextLine(int[] nextLine) {
        this.nextLine = nextLine;
    }

    public CellState getNextColor() {
        return nextColor;
    }

    public void setNextColor(CellState nextColor) {
        this.nextColor = nextColor;
    }

    public HashMap<Coordinates[], Integer> getRedRowsToNbOfRedCoordinates() {
        return redRowsToNbOfRedCoordinates;
    }

    public void setRedRowsToNbOfRedCoordinates(HashMap<Coordinates[], Integer> redRowsToNbOfRedCoordinates) {
        this.redRowsToNbOfRedCoordinates = redRowsToNbOfRedCoordinates;
    }

    public HashMap<Coordinates[], Integer> getGreenRowsToNbOfGreenCoordinates() {
        return greenRowsToNbOfGreenCoordinates;
    }

    public void setGreenRowsToNbOfGreenCoordinates(HashMap<Coordinates[], Integer> greenRowsToNbOfGreenCoordinates) {
        this.greenRowsToNbOfGreenCoordinates = greenRowsToNbOfGreenCoordinates;
    }
}
