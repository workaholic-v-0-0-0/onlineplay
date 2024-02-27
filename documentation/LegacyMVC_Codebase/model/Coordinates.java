
package model;

// représente les coordonnées d'une case sur le plateau de jeu ;
public class Coordinates {



  // ATTRIBUTS



  private int x;
  private int y;



  // CONSTRUCTEUR



  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }



  // ASSESSEURS



  public int getX() {return this.x;}
  public int getY() {return this.y;}



  // MUTATEURS



  public void setX(int x) {this.x = x;}
  public void setY(int y) {this.y = y;}



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return
      "("
      + this.x
      + ","
      + this.y
      + " at "
      + super.toString()
      + ")";
  }



}
