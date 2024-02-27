
package view;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

// représente un disque ;
// classe mère des classes ColorableToken et PaintablePossibleNextToken
// représentant respectivement les jetons joués par les joueurs
// lors d'une partie de puissance 4 et les jetons mobiles non
// affichés définitivement pour montrer aux utilisateur l'
// éventuel prochain coup dans la colonne survolée s'il y clique
public class DrawingDisk extends DrawingElement {

  protected int radius;

  public DrawingDisk(int x, int y, int radius) {

    this.radius = radius;
    this.area = new Area(new Ellipse2D.Double(x, y, radius, radius));

  }

}
