
package view;

// importations pour gérer le dessin
import java.awt.geom.Rectangle2D;
import java.awt.TexturePaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;

// importation pour gérer le traitement des images
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// représente l'éventuel prochain jeton qui serait joué dans la colonne
// de la grille de jeu survolée par la souris sur l'instance de classe
// drawing représentant le dessin du jeu
public final class PaintablePossibleNextToken
    extends DrawingDisk
    implements Paintable {



  // ATTRIBUTS



  // chemin du fichier contenant l'image du jeton représentant l'éventuel
  // prochain coup
  private static final String IMAGE_PATH = "./view/question_mark.png";

  // image du jeton représentant l'éventuel prochain coup
  private BufferedImage image;

  // définit la façon dont une zone géomtrique (instance de Area) doit
  // être peinte
  private TexturePaint texturePaint;



  // CONSTRUCTEUR



  public PaintablePossibleNextToken(int x, int y, int radius, int panelHeight) {

    // appel au constructeur de la classe mère
    super(x, y, radius);

    // instance implémentant l'interface Shape représentant la forme de
    // l'image de l'éventuel prochain coup
    Shape imageRectangle;

    // initialisation de l'image de l'éventuel prochain coup
    try {
      this.image = null;
      image = ImageIO.read(new File(this.IMAGE_PATH));
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }
    imageRectangle
      = new Rectangle2D.Double(
        x,
        y,
        this.radius,
        this.radius
      );
    this.texturePaint
      = new TexturePaint(image, imageRectangle.getBounds2D());

  }



  // MÉTHODE DE DESSIN



  // pour peindre l'instance via un objet de classe Graphics2D
  public void draw(Graphics2D graphics2D) {

    graphics2D.setPaint(this.texturePaint);
    graphics2D.fill(this.area);

  }



  // MÉTHODE DE POSITIONNEMENT



  // pour positionner l'instance
  public void moveTo(int x, int y) {
    Shape imageRectangle
      = new Rectangle2D.Double(
        x,
        y,
        this.radius,
        this.radius
      );
    this.area = new Area(imageRectangle);
    this.texturePaint =
      new TexturePaint(
        image,
        imageRectangle.getBounds2D()
      );
  }



}
