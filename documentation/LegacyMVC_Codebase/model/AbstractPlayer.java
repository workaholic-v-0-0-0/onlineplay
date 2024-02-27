
package model;

// classe abstraite représentant un joueur ;
// classe mère des classes HumanPlayer et AIPlayer représentant
// respectivement les joueur humains et les joueurs IA
public abstract class AbstractPlayer {



  // ATTRIBUT



  // couleur jouée par le joueur
  protected CellState color;



  // MÉTHODE DE MISE À JOUR



  // mise à jour relative au coup joué dans la colonne
  // représentée par le paramètre de classe ColumnNumber ;
  // la classe fille HumanPlayer n'implémentera pas cette
  // méthode car dans ce cas cette mise à jour se fait dans le
  // cerveau de l'utilisateur en question et non dans l'ordinateur
  public abstract void updateWithMove(
      ColumnNumber columnNumberWhichHasBeenPlayed);



  // ASSESSEURS



  public CellState getColor() {return this.color;}



  // MUTATEURS



  public void setColor(CellState color) {this.color = color;}



}
