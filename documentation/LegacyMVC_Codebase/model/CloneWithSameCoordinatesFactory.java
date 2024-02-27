
package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

// classe statique regroupant les méthodes créant des pseudo-copie
// d'objets ; en effet, seules les instances de la classe
// Coordinates ne sont pas recopiées
public final class CloneWithSameCoordinatesFactory {

  // renvoie un pointeur vers une copie de la cellule
  // passée en paramètre
  public static Cell cloneCell(Cell cell) {

    Cell clone;

    clone
      = new Cell(
          cell.getCoordinates(),
          cell.getCellState()
        );

    return clone;

  }

  // renvoie un dictionnaire qui, à chaque cellule de la grille
  // passée en paramètre, associe une cellule clone, i.e. un pointeur
  // vers une copie de cette cellule
  public static HashMap<Cell,Cell> cellToCellClone(Grid grid) {

    HashMap<Cell,Cell> cellToCellClone;

    cellToCellClone = new HashMap<Cell,Cell>();

    for (Cell cell : grid)
      cellToCellClone.put(
        cell,
        cloneCell(cell)
      );

    return cellToCellClone;

  }

  // renvoie une copie de la matrice de cellules passée en premier paramètre
  // suivant le dictionnaire passé en second paramètre ;
  // permet de cloner l'attribut matrixOfCells de Grid
  public static Cell[][] cloneMatrixOfCells(
      Cell[][] matrixOfCells,
      HashMap<Cell,Cell> cellToCellClone) {

    Cell[][] clone;

    clone = new Cell[7][6];

    for (int i = 0 ; i < 7 ; i++)
      for (int j = 0 ; j < 6 ; j++)
        clone[i][j]
          = cellToCellClone.get(
            matrixOfCells[i][j]
          );

    return clone;

  }

  // renvoie une copie de la grille passée en premier paramètre
  // suivant le dictionnaire passé en second paramètre
  public static Grid cloneGrid(
      Grid grid,
      HashMap<Cell,Cell> cellToCellClone) {

    Grid clone;

    clone = new Grid(true);

    clone.setMatrixOfCells(
      cloneMatrixOfCells(
        grid.getMatrixOfCells(),
        cellToCellClone
      )
    );

    return clone;

  }

  // renvoie une copie du groupe de quatre cellules alignées passé
  // en premier paramètre suivant le dictionnaire passé en second paramètre
  public static FourCellsInARow cloneFourCellsInARow(
      FourCellsInARow fourCellsInARow,
      HashMap<Cell,Cell> cellToCellClone) {

    FourCellsInARow clone;

    clone = new FourCellsInARow();

    for (Cell cell : fourCellsInARow)
      clone.add(
        cellToCellClone.get(cell)
      );

    return clone;

  }

  // renvoie un dictionnaire qui, à chaque groupe de quatre cellules alignées
  // élément de l'ensemble passé en premier paramètre, associe une copie de
  // ce groupe dont les cellules qu'il contient correspondent aux cellules
  // suivant le dictionnaire passé en second paramètre
  public static HashMap<FourCellsInARow,FourCellsInARow>
  fourCellsInARowToFourCellsInARowClone(
      HashSet<FourCellsInARow> setOfFourCellsInARow,
      HashMap<Cell,Cell> cellToCellClone) {

    HashMap<FourCellsInARow,FourCellsInARow>
    fourCellsInARowToFourCellsInARowClone;

    fourCellsInARowToFourCellsInARowClone
      = new HashMap<FourCellsInARow,FourCellsInARow>();

    for (FourCellsInARow fourCellsInARow : setOfFourCellsInARow)
      fourCellsInARowToFourCellsInARowClone.put(
        fourCellsInARow,
        cloneFourCellsInARow(
          fourCellsInARow,
          cellToCellClone
        )
      );

    return fourCellsInARowToFourCellsInARowClone;

  }

  // renvoie une copie de l'ensemble passé en premier paramètre
  // en copiant ses éléments et les éléments de ses éléments suivant
  // les correspondances définies par les dictionnaires passés en
  // deuxième et en troisième paramètres ;
  // permet de cloner l'attribut setOfFourCellsInARow
  // de ConstantGridParser
  public static HashSet<FourCellsInARow> cloneSetOfFourCellsInARow(
      HashSet<FourCellsInARow> setOfFourCellsInARow,
      HashMap<FourCellsInARow,FourCellsInARow>
      fourCellsInARowToFourCellsInARowClone) {

    HashSet<FourCellsInARow> clone;

    clone = new HashSet<FourCellsInARow>();

    for (FourCellsInARow fourCellsInARow : setOfFourCellsInARow)
      clone.add(
        fourCellsInARowToFourCellsInARowClone.get(
          fourCellsInARow
        )
      );

    return clone;

  }

  // renvoie une copie du dictionnaire passé en premier argument
  // suivant les correspondances définies par les dictionnaires
  // passés en deuxième et en troisième paramètres ;
  // permet de cloner l'attribut cellToSetOfFourCellsInARow
  // de ConstantGridParser
  public static HashMap<Cell,HashSet<FourCellsInARow>>
  cloneCellToSetOfFourCellsInARow(
      HashMap<Cell,HashSet<FourCellsInARow>> cellToSetOfFourCellsInARow,
      HashMap<Cell,Cell> cellToCellClone,
      HashMap<FourCellsInARow,FourCellsInARow>
      fourCellsInARowToFourCellsInARowClone) {

    HashMap<Cell,HashSet<FourCellsInARow>> clone;

    HashSet<FourCellsInARow> setOfFourCellsInARow;

    clone = new HashMap<Cell,HashSet<FourCellsInARow>>();

    for (Cell cell : cellToSetOfFourCellsInARow.keySet()) {

      setOfFourCellsInARow = new HashSet<FourCellsInARow>();
      for (FourCellsInARow fourCellsInARow :
          cellToSetOfFourCellsInARow.get(cell))
        setOfFourCellsInARow.add(
          fourCellsInARowToFourCellsInARowClone.get(
            fourCellsInARow
          )
        );

      clone.put(
        cellToCellClone.get(cell),
        setOfFourCellsInARow
      );

    }

    return clone;

  }

  // renvoie une copie du dictionnaire passé en premier argument
  // suivant les correspondances définies par les dictionnaires
  // passés en deuxième et en troisième paramètres ;
  // permet de cloner l'attribut
  // colorToArrayOfSetOfMonochromaticFourCellsInARow
  // de EvolutiveGridParser
  public static HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>
  cloneColorToArrayOfSetOfMonochromaticFourCellsInARow(
      HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>
      colorToArrayOfSetOfMonochromaticFourCellsInARow,
      HashMap<FourCellsInARow,FourCellsInARow>
      fourCellsInARowToFourCellsInARowClone) {

    HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>> clone;

    ArrayList<HashSet<FourCellsInARow>>
    arrayOfSetOfMonochromaticFourCellsInARow;

    ArrayList<HashSet<FourCellsInARow>>
    arrayOfSetOfMonochromaticFourCellsInARowClone;

    HashSet<FourCellsInARow> setOfMonochromaticFourCellsInARow;

    clone = new HashMap<CellState,ArrayList<HashSet<FourCellsInARow>>>();

    for (CellState color :
        colorToArrayOfSetOfMonochromaticFourCellsInARow.keySet()) {

      arrayOfSetOfMonochromaticFourCellsInARow
        = colorToArrayOfSetOfMonochromaticFourCellsInARow.get(color);

      arrayOfSetOfMonochromaticFourCellsInARowClone
        = new ArrayList<HashSet<FourCellsInARow>>();

      for (
          int k = 0 ;
          k < arrayOfSetOfMonochromaticFourCellsInARow.size();
          k++) {

        arrayOfSetOfMonochromaticFourCellsInARowClone.add(
          new HashSet<FourCellsInARow>()
        );

        arrayOfSetOfMonochromaticFourCellsInARowClone.set(
          k,
          cloneSetOfFourCellsInARow(
            arrayOfSetOfMonochromaticFourCellsInARow
              .get(k),
            fourCellsInARowToFourCellsInARowClone
          )
        );

      }

      clone.put(
        color,
        arrayOfSetOfMonochromaticFourCellsInARowClone
      );

    }

    return clone;

  }

  // retourne une copie du parseur évolutif de grille passé en paramètre ;
  // seules les coordonnées des cellules ne sont pas recopiées
  public static EvolutiveGridParser cloneEvolutiveGridParser(
      EvolutiveGridParser evolutiveGridParser) {

    // la copie du parseur évolutif de grille passé en paramètre à retourner
    EvolutiveGridParser clone;

    // dictionnaire qui, à chaque cellule du paramètre evolutiveGridParser,
    // associe une copie de cette cellule
    HashMap<Cell,Cell> cellToCellClone;

    // dictionnaire qui, à chaque groupe de quatre cellules alignées du
    // paramètre evolutiveGridParser, associe une copie de ce groupe
    HashMap<FourCellsInARow,FourCellsInARow>
    fourCellsInARowToFourCellsInARowClone;

    // déclaration d'une instance de EvolutiveGridParser
    // sans initialisation d'attribut
    clone = new EvolutiveGridParser(true);

    // initialisation d'un dictionnaire qui, à chaque cellule du
    // paramètre evolutiveGridParser, associe une copie de cette cellule
    cellToCellClone = cellToCellClone((Grid) evolutiveGridParser);

    // initialisation d'un dictionnaire qui, à chaque groupe de quatre
    // cellules alignées du paramètre evolutiveGridParser, associe une
    // copie de ce groupe

    fourCellsInARowToFourCellsInARowClone
      = fourCellsInARowToFourCellsInARowClone(
          evolutiveGridParser.getSetOfFourCellsInARow(),
          cellToCellClone
        );

    // INITIALISATION RELATIVE À LA CLASSE GRID

    // adjonction des copies des cellules du parseur évolutif de grille
    // passé en paramètre qui leur sont associées via le dictionnaire
    // cellToCellClone
    for (Cell cell : cellToCellClone.keySet())
      clone.add(cellToCellClone.get(cell));

    // initialisation de l'attribut matrixOfCells de la copie du parseur
    // évolutif de grille à retourner
    clone.setMatrixOfCells(
      cloneMatrixOfCells(
        evolutiveGridParser.getMatrixOfCells(),
        cellToCellClone
      )
    );

    // INITIALISATION RELATIVE À LA CLASSE ConstantGridParser

    // initialisation de l'attribut setOfFourCellsInARow de la copie
    // du parseur évolutif de grille à retourner
    clone.setSetOfFourCellsInARow(
      cloneSetOfFourCellsInARow(
        evolutiveGridParser.getSetOfFourCellsInARow(),
        fourCellsInARowToFourCellsInARowClone)
    );

    // initialisation de l'attribut cellToSetOfFourCellsInARow de la
    // copie du parseur évolutif de grille à retourner
    clone.setCellToSetOfFourCellsInARow(
      cloneCellToSetOfFourCellsInARow(
        evolutiveGridParser.getCellToSetOfFourCellsInARow(),
        cellToCellClone,
        fourCellsInARowToFourCellsInARowClone
      )
    );

    // INITIALISATION RELATIVE À LA CLASSE EvolutiveGridParser

    // initialisation de l'attribut columnNumberToNextLineNumber de la
    // copie du parseur évolutif de grille à retourner
    clone.setColumnNumberToNextLineNumber(
      new HashMap<ColumnNumber,LineNumber>(
        evolutiveGridParser.getColumnNumberToNextLineNumber()
      )
    );

    // initialisation de l'attribut
    // colorToArrayOfSetOfMonochromaticFourCellsInARow de la
    // copie du parseur évolutif de grille à retourner
    clone.setColorToArrayOfSetOfMonochromaticFourCellsInARow(
      cloneColorToArrayOfSetOfMonochromaticFourCellsInARow(
        evolutiveGridParser
          .getColorToArrayOfSetOfMonochromaticFourCellsInARow(),
        fourCellsInARowToFourCellsInARowClone
      )
    );

    return clone;

  }

}
