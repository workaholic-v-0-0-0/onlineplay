
package model;

// provisoire
import java.util.Random;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

// regroupe les données représentant la grille de jeu (instance de la
// classe Grid dont cette classe hérite), les données déduites "constantes"
// (instance de la classe ConstantGridParser dont cette classe hérite), et
// les données déduites "évolutives" (instance de la classe
// EvolutiveGridParser dont cette classe hérite) ;
// grand-mère : Grid ;
// mère : ConstantGridParser ;
public final class EvolutiveGridParser extends ConstantGridParser {



  // ATTRIBUTS



  // dictionnaire qui, à un numéro de colonne, associe
  // le numéro de la ligne qui sera jouée au prochain coup
  // joué dans cette colonne ;
  // par convention, on traduit le fait que l'on ne peut plus
  // jouer dans une colonne quand sa valeur associée via ce
  // dictionnaire est 0
  private HashMap<ColumnNumber,LineNumber> columnNumberToNextLineNumber;

  // dictionnaire qui, à une couleur c (instance de l'énumération
  // CellState), associe le tableau qui, à un entier k de 0 à 4,
  // associe l'ensemble des groupes de quatre cellules alignées
  // comportant exactement k cellule(s) de la couleur c et dont les
  // autres cellules ne sont pas colorées
  private HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>
  colorToArrayOfSetOfMonochromaticFourCellsInARow;



  // CONSTRUCTEUR



  public EvolutiveGridParser(boolean... notInitialized) {

    // initialisation des attributs relatifs aux classes mères
    // (et grands-mères) représentant la grille de jeu et les
    // données déduites constantes
    super(notInitialized);

    // on initialise les attributs relatifs à cette classe seulement
    // si l'argument optionnel est absent
    if (notInitialized.length == 0) {

      // initialisation de l'attribut columnNumberToNextLineNumber
      initialiseColumnNumberToNextLineNumber();

      // initialisation de l'attribut
      // colorToArrayOfSetOfMonochromaticFourCellsInARow
      initialiseColorToArrayOfSetOfMonochromaticFourCellsInARow();

    }

  }



  // MÉTHODES D'INITIALISATION



  // initialisation de l'attribut columnNumberToNextLineNumber
  public void initialiseColumnNumberToNextLineNumber() {

    this.columnNumberToNextLineNumber = new HashMap<ColumnNumber,LineNumber>();

    for (ColumnNumber columnNumber : ColumnNumber.SET_OF_COLUMN_NUMBERS)
      columnNumberToNextLineNumber.put(
        columnNumber,
        LineNumber.SIX
      );

  }

  // initialisation de l'attribut
  // colorToArrayOfSetOfMonochromaticFourCellsInARow ;
  public void initialiseColorToArrayOfSetOfMonochromaticFourCellsInARow() {

    HashSet<CellState> setOfColors;

    setOfColors = new HashSet<CellState>();
    for (CellState color : CellState.values())
      if ((color != CellState.NULL) && (color != CellState.TODEBUG))
        setOfColors.add(color);

    this.colorToArrayOfSetOfMonochromaticFourCellsInARow
      = new HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>();

    for (CellState color : setOfColors) {

      this.colorToArrayOfSetOfMonochromaticFourCellsInARow.put(
        color,
        new ArrayList<HashSet<FourCellsInARow>>()
      );

      for (int k = 0 ; k <= 4 ; k++) {

        this
          .colorToArrayOfSetOfMonochromaticFourCellsInARow
          .get(color)
          .add(
            new HashSet<FourCellsInARow>()
          );

      }

      for (FourCellsInARow fourCellsInARow : this.setOfFourCellsInARow) {

        this
          .colorToArrayOfSetOfMonochromaticFourCellsInARow
          .get(color)
          .get(0)
          .add(fourCellsInARow);

      }

    }

  }



  // MÉTHODES DE MISE À JOUR



  // mise à jour de l'instance relative au coup joué dans la colonne
  // représentée par le premier paramètre avec la couleur représentée
  // par le deuxième paramètre
  public void updateWithMove(
      ColumnNumber columnNumber,
      CellState color) {

    // la cellule jouée
    Cell cell;

    // récupération de la cellule à modifier
    cell =
      this.getCell(
          columnNumber.toInt(),
        this
          .columnNumberToNextLineNumber
          .get(
            columnNumber
          )
          .toInt()
      );

    // mise à jour de la cellule à modifier ;
    // la vue écoutant les changements de valeurs de l'attribut
    // color des instances de Cell (représentant les cases) en est
    // donc informée
    cell.setCellState(color);

    // mise à jour de l'attribut columnNumberToNextLineNumber
    updateColumnNumberToNextLineNumber(columnNumber);

    // mise à jour des attributs
    // numberOfBeginningColorInARowToSetOfFourCellsInARow
    // et numberOfBeginningColorInARowToSetOfFourCellsInARow
    updateColorMonochromaticFourCellsInARowProperties(cell, color);

  }

  // mise à jour de l'attribut columnNumberToNextLineNumber
  public void updateColumnNumberToNextLineNumber(
      ColumnNumber columnNumber) {

    LineNumber nextLineNumber;

    nextLineNumber
      = this
        .columnNumberToNextLineNumber
        .get(columnNumber)
        .decrement();

    this.setColumnNumberToNextLineNumber(
      columnNumber,
      nextLineNumber
    );

  }

  // mise à jour des attributs
  // setOfFourCellsInARow, cellToSetOfFourCellsInARow,
  // et colorToArrayOfSetOfMonochromaticFourCellsInARow
  public void updateColorMonochromaticFourCellsInARowProperties(
      Cell playedCell,
      CellState color) {

    // ensemble des groupes de quatre cellules alignées
    // et monochromatiques contenant la cellule jouée
    HashSet<FourCellsInARow> setOfPlayedFourCellsInARow;

    // tableau qui, à un entier n, associe l'ensemble des groupes
    // de quatre cellules alignées, contenant la cellule jouée,
    // de couleur CellState.NULL ou celle jouée (color),
    // et contenant n cellules de la couleur jouée (color)
    ArrayList<HashSet<FourCellsInARow>>
    arrayOfSetOfMonochromaticFourCellsInARowOfThatColor;

    // tableau qui, à un entier n, associe l'ensemble des groupes
    // de quatre cellules alignées, contenant la cellule jouée,
    // de couleur CellState.NULL ou celle non jouée,
    // et contenant n cellules de la couleur non jouée
    ArrayList<HashSet<FourCellsInARow>>
    arrayOfSetOfMonochromaticFourCellsInARowOfTheOtherColor;

    // pour stocker les nombres de cellules d'instances de
    // FourCellsInARow qui sont d'une seule couleur
    int numberOfCellsOfThatColor = 0;
    int numberOfCellsOfTheOtherColor = 0;

    // pour stocker des instances de FourCellsInARow non
    // monochromatiques dont les réféences relatives aux
    // attributs setOfFourCellsInARow,
    // cellToSetOfFourCellsInARow et
    // colorToArrayOfSetOfMonochromaticFourCellsInARow
    HashSet<FourCellsInARow> setOfNotMonochromaticFourCellsInARow;

    // pour stocker des clefs du dictionnaire
    // cellToSetOfFourCellsInARow qui sont associées
    // à des ensembles d'instances de FourCellsInARow
    // dont des éléments doivent être supprimés
    HashSet<Cell> setOfKeysWhereToRemove;

    // ensemble des groupes de quatre cellules alignées
    // et monochromatiques contenant la cellule jouée
    setOfPlayedFourCellsInARow
      = this.cellToSetOfFourCellsInARow.get(playedCell);

    // tableau qui, à un entier n, associe l'ensemble des groupes
    // de quatre cellules alignées, contenant la cellule jouée,
    // de couleur CellState.NULL ou celle jouée (color),
    // et contenant n cellules de la couleur jouée (color)
    arrayOfSetOfMonochromaticFourCellsInARowOfThatColor
      = this
        .colorToArrayOfSetOfMonochromaticFourCellsInARow
        .get(color);

    // tableau qui, à un entier n, associe l'ensemble des groupes
    // de quatre cellules alignées, contenant la cellule jouée,
    // de couleur CellState.NULL ou celle non jouée,
    // et contenant n cellules de la couleur non jouée
    arrayOfSetOfMonochromaticFourCellsInARowOfTheOtherColor
    = this
      .colorToArrayOfSetOfMonochromaticFourCellsInARow
      .get(
        color.theOtherColor()
      );

    // pour stocker des instances de FourCellsInARow non
    // monochromatiques dont les réféences relatives aux
    // attributs setOfFourCellsInARow,
    // cellToSetOfFourCellsInARow et
    // colorToArrayOfSetOfMonochromaticFourCellsInARow
    setOfNotMonochromaticFourCellsInARow
      = new HashSet<FourCellsInARow>();

    // on parcourt l'ensemble des groupes de quatre cellules
    // alignées, contenant la cellule qui vient d'être jouée,
    // et qui étaient monochromatiques avant ce dernier coup
    for (FourCellsInARow fourCellsInARow : setOfPlayedFourCellsInARow) {

      // récupération des nombres de cellules de
      // fourCellsInARow qui sont d'une seule couleur
      // dans les variables numberOfCellsOfThatColor
      // et numberOfCellsOfTheOtherColor
      numberOfCellsOfThatColor
        = fourCellsInARow.numberOfColoredCells(color);
      numberOfCellsOfTheOtherColor
        = fourCellsInARow
          .numberOfColoredCells(
            color.theOtherColor()
          );

      // si le groupe de quatre cellules alignées est encore
      // monochromatique après le dernier coup
      if (fourCellsInARow.isMonochmomatic()) {

        // après modification de la couleur de la cellule,
        // fourCellsInARow est encore monochromatique ;
        // on doit donc, relativement à la
        // couleur jouée (color), modifier
        // colorToArrayOfSetOfMonochromaticFourCellsInARow
        // de façon à supprimer fourCellsInARow de l'ensemble
        // indexé par l'entier numberOfCellsOfThatColor - 1
        // et à ajouter fourCellsInARow à l'ensemble
        // indexé par l'entier numberOfCellsOfThatColor
        arrayOfSetOfMonochromaticFourCellsInARowOfThatColor
          .get(numberOfCellsOfThatColor - 1)
          .remove(fourCellsInARow);
        arrayOfSetOfMonochromaticFourCellsInARowOfThatColor
          .get(numberOfCellsOfThatColor)
          .add(fourCellsInARow);

        // si fourCellsInARow vient de recevoir son premier jeton,
        // il faut supprimer toutes ses références relatives
        // au tableau d'ensembles de groupes de quatre cellules
        // alignées et monochromatiques pour l'autre couleur ;
        // ainsi, ce dernier tableau ne sera constitué que d'ensembles
        // de groupes de quatre cellules monochromatiques de
        // couleur non jouée au dernier coup
        if (numberOfCellsOfThatColor == 1)
          arrayOfSetOfMonochromaticFourCellsInARowOfTheOtherColor
            .get(0)
            .remove(fourCellsInARow);

        // on ne supprime pas fourCellsInARow de l'ensemble
        // setOfFourCellsInARow puisque fourCellsInARow
        // est toujours monochromatique après le dernier coup
        // joué ;
        // on ne supprime pas non plus ses références relatives
        // au dictionnaire cellToSetOfFourCellsInARow

      }

      // si le groupe de quatre cellules alignées n'est plus
      // monochromatique après le dernier coup,
      // on doit supprimer les références à fourCellsInARow
      // relatives aux attributs setOfFourCellsInARow,
      // cellToSetOfFourCellsInARow et
      // colorToArrayOfSetOfMonochromaticFourCellsInARow ;
      // pour ce faire, on stocke d'abord ce groupe de
      // quatre cellules alignées dans l'ensemble
      // setOfNotMonochromaticFourCellsInARow
      else {

        setOfNotMonochromaticFourCellsInARow
          .add(
            fourCellsInARow
          );

      }

    }

    // suppressions des références aux groupes de quatre
    // cellules alignées non monochromatiques stockées
    // précédemment dans l'ensemble
    // setOfNotMonochromaticFourCellsInARow
    for (FourCellsInARow fourCellsInARow:
        setOfNotMonochromaticFourCellsInARow) {

      // récupération des nombres de cellules de
      // fourCellsInARow qui sont d'une seule couleur
      // dans les variables numberOfCellsOfThatColor
      // et numberOfCellsOfTheOtherColor
      numberOfCellsOfThatColor
        = fourCellsInARow.numberOfColoredCells(color);
      numberOfCellsOfTheOtherColor
        = fourCellsInARow
          .numberOfColoredCells(
            color.theOtherColor()
          );

      // suppression des références à fourCellsInARow
      // relatives à l'attribut setOfFourCellsInARow
      this.setOfFourCellsInARow.remove(fourCellsInARow);


      // suppression des références à fourCellsInARow
      // relatives à l'attribut cellToSetOfFourCellsInARow
      setOfKeysWhereToRemove = new HashSet<Cell>();
      for (Cell cell : fourCellsInARow)
        setOfKeysWhereToRemove.add(cell);
      for (Cell cell : setOfKeysWhereToRemove)
        this
          .cellToSetOfFourCellsInARow
          .get(cell)
          .remove(fourCellsInARow);

      // suppression des références à fourCellsInARow
      // relatives à l'attribut
      // colorToArrayOfSetOfMonochromaticFourCellsInARow
      arrayOfSetOfMonochromaticFourCellsInARowOfThatColor
        .get(numberOfCellsOfThatColor - 1)
        .remove(fourCellsInARow);
      arrayOfSetOfMonochromaticFourCellsInARowOfTheOtherColor
        .get(numberOfCellsOfTheOtherColor)
        .remove(fourCellsInARow);

    }

  }



  // ASSESSEURS



  public HashMap<ColumnNumber,LineNumber> getColumnNumberToNextLineNumber() {
    return this.columnNumberToNextLineNumber;
  }

  public LineNumber getColumnNumberToNextLineNumber(ColumnNumber columnNumber) {
    return this.columnNumberToNextLineNumber.get(columnNumber);
  }

  public HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>
  getColorToArrayOfSetOfMonochromaticFourCellsInARow() {
    return this.colorToArrayOfSetOfMonochromaticFourCellsInARow;
  }



  // MUTATEURS



  public void setColumnNumberToNextLineNumber(
      HashMap<ColumnNumber,LineNumber> columnNumberToNextLineNumber) {
    this.columnNumberToNextLineNumber = columnNumberToNextLineNumber;
  }

  public void setColumnNumberToNextLineNumber(
      ColumnNumber columnNumber,
      LineNumber nextLineNumber) {
    this.columnNumberToNextLineNumber.put(
      columnNumber,
      nextLineNumber
    );
  }

  public void setColorToArrayOfSetOfMonochromaticFourCellsInARow(
      HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>
      colorToArrayOfSetOfMonochromaticFourCellsInARow) {
    this.colorToArrayOfSetOfMonochromaticFourCellsInARow
      = colorToArrayOfSetOfMonochromaticFourCellsInARow;
  }



  // MÉTHODES DE DÉBOGAGE



  public void showColorToArrayOfSetOfMonochromaticFourCellsInARow() {

    for (CellState color :
        this.colorToArrayOfSetOfMonochromaticFourCellsInARow.keySet()) {

      ArrayList<HashSet<FourCellsInARow>>
      arrayOfSetOfMonochromaticFourCellsInARow
        = this
          .colorToArrayOfSetOfMonochromaticFourCellsInARow
          .get(color);

      for (int k = 0 ; k <= 4 ; k++) {

        System.out.println("\n" + k + " : ");

        HashSet<FourCellsInARow> setOfFourCellsInARow
          = arrayOfSetOfMonochromaticFourCellsInARow.get(k);

        for (FourCellsInARow fourCellsInARow : setOfFourCellsInARow) {

          System.out.println(fourCellsInARow);
          System.out.println("\n");

        }

      }

    }

  }

  public void showSetOfFourCellsInARow() {

    System.out.println("setOfFourCellsInARow :");

    for (FourCellsInARow fourCellsInARow : this.setOfFourCellsInARow) {

      System.out.println(fourCellsInARow);

    }

  }

  public void showCellToSetOfFourCellsInARow() {

    for (Cell cell : this.cellToSetOfFourCellsInARow.keySet()) {

      System.out.println(
        "\n"
        + cell
        + " :\n"
      );

      for (FourCellsInARow fourCellsInARow :
          this.cellToSetOfFourCellsInARow.get(cell)) {

        System.out.println(fourCellsInARow + "\n");

      }

      System.out.println("\n\n\n\n");

    }

  }

  public FourCellsInARow aFourCellsInARowAtRandom() {

    int size;

    int randomIndex;

    int index;

    size = this.setOfFourCellsInARow.size();

    randomIndex = new Random().nextInt(size);

    index = 0;

    for (FourCellsInARow fourCellsInARow : this.setOfFourCellsInARow) {

      if (index == randomIndex) return fourCellsInARow;
      index++;

    }

    return null;

  }



}
