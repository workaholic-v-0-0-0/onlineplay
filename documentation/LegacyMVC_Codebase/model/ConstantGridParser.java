
package model;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

// regroupe les données représentant la grille de jeu (instance de la
// classe Grid dont cette classe hérite) et les données déduites "constantes"
public class ConstantGridParser extends Grid{



  // ATTRIBUTS



  // ensemble des groupes de quatre cellules alignées ;
  // cet attribut sera en fait modifiable depuis la classe fille
  // EvolutiveGridParser afin d'allèger la mémoire
  protected HashSet<FourCellsInARow> setOfFourCellsInARow;

  // dictionnaire qui, à une cellule, associe l'ensemble des groupes de
  // quatre cellules alignées dont elle appartient ;
  // cet attribut sera en fait modifiable depuis la classe fille
  // EvolutiveGridParser afin d'allèger la mémoire
  protected HashMap<Cell,HashSet<FourCellsInARow>>
  cellToSetOfFourCellsInARow;



  // CONSTRUCTEUR



  public ConstantGridParser(boolean... notInitialized) {

    // initialisation des attributs relatifs à la classe mère
    // représentant la grille de jeu
    super(notInitialized);

    // on initialise les attributs relatifs à cette classe seulement
    // si l'argument optionnel est absent
    if (notInitialized.length == 0) {

      // initialisation de l'attribut setOfFourCellsInARow
      initialiseSetOfFourCellsInARow();

      // initialisation de l'attribut cellToSetOfFourCellsInARow
      initialiseCellToSetOfFourCellsInARow();

    }

  }



  // MÉTHODES D'INITIALISATION



  // initialisation de l'attribut setOfFourCellsInARow
  @SuppressWarnings("unchecked")
  private void initialiseSetOfFourCellsInARow() {

    int i, j, k;
    HashSet fourCellsInARow;

    this.setOfFourCellsInARow = new HashSet<FourCellsInARow>();

    // adjonction des ensembles connexes de quatre cellules alignées
    // horizontaux
    for (i = 1 ; i <= 4 ; i++) {
      for (j = 1 ; j <= 6 ; j++) {
        fourCellsInARow = new HashSet<Cell>();
        for (k = 0 ; k < 4 ; k++) {
          fourCellsInARow.add(this.getCell(i+k,j));
        }
        try {
          setOfFourCellsInARow.add(new FourCellsInARow(fourCellsInARow));
        }
        catch (NotARightFourCellsInARowException exception) {
          System.out.println(exception.getMessage());
        }
      }
    }

    // adjonction des ensembles connexes de quatre cellules alignées
    // verticaux
    for (i = 1 ; i <= 7 ; i++) {
      for (j = 1 ; j <= 3 ; j++) {
        fourCellsInARow = new HashSet<Cell>();
        for (k = 0 ; k < 4 ; k++) {
          fourCellsInARow.add(this.getCell(i,j+k));
        }
        try {
          setOfFourCellsInARow.add(new FourCellsInARow(fourCellsInARow));
        }
        catch (NotARightFourCellsInARowException exception) {
          System.out.println(exception.getMessage());
        }
      }
    }

    // adjonction des ensembles connexes de quatre cellules alignées
    // oblique descendant
    for (i = 1 ; i <= 4 ; i++) {
      for (j = 1 ; j <= 3 ; j++) {
        fourCellsInARow = new HashSet<Cell>();
        for (k = 0 ; k < 4 ; k++) {
          fourCellsInARow.add(this.getCell(i+k,j+k));
        }
        try {
          setOfFourCellsInARow.add(new FourCellsInARow(fourCellsInARow));
        }
        catch (NotARightFourCellsInARowException exception) {
          System.out.println(exception.getMessage());
        }
      }
    }

    // adjonction des ensembles connexes de quatre cellules alignées
    // oblique ascendant
    for (i = 1 ; i <= 4 ; i++) {
      for (j = 4 ; j <= 6 ; j++) {
        fourCellsInARow = new HashSet<Cell>();
        for (k = 0 ; k < 4 ; k++) {
          fourCellsInARow.add(this.getCell(i+k,j-k));
        }
        try {
          setOfFourCellsInARow.add(new FourCellsInARow(fourCellsInARow));
        }
        catch (NotARightFourCellsInARowException exception) {
          System.out.println(exception.getMessage());
        }
      }
    }

  }

  // initialisation de l'attribut cellToSetOfFourCellsInARow
  private void initialiseCellToSetOfFourCellsInARow() {

    this.cellToSetOfFourCellsInARow
      = new HashMap<Cell,HashSet<FourCellsInARow>>();

    for (Cell cell : this)
      this.cellToSetOfFourCellsInARow.put(
        cell,
        new HashSet<FourCellsInARow>()
      );

    for (FourCellsInARow fourCellsInARow : this.setOfFourCellsInARow) {

      for (Cell cell : fourCellsInARow) {

        this
          .cellToSetOfFourCellsInARow
          .get(cell)
          .add(
            fourCellsInARow
          );

      }

    }

  }



  // ASSESSEURS



  public HashSet<FourCellsInARow> getSetOfFourCellsInARow() {
    return this.setOfFourCellsInARow;
  }

  public HashMap<Cell,HashSet<FourCellsInARow>>
  getCellToSetOfFourCellsInARow() {
    return this.cellToSetOfFourCellsInARow;
  }



  // MUTATEURS



  public void setSetOfFourCellsInARow(
      HashSet<FourCellsInARow> setOfFourCellsInARow) {
    this.setOfFourCellsInARow = setOfFourCellsInARow;
  }

  public void setCellToSetOfFourCellsInARow(
      HashMap<Cell,HashSet<FourCellsInARow>> cellToSetOfFourCellsInARow) {
    this.cellToSetOfFourCellsInARow = cellToSetOfFourCellsInARow;
  }



  // MÉTHODES DE DÉBOGAGE



  public void showSetOfCell(HashSet<Cell> setOfCell) {
    for (Cell cell : setOfCell)
      this.setCell(
        cell
          .getCoordinates()
          .getX()
        ,
        cell
          .getCoordinates()
          .getY()
        ,
        CellState.TODEBUG
      );
    this.display("");
  }



}
