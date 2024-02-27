
package model;

import java.util.HashMap;
import java.util.HashSet;

// regroupe les données d'une grille de jeu ;
// hérite de la classe HashSet<Cell> ;
// plus précisément, une instance est un ensemble d'objet de classe Cell,
// une instance de la classe Cell représentant une cellule de la grille
// de jeu
public class Grid extends HashSet<Cell> {



  // ATTRIBUT



  // matrice des cellules (représentant les cases de la grille de jeu) qui,
  // à un couple d'entier associe la cellule située aux coordonnées
  // constituées par ce couple d'entiers ;
  // plus la première coordonnée est grande, plus la cellule est à droite ;
  // plus la deuxième coordonnée est grande, plus la cellule est en bas ;
  private Cell[][] matrixOfCells;



  // CONSTRUCTEUR



  public Grid(boolean... notInitialized) {

    this.matrixOfCells = new Cell[7][6];

    // on initialise les attributs relatifs à cette classe seulement
    // si l'argument optionnel est absent
    if (notInitialized.length == 0) {

      for (int i = 1 ; i <= 7 ; i++) {
        for (int j = 1 ; j <= 6 ; j++) {

            this.matrixOfCells[i - 1][j - 1]
              = new Cell(new Coordinates(i,j));
            this.add(this.matrixOfCells[i - 1][j - 1]);

        }
      }

    }



  }



  // ASSESSEURS



  public Cell[][] getMatrixOfCells() {
    return this.matrixOfCells;
  }

  public Cell getCell(int i, int j) {
    return this.matrixOfCells[i - 1][j - 1];
  }



  // MUTATEURS



  public void setMatrixOfCells(Cell[][] matrixOfCells) {
    this.matrixOfCells = matrixOfCells;
  }

  public void setCell(int i, int j, CellState color) {
    this.matrixOfCells[i - 1][j - 1].setCellState(color);
  }



  // MÉTHODES DE DÉBOGAGE



  // imprime une ligne horizontale ;
  // pour séparer les lignes de cases horizontalement à l'affichage ;
  // appelé dans la méthode display()
  private void printLine() {
    System.out.print("   ");
    for (int i = 0 ; i < 29 ; i++) System.out.print("-");
  }

  // affiche le plateau de jeu avec les jetons déjà joués et la chaîne
  // passée en guise de commentaire
  public void display(String comment) {

    for (int j = 1 ; j <= 6 ; j++) {
      printLine();
     if (j == 3) System.out.println("\t" + comment);
      else System.out.println();
      System.out.print(" " + j + " ");
      for (int i = 1 ; i <= 7 ; i++) {
        System.out.print(
          "| "
          + this.getCell(i,j).getCellState().getToken()
          + " ");
      }
      System.out.println("|");
    }
    printLine();
    System.out.println();
    System.out.print("   ");
    for (int i = 1 ; i <= 7 ; i++)
      System.out.print("  " + i + " ");
  }

  // donne la couleur CellState.TODEBUG aux cellules de
  // l'ensemble passé en paramètre
  public void markSetOfCell(HashSet<Cell> setOfCell) {

    for (Cell cell : setOfCell)
      cell.unobservableSetCellState(
        CellState.TODEBUG
      );

  }

  // donne les couleurs du tableau passé en premier paramètre aux
  // cellules de l'instance de FourCellsInARow passé en second
  // paramètre
  public void colorFourCellsInARow(
      CellState[] arrayOfColors,
      FourCellsInARow fourCellsInARow) {

    int k;

    k = 0;

    for (Cell cell : fourCellsInARow) {
      cell.unobservableSetCellState(
        arrayOfColors[k]
      );
      k++;
    }

  }



}
