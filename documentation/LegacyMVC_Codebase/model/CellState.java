
package model;

// représente l'état d'une case
public enum CellState {

  RED("X"), // rouge
  YELLOW("O"), // jaune
  NULL(" "),  // pas de jeton
  TODEBUG("*"); // pour déboguer



  // ATTRIBUTS



  private String token;



  // CONSTRUCTEUR PRIVÉ



  private CellState(String token) {this.token = token;}



  // MÉTHODES



  // renvoie l'autre couleur joué
  public CellState theOtherColor() {

    return (this == CellState.RED) ? CellState.YELLOW : CellState.RED;

  }

  // renvoie une chaine de caractères correspondant à la couleur
  // représentée par l'instance
  @Override
  public String toString() {
    if (this == CellState.RED)
      return "Rouge";
    else if (this == CellState.YELLOW)
      return "Jaune";
    else
      return "null";
  }



  // ASSESSEUR



  public String getToken() {return this.token;}



}
