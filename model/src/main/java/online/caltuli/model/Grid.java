package online.caltuli.model;

public class Grid {
    private Cell[][] matrixOfCells;

    public Grid(boolean... notInitialized) {

        this.matrixOfCells = new Cell[7][6];

        // on initialise les attributs relatifs à cette classe seulement
        // si l'argument optionnel est absent
        if (notInitialized.length == 0) {

            for (int i = 1 ; i <= 7 ; i++) {
                for (int j = 1 ; j <= 6 ; j++) {

                    this.matrixOfCells[i - 1][j - 1]
                            = new Cell(new Coordinates(i,j));
                }
            }
        }
    }

}
