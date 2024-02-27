
package view;

// importations pour gérer le dessin
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.TexturePaint;

// importation pour gérer le traitement des images
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

// importation de la classe HashSet pour construire des ensembles d'objets
import java.util.HashSet;

// regroupe les données relatives à la zone regrésentant le grille du
// jeu et à sa façon de la dessiner
public final class PaintableGrid
  extends DrawingElement
  implements Paintable {

  // définitions des constantes caractérisant les positions et dimensions
  // des disques à soustraire à l'attribut area pour le trouer selon
  // des cases du jeu dans le cas où le paramètre du constructeur
  // vaut 900 ;
  // ces constantes ont été estimées empiriquement lors du développement
  // de ce programme

  // rayon des disques
  private static final int RADIUS = 130;
  // abscisse du carré englobant le disque supérieur gauche
  private static final int X = 22;
  // ordonnée du carré englobant le disque supérieur gauche
  private static final int Y = 12;
  // décalage horizontal des disques
  private static final int DX = 146;
  // décalage vertical des disques
  private static final int DY = 147;

  // adresse de l'image destinée à remplir la zone représentant
  // la grille de jeu (avant redimentionnement, voir commentaire de
  // RESIZED_IMAGE_PATH)
  private static final String IMAGE_PATH = "./view/game.png";

  // adresse de l'image obtenue de celle d'adresse IMAGE_PATH
  // obtenue lors d'un appel au constructeur par le redimentionnement
  // (via la méthode resize de la classe statique ImageManager) sans
  // déformation correspondant à la hauteur passée en paramètre au
  // constructeur
  private static final String RESIZED_IMAGE_PATH = "./view/resized_game.png";

  // largeur de l'image avant redimentionnement
  private int imageWidth;

  // hauteur de l'image avant redimentionnement
  private int imageHeight;

  // largeur de l'image après redimentionnement
  private int resizedImageWidth;

  // hauteur de l'image après redimentionnement
  private int resizedImageHeight;

  // image redimentionnée
  private BufferedImage image;

  // zone représentant la grille de jeu
  private Area area;

  // protocole de dessin relatif à la zone représentant
  // la grille de jeu
  private TexturePaint texturePaint;

  // caractéristiques des disques à soustraire à l'attribut area
  // pour le trouer selon les cases du jeu
  private int radius; // rayon des disques
  private int x; // abscisse du carré englobant le disque supérieur gauche
  private int y; // ordonnée du carré englobant le disque supérieur gauche
  private int dx; // décalage horizontal des disques
  private int dy; // décalage vertical des disques



  // CONSTRUCTEUR



  public PaintableGrid(int height) {

    // pour stocker un rectangle de même dimensions et positions que celles
    // de l'image redimentionnée
    Shape imageRectangle;

    // initialisation des attributs imageWith et imageHeight;
    initialiseImageDimensions();

    // initialise la hauteur de l'image redimentionnée
    this.resizedImageHeight = height;

    // calcul de la largeur de l'image redimentionnée via la règle
    // de trois pour ne pas générer de déformation
    this.resizedImageWidth
      = theFourthProportionnal(
        this.imageHeight,
        this.resizedImageHeight,
        this.imageWidth
      );

    // redimentionnement et initialisation de l'image
    try {

      // redimentionnement de l'image
      ImageManager.resize(
        IMAGE_PATH,
        RESIZED_IMAGE_PATH,
        this.resizedImageWidth,
        this.resizedImageHeight
      );

      // initialisation de image
      this.image = null;
      image = ImageIO.read(new File(this.RESIZED_IMAGE_PATH));
      this.resizedImageWidth = image.getWidth();
    }

    // gestion de l'exception potentiellement levée lors de l'appel
    // à la méthode read de la classe ImageIO
    catch (IOException exception) {

      exception.printStackTrace();

    }

    // initialisation de imageRectangle par un rectangle de mêmes
    // dimensions que celles de image redimentionnée
    imageRectangle
      = new Rectangle2D.Double(
        0,
        0,
        this.resizedImageWidth,
        this.resizedImageHeight
      );

    // initialisation de l'attribut texturePaint par une instance de la classe
    // texturePaint représentant le protocole pour remplir la correspondance
    // graphique de la zone imageRectangle par l'image image
    this.texturePaint
      = new TexturePaint(image, imageRectangle.getBounds2D());

    // initialisation de la zone représentant la grille de jeu
    this.area = new Area(imageRectangle);

    // calcul des caractéristiques des disques à soustraire à l'attribut
    // area pour le trouer selon les cases du jeu
    initialiseDiskToBeSubtracted();

    // calcul de la zone représentant la grille de jeu par
    // soustration des disques occupant les cases de la grille
    subtractDisksToArea();

  }



  // MÉTHODES D'INITIALISATION



  // initialise les dimention de l'image
  private void initialiseImageDimensions() {

    BufferedImage image;

    try {
      image = ImageIO.read(new File(IMAGE_PATH));
      this.imageWidth = image.getWidth();
      this.imageHeight = image.getHeight();
    }

    catch (IOException exception) {
      exception.printStackTrace();
    }

  }

  // initialise les caractéristiques des disques à soustraire à l'attribut
  // area pour le trouer selon les cases du jeu
  private void initialiseDiskToBeSubtracted() {

    // rayon
    this.radius = theFourthProportionnal(
      this.imageHeight,
      this.resizedImageHeight,
      RADIUS
    );

    // coordonnée X
    this.x = theFourthProportionnal(
      this.imageHeight,
      this.resizedImageHeight,
      X
    );

    // coordonnée Y
    this.y = theFourthProportionnal(
      this.imageHeight,
      this.resizedImageHeight,
      Y
    );

    // décalage suivant coordonnée X
    this.dx = theFourthProportionnal(
      this.imageHeight,
      this.resizedImageHeight,
      DX
    );

    // décalage suivant coordonnée Y
    this.dy = theFourthProportionnal(
      this.imageHeight,
      this.resizedImageHeight,
      DY
    );

  }

  // soustrait les disques occupant les cases de la grille de
  // jeu à la zone la représentant
  private void subtractDisksToArea() {

    for (int i = 0 ; i < 7 ; i++) {
      for (int j = 0 ; j < 6 ; j++) {
        this.area.subtract(
          new Area(
            new Ellipse2D.Double(
              this.x + i * this.dx,
              this.y + j * this.dy,
              this.radius,
              this.radius
            )
          )
        );
      }
    }

  }

  // calcul la quatrième proportionnelle des trois arguments
  // passés (arrondie)
  private int theFourthProportionnal(int a1, int b1, int a2) {

    return (a2 * b1) / a1;

  }



  // MÉTHODE DE DESSIN



  // pour peindre l'instance via un objet de classe Graphics2D
  public void draw(Graphics2D graphics2D) {

    graphics2D.setPaint(this.texturePaint);
    graphics2D.fill(this.area);

  }



  // ASSESSEURS



  // appelé par le construceur de la classe Drawing pour ajuster
  // la largeur de ses instances
  public int getResizedImageWidth() {return this.resizedImageWidth;}

  // appellés par la méthode paintComponent(Graphics) de la classe
  // Drawing pour réaliser le dessin de la grille de jeu
  public TexturePaint getTexturePaint() {return this.texturePaint;}
  public Area getArea() {return this.area;}
  public int getX() {return this.x;};
  public int getY() {return this.y;};
  public int getDx() {return this.dx;};
  public int getDy() {return this.dy;};
  public int getRadius() {return this.radius;};



}
