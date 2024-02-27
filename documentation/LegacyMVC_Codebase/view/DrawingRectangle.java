
package view;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

// représente un rectangle ;
// classe mère de la classe InvisibleColumnArea représentant une zone
// délimittant une colonne sur le dessin représentant la grille de jeu
public abstract class DrawingRectangle extends DrawingElement {

  public DrawingRectangle(int x, int y, int width, int height) {

    this.area = new Area(new Rectangle2D.Double(x, y, width, height));

  }

}
