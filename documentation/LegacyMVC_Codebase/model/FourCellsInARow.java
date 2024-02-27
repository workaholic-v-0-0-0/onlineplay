
package model;

import java.util.HashSet;

// ensemble de quatre cellules alignées dans la grille de jeu
public class FourCellsInARow extends HashSet<Cell> {



  // CONSTRUCTEURS



  public FourCellsInARow(HashSet<Cell> fourCellsInARow)
    throws NotARightFourCellsInARowException {

    super(fourCellsInARow);

    if (this.size() != 4) throw new NotARightFourCellsInARowException();

  }

  public FourCellsInARow() {

    super();

  }



  // MÉTHODES



  // indique si l'instance ne contient que des cellules non encore
  // jouées ou que d'une couleur données
  public boolean isMonochmomatic() {

    CellState cellState1, cellState2;

    for (Cell cell1 : this) {

      cellState1 = cell1.getCellState();

      for (Cell cell2 : this) {

        cellState2 = cell2.getCellState();

        if ((cellState1 != CellState.NULL)
            && (cellState2 != CellState.NULL)
            && (cell1 != cell2)
            && (cellState1 != cellState2)) {

          return false;

        }

      }

    }

    return true;

  }

  // renvoie le nombre de cellules colorées de l'instance
  public int numberOfColoredCells(CellState color) {

    int numberOfColoredCells;

    numberOfColoredCells = 0;

    for (Cell cell : this)
      if (cell.getCellState() == color)
        numberOfColoredCells++;

    return numberOfColoredCells;

  }

  // si l'instance est un groupe de quatre cellules colorées,
  // retourne un grand nombre ;
  // sinon, renvoie l'altitude moyenne des cellules non colorées
  // appartenant à l'instance i.e. la moyenne de leur coordonnée Y ;
  // attention : plus la coordonnée Y est grande, plus la cellule
  // est basse dans l'affichage ;
  // utilisé par la classe AIPlayer pour évaluer les positions ;
  public float uncoloredCellsAltitudeAverage() {

    float uncoloredCellsAltitudeAverage = 0;
    int uncoloredCellsNumber = 0;

    for (Cell cell : this)
      if (cell.getCellState() == CellState.NULL) {
        uncoloredCellsNumber++;
        uncoloredCellsAltitudeAverage += cell.getCoordinates().getY();
      }

    // si l'instance est un groupe de quatre cellules colorées,
    // on retourne un grand nombre ;
    // ce cas correspond à une combinaison gagnante ;
    // cette méthode est en effet appelée par les méthodes
    // d'évaluation des instances de AIPlayer représentant des
    // joueurs IA pour des groupes de quatre cellules alignées
    // monochromatiques
    if (uncoloredCellsNumber == 0) return 100000;

    // sinon (s'il y a au moins une cellule non jouée dans le groupe
    // de quatre cellules alignées représentant l'instance), on renvoie
    // la moyenne des numéros de ligne des cellules non jouées ;
    // plus la valeurs est grande, plus l'instance est potentiellement
    // gagnable ;
    // cette méthode est en effet appelée par les méthodes
    // d'évaluation des instances de AIPlayer représentant des
    // joueurs IA pour des groupes de quatre cellules alignées
    // monochromatiques, et les plus grandes valeurs correspondent
    // donc à des groupes de quatre cellules alignées au plus bas dans
    // la grille
    else {
      uncoloredCellsAltitudeAverage /= uncoloredCellsNumber;
      return uncoloredCellsAltitudeAverage;
    }

  }



  // MÉTHODE DE DÉBOGAGE



  @Override
  public String toString() {

    String res;

    res = "";

    for (Cell cell : this) {

      res
        = res
          + cell
          + "\n";

    }

    return res;

  }

  public String showCoordinates() {

    String res;

    res = "";

    for (Cell cell : this)
      res =
        res
        + cell.getCoordinates().getX()
        + ","
        + cell.getCoordinates().getY()
        + ";";

    return res;

  }



}
