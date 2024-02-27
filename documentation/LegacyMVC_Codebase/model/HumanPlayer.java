
package model;

// représente un joueur utilisateur
public final class HumanPlayer extends AbstractPlayer {



  // CONSTRUCTEUR



  public HumanPlayer(CellState color) {

    this.color = color;

  }


  // mise à jour relative au coup joué dans la colonne
  // représentée par le paramètre de classe ColumnNumber
  public void updateWithMove(ColumnNumber columnNumber) {

    // l'utilisateur a évidemment la charge de se mettre à jour tout seul
    // en prenant connaissance du coup joué via l'interface graphique

  }

}
