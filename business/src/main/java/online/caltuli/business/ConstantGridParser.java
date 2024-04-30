package online.caltuli.business;

import online.caltuli.consumer.api.abuseipdb.IpValidator;
import online.caltuli.model.BidimensionalParametrizationOfSetOfCoordinatesFactory;
import online.caltuli.model.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstantGridParser {

    private static final Logger logger = LogManager.getLogger(ConstantGridParser.class);

    // Coordinates parametrization
    // to optimally fetch a Coordinates instance from its coordinates
    // usage : Coordinates coordinates = C.get(3,4);
    private static final BidimensionalParametrizationOfSetOfCoordinatesFactory CP;

    // Coordinates array
    // to optimally traverse all the Coordinates instances
    // usage : Coordinates coordinates = C.get(3,4);
    private static final Coordinates[][] CT;

    // set of Coordinates rows
    private static final HashSet<Coordinates[]> setOfCoordinatesRows = null;

    // to optimize
    // map : coordinates |----> array of Coordinates rows which contain coordinates
    // think ArrayList<Coordinates[]>[][]
    // usage :
    //      Coordinates[][][][] param =
    //          ConstantGridParser.bidimensionalParametrizationOfArrayOfCoordinatesRows;
    //      /*
    //      param[0][0][3][1] is the last Coordinates instance of the second Coordinates
    //      rows of the array of Coordinates rows which contain the Coordinates instance
    //      at the bottom left of the grid (i.e. at (0,0))
    //       */
    public static final Coordinates[][][][]
            bidimensionalParametrizationOfArrayOfCoordinatesRows;

    // map : Coordinates row |----> its altitude
    // useless ? Actually altitude of the Coordinates[] is the first component
    // of the first element of the Coordinates[]
    // private static final HashMap<Coordinates[], Integer> coordinatesRowToAltitude = null;

    static {
        CP =
                BidimensionalParametrizationOfSetOfCoordinatesFactory
                        .getInstance();
        CT =
                CP
                        .getCoordinatesGrid();
        /*
         *Initialize a 4D array to represent groups of four aligned positions in a
         * Connect Four grid.
         * Each element tab[i][j] is configured based on its position in the grid (i,j).
         * It dynamically sets the third dimension size (nbOfCoordinatesRows),
         * representing Coordinates rows passing through the cell.
         */
        bidimensionalParametrizationOfArrayOfCoordinatesRows = new Coordinates[6][7][][];

        int line, column, coordinatesRowIndex;
        int[][] counter = new int[6][7];
        Coordinates coordinates;

        for (line = 0; line < 6; line++) {
            for (column = 0; column < 7; column++) {
                int nbOfCoordinatesRows = nbOfCoordinatesRowsContainingCoordinates(line, column);
                bidimensionalParametrizationOfArrayOfCoordinatesRows[line][column]
                        = new Coordinates[nbOfCoordinatesRows][4];
                counter[line][column] = 0;
                logger.info("init length loop ; line = " + line + " ; column = " + column);
            }
        }

        // horizontal Coordinates rows addition
        for (line = 0 ; line <= 5 ; line++) {
            for (column = 0 ; column <= 3 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line,column + coordinatesRowIndex);
                    bidimensionalParametrizationOfArrayOfCoordinatesRows
                            [line]
                            [column]
                            [counter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                    logger.info("horizontal add loop ; line = " + line + " ; column = " + column + " ; coordinatesRowIndex = " + coordinatesRowIndex);
                }
                Coordinates[] coordinatesRow =
                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                        [line]
                        [column]
                        [counter[line][column]];
                logger.info("param[" + line + "," + column + "][" + counter[line][column] + "]=[" + coordinatesRow
                        + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                        + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                        + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                        + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                ;
                counter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {
                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [c.getX()]
                                [c.getY()]
                                [counter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        logger.info("param[" + c.getX() + "," + c.getY() + "][" + counter[c.getX()][c.getY()] + "]=[" + coordinatesRow
                                + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                                + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                                + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                                + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                        ;
                        counter[c.getX()][c.getY()]++;
                    }
                }
            }
        }

        // vertical Coordinates rows addition
        for (line = 0 ; line <= 2 ; line++) {
            for (column = 0 ; column <= 6 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line + coordinatesRowIndex, column);
                    bidimensionalParametrizationOfArrayOfCoordinatesRows
                            [line]
                            [column]
                            [counter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                    logger.info("horizontal add loop ; line = " + line + " ; column = " + column + " ; coordinatesRowIndex = " + coordinatesRowIndex);
                }
                Coordinates[] coordinatesRow =
                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [line]
                                [column]
                                [counter[line][column]];
                logger.info("param[" + line + "," + column + "][" + counter[line][column] + "]=[" + coordinatesRow
                        + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                        + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                        + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                        + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                ;
                counter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {
                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [c.getX()]
                                [c.getY()]
                                [counter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        logger.info("param[" + c.getX() + "," + c.getY() + "][" + counter[c.getX()][c.getY()] + "]=[" + coordinatesRow
                                + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                                + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                                + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                                + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                        ;
                        counter[c.getX()][c.getY()]++;
                    }
                }
            }
        }

        // diagonally ascending Coordinates rows addition
        for (line = 0 ; line <= 2 ; line++) {
            for (column = 0 ; column <= 3 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line + coordinatesRowIndex, column + coordinatesRowIndex);
                    bidimensionalParametrizationOfArrayOfCoordinatesRows
                            [line]
                            [column]
                            [counter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                    logger.info("horizontal add loop ; line = " + line + " ; column = " + column + " ; coordinatesRowIndex = " + coordinatesRowIndex);
                }
                Coordinates[] coordinatesRow =
                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [line]
                                [column]
                                [counter[line][column]];
                logger.info("param[" + line + "," + column + "][" + counter[line][column] + "]=[ + coordinatesRow"
                        + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                        + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                        + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                        + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                ;
                counter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {

                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [c.getX()]
                                [c.getY()]
                                [counter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        logger.info("param[" + c.getX() + "," + c.getY() + "][" + counter[c.getX()][c.getY()] + "]=[" + coordinatesRow
                                + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                                + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                                + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                                + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                        ;
                        counter[c.getX()][c.getY()]++;
                    }
                }
            }
        }

        // diagonally descending Coordinates rows addition
        for (line = 0 ; line <= 2 ; line++) {
            for (column = 3 ; column <= 6 ; column++) {
                for (coordinatesRowIndex = 0 ; coordinatesRowIndex < 4 ; coordinatesRowIndex++) {
                    coordinates = CP.get(line + coordinatesRowIndex, column - coordinatesRowIndex);
                    bidimensionalParametrizationOfArrayOfCoordinatesRows
                            [line]
                            [column]
                            [counter[line][column]]
                            [coordinatesRowIndex]
                            =
                            coordinates;
                    logger.info("horizontal add loop ; line = " + line + " ; column = " + column + " ; coordinatesRowIndex = " + coordinatesRowIndex);
                }
                Coordinates[] coordinatesRow =
                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [line]
                                [column]
                                [counter[line][column]];
                logger.info("param[" + line + "," + column + "][" + counter[line][column] + "]=[" + coordinatesRow
                        + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                        + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                        + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                        + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                ;
                counter[line][column]++;
                for (Coordinates c : coordinatesRow) {
                    if (c != CP.get(line,column)) {

                        bidimensionalParametrizationOfArrayOfCoordinatesRows
                                [c.getX()]
                                [c.getY()]
                                [counter[c.getX()][c.getY()]]
                                =
                                coordinatesRow;
                        logger.info("param[" + c.getX() + "," + c.getY() + "][" + counter[c.getX()][c.getY()] + "]=[" + coordinatesRow
                                + "(" + coordinatesRow[0].getX() + "," + coordinatesRow[0].getY() + "), "
                                + "(" + coordinatesRow[1].getX() + "," + coordinatesRow[1].getY() + "), "
                                + "(" + coordinatesRow[2].getX() + "," + coordinatesRow[2].getY() + "), "
                                + "(" + coordinatesRow[3].getX() + "," + coordinatesRow[3].getY() + "), ")
                        ;
                        counter[c.getX()][c.getY()]++;
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


    /* debug
    public static void f() {
        Logger logger2 = LogManager.getLogger(ConstantGridParser.class);
        logger2.info("here");
        Coordinates[][][][] param  = new Coordinates[6][7][][];
        try {

            int line, column;
            for (line = 0; line < 6; line++) {
                for (column = 0; column < 7; column++) {
                    int nbOfCoordinatesRows = nbOfCoordinatesRowsContainingCoordinates(line, column);
                    logger2.info(line + "," + column + " -> " + nbOfCoordinatesRows);
                    param[line][column]
                            = new Coordinates[nbOfCoordinatesRows][4];
                }
            }

            Coordinates coordinates;
        } catch (Exception e) {
            ConstantGridParser.logger.error("Exception caught", e);

        }
    }

     */



}
