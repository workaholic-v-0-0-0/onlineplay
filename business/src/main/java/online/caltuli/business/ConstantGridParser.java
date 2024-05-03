package online.caltuli.business;

import online.caltuli.model.BidimensionalParametrizationOfSetOfCoordinatesFactory;
import online.caltuli.model.Coordinates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstantGridParser {

    private static final Logger logger = LogManager.getLogger(ConstantGridParser.class);

    // Coordinates parametrization
    // to optimally fetch a Coordinates instance from its coordinates
    // usage : Coordinates coordinates = CP.get(3,4);
    private static final BidimensionalParametrizationOfSetOfCoordinatesFactory CP;

    // Coordinates array
    // to optimally traverse all the Coordinates instances
    // usage : Coordinates coordinates = CA[3,4];
    public static final Coordinates[][] CA;

    public static final Coordinates[][][][]
            arrayOfCoordinatesRowsStartingFromBottomWithCoordinates;

    // an array which corresponds to a map :
    // coordinates |----> array of Coordinates rows which contain coordinates
    // think ArrayList<Coordinates[]>[][] but it is Coordinates[][][][] to optimize
    // usage :
    //      Coordinates[][][][] param =
    //          ConstantGridParser.arrayOfCoordinatesRowsContainingCoordinates;
    //      /*
    //      param[0][0][1][3] is the last Coordinates instance of the second Coordinates
    //      rows of the array of Coordinates rows which contain the Coordinates instance
    //      at the bottom left of the grid (i.e. at (0,0))
    //       */
    public static final Coordinates[][][][]
            arrayOfCoordinatesRowsContainingCoordinates;

    public static final int NUMBER_OF_COORDINATES_ROWS = 69;

    static {
        CP =
                BidimensionalParametrizationOfSetOfCoordinatesFactory
                        .getInstance();
        CA =
                CP
                        .getCoordinatesGrid();

        /*
         * Initialize the two bi-dimensional arrays of Coordinates rows
         */
        arrayOfCoordinatesRowsContainingCoordinates = new Coordinates[6][7][][];
        arrayOfCoordinatesRowsStartingFromBottomWithCoordinates = new Coordinates[6][7][][];

        int line, column, coordinatesRowIndex;
        int[][] containingCoordinatesCounter  = new int[6][7];
        int[][] startingWithCoordinatesCounter = new int[6][7];
        Coordinates coordinates;

        // initialize the "variables lengths" of the two nested arrays
        for (line = 0; line < 6; line++) {
            for (column = 0; column < 7; column++) {
                //int nbOfCoordinatesRows = nbOfCoordinatesRowsContainingCoordinates(line, column);
                arrayOfCoordinatesRowsContainingCoordinates[line][column]
                        = new Coordinates
                            [nbOfCoordinatesRowsContainingCoordinates(line, column)]
                            [4];
                arrayOfCoordinatesRowsStartingFromBottomWithCoordinates[line][column]
                        = new Coordinates
                        [nbOfCoordinatesRowsStartingFromBottomWithCoordinates(line, column)]
                        [4];
                containingCoordinatesCounter[line][column] = 0;
                startingWithCoordinatesCounter[line][column] = 0;
            }
        }

        // horizontal Coordinates rows addition
        for (line = 0 ; line <= 5 ; line++) {
            for (column = 0 ; column <= 3 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line,column + coordinatesRowIndex);
                    arrayOfCoordinatesRowsContainingCoordinates
                            [line]
                            [column]
                            [containingCoordinatesCounter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                }
                Coordinates[] coordinatesRow =
                        arrayOfCoordinatesRowsContainingCoordinates
                        [line]
                        [column]
                        [containingCoordinatesCounter[line][column]];
                containingCoordinatesCounter[line][column]++;
                arrayOfCoordinatesRowsStartingFromBottomWithCoordinates
                        [line]
                        [column]
                        [startingWithCoordinatesCounter[line][column]]
                        =
                        coordinatesRow;
                startingWithCoordinatesCounter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {
                        arrayOfCoordinatesRowsContainingCoordinates
                                [c.getX()]
                                [c.getY()]
                                [containingCoordinatesCounter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        containingCoordinatesCounter[c.getX()][c.getY()]++;
                    }
                }
            }
        }

        // vertical Coordinates rows addition
        for (line = 0 ; line <= 2 ; line++) {
            for (column = 0 ; column <= 6 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line + coordinatesRowIndex, column);
                    arrayOfCoordinatesRowsContainingCoordinates
                            [line]
                            [column]
                            [containingCoordinatesCounter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                }
                Coordinates[] coordinatesRow =
                        arrayOfCoordinatesRowsContainingCoordinates
                                [line]
                                [column]
                                [containingCoordinatesCounter[line][column]];
                containingCoordinatesCounter[line][column]++;
                arrayOfCoordinatesRowsStartingFromBottomWithCoordinates
                        [line]
                        [column]
                        [startingWithCoordinatesCounter[line][column]]
                        =
                        coordinatesRow;
                startingWithCoordinatesCounter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {
                        arrayOfCoordinatesRowsContainingCoordinates
                                [c.getX()]
                                [c.getY()]
                                [containingCoordinatesCounter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        containingCoordinatesCounter[c.getX()][c.getY()]++;
                    }
                }
            }
        }

        // diagonally ascending Coordinates rows addition
        for (line = 0 ; line <= 2 ; line++) {
            for (column = 0 ; column <= 3 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line + coordinatesRowIndex, column + coordinatesRowIndex);
                    arrayOfCoordinatesRowsContainingCoordinates
                            [line]
                            [column]
                            [containingCoordinatesCounter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                }
                Coordinates[] coordinatesRow =
                        arrayOfCoordinatesRowsContainingCoordinates
                                [line]
                                [column]
                                [containingCoordinatesCounter[line][column]];
                containingCoordinatesCounter[line][column]++;
                arrayOfCoordinatesRowsStartingFromBottomWithCoordinates
                        [line]
                        [column]
                        [startingWithCoordinatesCounter[line][column]]
                        =
                        coordinatesRow;
                startingWithCoordinatesCounter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {

                        arrayOfCoordinatesRowsContainingCoordinates
                                [c.getX()]
                                [c.getY()]
                                [containingCoordinatesCounter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        containingCoordinatesCounter[c.getX()][c.getY()]++;
                    }
                }
            }
        }

        // diagonally descending Coordinates rows addition
        for (line = 0 ; line <= 2 ; line++) {
            for (column = 3 ; column <= 6 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line + coordinatesRowIndex, column - coordinatesRowIndex);
                    arrayOfCoordinatesRowsContainingCoordinates
                            [line]
                            [column]
                            [containingCoordinatesCounter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                }
                Coordinates[] coordinatesRow =
                        arrayOfCoordinatesRowsContainingCoordinates
                                [line]
                                [column]
                                [containingCoordinatesCounter[line][column]];
                containingCoordinatesCounter[line][column]++;
                arrayOfCoordinatesRowsStartingFromBottomWithCoordinates
                        [line]
                        [column]
                        [startingWithCoordinatesCounter[line][column]]
                        =
                        coordinatesRow;
                startingWithCoordinatesCounter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {

                        arrayOfCoordinatesRowsContainingCoordinates
                                [c.getX()]
                                [c.getY()]
                                [containingCoordinatesCounter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        containingCoordinatesCounter[c.getX()][c.getY()]++;
                    }
                }
            }
        }
    }

    private static int nbOfCoordinatesRowsContainingCoordinates(int line, int column) {
        return
                // nb of horizontal Coordinates rows passing through (line, column)
                (Math.min(3, column) - Math.max(0, column - 3) + 1)
                +
                // nb of vertical Coordinates rows passing through (line, column)
                (Math.min(3, line) - Math.max(0, line - 2) + 1)
                +
                // nb of diagonally ascending Coordinates rows passing through (line, column)
                (((column - 3 <= line) && (line <= column + 2)) ?
                    Math.min(3,Math.min(line, column)) - Math.max(0,Math.max(line - 2, column - 3)) + 1
                    :
                    0)
                +
                // nb of diagonally descending Coordinates rows passing through (line, column)
                (((3 - column <= line) && (line <= 8 - column)) ?
                    Math.min(3, Math.min(line, 6 - column)) - Math.max(0, Math.max(line - 2, 3 - column)) + 1
                    :
                    0);
    }

    private static int nbOfCoordinatesRowsStartingFromBottomWithCoordinates(int line, int column) {
        return
                // add 1 if it starts a horizontal Coordinates row
                ((column <= 3) ? 1 : 0)
                +
                // add 1 if it starts a vertical Coordinates row
                ((line <= 2) ? 1 : 0)
                +
                // add 1 if it starts a diagonally ascending Coordinates row
                (((line <= 2) && (column <= 3)) ? 1 : 0)
                +
                // add 1 if it starts a diagonally descending Coordinates row
                (((line <= 2) && (column >= 3)) ? 1 : 0)
                ;
    }
}
