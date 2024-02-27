
package view;

// importations pour gérer le dessin
import java.awt.geom.Area;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;

// représente un jeton joué par un joueur lors d'une partie de
// puissance 4
public final class ColorableToken
    extends DrawingDisk
    implements Colorable {

  private Color color;

  public ColorableToken(int x, int y, int radius, Color color) {

    super(x, y, radius);
    this.color = color;

  }



  // MÉTHODE DE DESSIN



  // pour colorier l'instance via un objet de classe Graphics2D
  @Override
  public void draw(Graphics2D graphics2D) {

    graphics2D.setColor(this.color);
    graphics2D.fill(this.area);

  }



  // MÉTHODE DE POSITIONNEMENT



  // pour positionner l'instance
  public void moveTo(int x, int y) {
    this.area
      = new Area(
        new Ellipse2D.Double(
          x,
          y,
          this.radius,
          this.radius
        )
      );
  }



  // MÉTHODES DE DÉBOGAGE



  public Color getColor() {return this.color;}

  @Override
  public String toString() {

    return " color = " + this.color + " ;";
  }



}
