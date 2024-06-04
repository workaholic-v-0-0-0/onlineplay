package online.caltuli.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EvolutiveGridParser {

    // column number y to number of the lowest line x so that (x,y) can be played
    @JsonProperty
    protected int[] nextLine;

    // the color of the player to who it is its turn to play
    @JsonProperty
    protected CellState nextColor;

    // map :
    // coordinates row              number of red coordinates
    // R where only red    ----->   in R
    // has played
    @JsonProperty
    protected HashMap<Coordinates[],Integer> redRowsToNbOfRedCoordinates;

    // map :
    // coordinates row                number of green coordinates
    // R where only green    ----->   in R
    // has played
    @JsonProperty
    protected HashMap<Coordinates[],Integer> greenRowsToNbOfGreenCoordinates;

    // set of Coordinates rows which contain at least one red Coordinates
    // and one green Coordinates
    @JsonProperty
    protected HashSet<Coordinates[]> unWinnableCoordinatesRowsSet;

    protected Logger logger = LogManager.getLogger(EvolutiveGridParser.class); // to debug

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

    public Coordinates updateWithMove(int column) {

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

    public boolean isLegalMove(int column) {
        return this.getNextLine()[column] != 6;
    }

    public boolean detectDraw() {
        return unWinnableCoordinatesRowsSet.size() == ConstantGridParser.NUMBER_OF_COORDINATES_ROWS;
    }

    public CellState detectWinningColor() {
        for (int nbOfRedCoordinates : redRowsToNbOfRedCoordinates.values()) {
            if (nbOfRedCoordinates == 4) return CellState.RED;
        }
        for (int nbOfGreenCoordinates : greenRowsToNbOfGreenCoordinates.values()) {
            if (nbOfGreenCoordinates == 4) return CellState.GREEN;
        }
        return CellState.NULL;
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

    public HashSet<Coordinates[]> getUnWinnableCoordinatesRowsSet() {
        return unWinnableCoordinatesRowsSet;
    }

    public void setUnWinnableCoordinatesRowsSet(HashSet<Coordinates[]> unWinnableCoordinatesRowsSet) {
        this.unWinnableCoordinatesRowsSet = unWinnableCoordinatesRowsSet;
    }
}
