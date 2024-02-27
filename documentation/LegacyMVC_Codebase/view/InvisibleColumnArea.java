
package view;

// importations du package model
import model.ColumnNumber;
import model.LineNumber;

// représente une zone délimittant une colonne sur le dessin
// représentant la grille de jeu ;
// simplifie la localisation de la souris afin de faire jouer
// les utilisateurs dans la colonne qu'il survole
public final class InvisibleColumnArea extends DrawingRectangle {



  // ATTRIBUTS



  // dimentions
  public static int width;
  public static int height;

  // le numéro de la colonne
  private ColumnNumber columnNumber;

  // le numéro de la ligne la plus basse non occupée par un jeton
  // dans la colonne représentée par l'instance
  private LineNumber nextLineNumber;



  // CONSTRUCTEUR



  public InvisibleColumnArea(ColumnNumber columnNumber) {

    super(
      (columnNumber.toInt() - 1) * InvisibleColumnArea.width,
      0,
      InvisibleColumnArea.width,
      InvisibleColumnArea.height
    );

    this.columnNumber = columnNumber;

    // au début de la partie la ligne ligne 6 est la ligne
    // la plus basse non occupée par un jeton
    this.nextLineNumber = LineNumber.SIX;

  }



  // ASSESSEURS



  public ColumnNumber getColumnNumber() {return this.columnNumber;}
  public LineNumber getNextLineNumber() {return this.nextLineNumber;}



  // MUTATEURS



  // mutateurs statiques
  public static void setWidth(int width) {InvisibleColumnArea.width = width;}
  public static void setHeight(int height) {
    InvisibleColumnArea.height = height;
  }

  public void setNextLineNumber(LineNumber nextLineNumber) {
    this.nextLineNumber = nextLineNumber;
  }



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return this.columnNumber + ", " + this.nextLineNumber;
  }



}
