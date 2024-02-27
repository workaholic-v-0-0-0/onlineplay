
package view;

// importations du paquage model
import model.CellState;
import model.ColumnNumber;

// importations du paquage java.util
import java.util.HashSet;
import java.util.HashMap;

// importations pour gérer les évènements
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

// importations pour gérer l'interface graphique
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.TexturePaint;
import java.awt.Rectangle;
import java.awt.geom.Area;

// importations pour gérer les images
import java.io.File;
import javax.imageio.ImageIO;

// importation d'une classe instanciant des déclencheurs
// pour gérer l'animation de la chute d'un jeton
import javax.swing.Timer;

// Panneau sur lequel on dessine le déroulement d'une partie de
// Puissance 4
public class Drawing
  extends JPanel
  implements ActionListener, MouseMotionListener {



  // ATTRIBUTS



  // dimensions
  private int width;
  private final static int HEIGHT = 600;

  // dictionnaire qui, à une couleur de cellule, associe l'instance
  // de classe Color correspondante
  private HashMap<CellState,Color> cellStateToColor;

  // dictionnaire qui, à une couleur de cellule, associe une chaîne de
  // caractères correspondante
  private HashMap<CellState,String> cellStateToColorAsString;

  // regroupe les données relatives à la zone regrésentant la grille du
  // jeu et à sa façon de la dessiner (via son attribut de classe TexturePaint)
  private PaintableGrid paintableGrid;

  // ensemble des objets dessinables, ie admettant une méthode
  // draw(Graphics2D) que la méthode paintComponent peut appeler
  // pour générer leur affichage ;
  // comme les éléments du dessin qui ne correspondent pas à la grille
  // de jeu ne se superposent pas, l'ordre dans lequel il sont dessinés
  // dans paintComponent n'est pas important ; et comme il sont à
  // dessiner derrière la grille, celle-ci est dessinée en dernier (dans
  // paintComponent)
  private HashSet<Drawable> setOfDrawableNotPaintableGrid;

  // ensemble des instances de la classe invisibleColumnAreas représentant
  // les zones rectangulaires des colonnes de la grille de jeu ;
  // les instances de la classe invisibleColumnAreas
  // n'implémentant pas l'interface Drawable, aucun affichage ne
  // correspond à ces instances
  private HashSet<InvisibleColumnArea> setOfInvisibleColumnAreas;

  // objet dessinable représentant le prochain coup si l'utilisateur clique
  // et si la colonne survolée par l'utilisateur n'est pas déjà remplie
  private PaintablePossibleNextToken paintablePossibleNextToken;

  // numéro de la colonne de la grille survolée par la souris
  private ColumnNumber overfliedColumnNumber;

  // pour déclencher et arrêter l'animation représentant la chute d'un
  // jeton joué
  private ListenableTimer fallingTokenTimer;

  // pointe sur l'instance de ColorableToken représentant le dernier jeton
  // joué ; permet d'identifier le jeton que l'on doit représenter en train
  // de chuter via l'animation déclenchée par l'attribut fallingTokenTimer
  private ColorableToken fallingToken;

  // indicateur de position de l'attribut fallingToken
  private int fallingTokenX; // coordonnée X
  private int fallingTokenY; // coordonnée Y
  private int finalFallingTokenY; // altitude final à la fin de la chute



  // CONSTRUCTEUR



  public Drawing() {

    // initialisation du déclencheur gérant l'animation de la chute d'un
    // jeton
    this.fallingTokenTimer = new ListenableTimer(1, this);

    // initialisation de l'attribut setOfDrawableNotPaintableGrid,
    // ensemble des objets à dessiner qui ne sont pas la grille de jeu
    // (instance de classe PpaintableGrid)
    this.setOfDrawableNotPaintableGrid = new HashSet<Drawable>();

    // règle des paramètres de l'instance via des méthodes des
    // classes dont sa classe hérite
    this.setBackground(Color.WHITE);
    this.paintableGrid = new PaintableGrid(HEIGHT);
    this.width = this.paintableGrid.getResizedImageWidth();
    this.setPreferredSize(
      new Dimension(
        this.width,
        Drawing.HEIGHT
      )
    );

    // initialise l'attribut cellStateToColor
    initialiseCellStateToColor();

    // initialise l'attribut cellStateToColorAsString
    initialiseCellStateToColorAsString();

    // initialise l'attribut setOfInvisibleColumnAreas
    initialiseSetOfInvisibleColumnAreas();

    // initialisation de paintablePossibleNextToken de façon à ce qu'il
    // soit "dessiné à l'extérieur de l'instance de Drawing" pour le rendre
    // invisible tant que l'utilisateur n'a pas déplacé la souris sur cette
    // instance de Drawing ;
    // adjonction de paintablePossibleNextToken à
    // setOfDrawableNotPaintableGriddes, l'ensemble des objets qui sont
    // dessinés avec PaintableGrid lors d'un appel à paintComponent
    initialisePaintablePossibleNextToken();

    // raffraichissement de l'affichage
    repaint();

  }



  // MÉTHODES D'INITIALISATION



  // initialise l'attribut cellStateToColor
  private void initialiseCellStateToColor() {

    this.cellStateToColor = new HashMap<CellState,Color>();
    this.cellStateToColor.put(
      CellState.RED,
      Color.RED
    );
    this.cellStateToColor.put(
      CellState.YELLOW,
      Color.YELLOW
    );
    // TO DEGUG
    this.cellStateToColor.put(
      CellState.TODEBUG,
      Color.PINK
    );

  }

  // initialise l'attribut cellStateToColorAsString
  private void initialiseCellStateToColorAsString() {

    this.cellStateToColorAsString = new HashMap<CellState,String>();
    this.cellStateToColorAsString.put(
      CellState.RED,
      "rouge"
    );
    this.cellStateToColorAsString.put(
      CellState.YELLOW,
      "jaune"
    );

  }

  // initialise l'attribut setOfInvisibleColumnAreas
  private void initialiseSetOfInvisibleColumnAreas() {

    // initialisation des attributs de la classe InvisibleColumnArea
    InvisibleColumnArea.setWidth(this.width / 7);
    InvisibleColumnArea.setHeight(this.HEIGHT);

    this.setOfInvisibleColumnAreas = new HashSet<InvisibleColumnArea>();

    for (ColumnNumber columnNumber : ColumnNumber.SET_OF_COLUMN_NUMBERS) {
      this.setOfInvisibleColumnAreas.add(
        new InvisibleColumnArea(columnNumber)
      );
    }

  }

  // initialise l'attribut paintablePossibleNextToken
  public void initialisePaintablePossibleNextToken() {

    this.paintablePossibleNextToken
      = new PaintablePossibleNextToken(
        -100,
        100,
        this.paintableGrid.getRadius(),
        this.HEIGHT
      );
    setOfDrawableNotPaintableGrid.add(paintablePossibleNextToken);

  }



  // MÉTHODE DE DESSIN



  @Override
  public void paintComponent(Graphics g) {

    // pour gérer le dessin des objets dont le nom de la classe
    // se termine par "2D"
    Graphics2D graphics2D;

    // appel à la méthode paintComponent de la classe mère
    super.paintComponent(g);

    // transtypage de l'instance g de Graphics en instance
    // de classe Graphics2D
    graphics2D = (Graphics2D) g;

    // boucle sur l'ensemble setOfDrawableNotPaintableGrid des objets
    // à dessiner qui ne sont pas paintableGrid pour les dessiner
    // tour à tour
    for (Drawable drawable : setOfDrawableNotPaintableGrid)
      drawable.draw(graphics2D);

    // dessine la grille de jeu en dernier pour la représenter
    // au premier plan
    paintableGrid.draw(graphics2D);

	}

  // retire l'attribut paintablePossibleNextToken (représentant le futur
  // jeton joué avec un point d'interrogation) de l'ensemble des objets
  // à dessiner ;
  // permet de cesser de rendre réactif le dessin du jeu aux actions des
  // utilisateurs (quand aucun utilisateur ne doit jouer)
  public void hidePaintablePossibleNextToken() {
    this.setOfDrawableNotPaintableGrid.remove(this.paintablePossibleNextToken);
    repaint();
  }

  // ajoute l'attribut paintablePossibleNextToken (représentant le futur
  // jeton joué avec un point d'interrogation) de l'ensemble des objets
  // à dessiner ;
  // permet de rendre réactif le dessin du jeu aux actions des
  // utilisateurs (quand un utilisateur doit jouer)
  public void showPaintablePossibleNextToken() {
    this.setOfDrawableNotPaintableGrid.add(this.paintablePossibleNextToken);
    repaint();
  }



  // MÉTHODES DE GESTION DES ÉVÈNEMENTS



  // IMPLÉMENTATION DE L'INTERFACE ActionListener

  // exécuté régulièremant entre un appel à start() et un appel à
  // la méthode stop() de l'attribut fallingTokenTimer ;
  // anime la chute d'un jeton joué
  public void actionPerformed(ActionEvent e) {

    this.fallingTokenY = this.fallingTokenY + 10;
    if (this.fallingTokenY > this.finalFallingTokenY) {
      this.fallingToken.moveTo(this.fallingTokenX, this.finalFallingTokenY);
      this.fallingTokenTimer.stop();
    }
    else {
      this.fallingToken.moveTo(this.fallingTokenX, this.fallingTokenY);
    }
    repaint();
  }

  // IMPLÉMENTATION DE L'INTERFACE MOUSEMOTIONLISTENER

  // si le suvol de la souris change de colonne de grille de jeu,
  // dessine l'eventuel prochain coup dans la nouvelle colonne survolée
  // si celle-ci n'est pas déjà remplie
  @Override
  public void mouseMoved(MouseEvent e) {

    // modification de paintablePossibleNextToken lorsque que le survol de
    // la souris change de colonne
    for (InvisibleColumnArea invisibleColumnArea :
        this.setOfInvisibleColumnAreas) {

      if (invisibleColumnArea.area.contains(e.getPoint())) {

        // mise à jour de overfliedColumnNumber en affectant cet attribut par
        // l'attribut columnNumber de l'instance de InvisibleColumnAreas
        // représentant la nouvelle colonne survolée ;
        // cette nouvelle valeur est donc le numéro de la nouvelle
        // colonne survolée
        this.overfliedColumnNumber = invisibleColumnArea.getColumnNumber();
        // "déplacement" de paintablePossibleNextToken représentant
        // l'hypothétique prochain coup
        this
          .paintablePossibleNextToken
          .moveTo(
            this.paintableGrid.getX()
              + (this.overfliedColumnNumber.toInt() - 1)
                * this.paintableGrid.getDx(),
            this.paintableGrid.getY()
              + (invisibleColumnArea.getNextLineNumber().toInt() - 1)
                * this.paintableGrid.getDy()
          );
        // rafraichissement de l'affichage
        repaint();

      }

    }

  }

  // le reste de l'interface MouseMotionListener n'est pas implémenté
  @Override
  public void mouseDragged(MouseEvent e) {}



  // ASSESSEURS



  public HashMap<CellState,Color> getCellStateToColor() {
    return this.cellStateToColor;
  }
  public HashMap<CellState,String> getCellStateToColorAsString() {
    return this.cellStateToColorAsString;
  }
  public PaintableGrid getPaintableGrid() {
    return this.paintableGrid;
  }
  public ListenableTimer getFallingTokenTimer() {return this.fallingTokenTimer;}
  //public ListenableBoolean getRunning() {return this.running;}
  public PaintablePossibleNextToken getPaintablePossibleNextToken() {
    return this.paintablePossibleNextToken;
  }
  public HashSet<Drawable> getSetOfDrawableNotPaintableGrid() {
    return this.setOfDrawableNotPaintableGrid;
  }
  public HashSet<InvisibleColumnArea> getSetOfInvisibleColumnAreas() {
    return this.setOfInvisibleColumnAreas;
  }
  public ColumnNumber getOverfliedColumnNumber() {return this.overfliedColumnNumber;}
  public ColorableToken getFallingToken() {return this.fallingToken;}
  public int getFallingTokenX() {return this.fallingTokenX;}
  public int getFallingTokenY() {return this.fallingTokenY;}
  public int getFinalFallingTokenY() {return this.finalFallingTokenY;}



  // MUTATEURS



  public void setFallingTokenX(int fallingTokenX) {
    this.fallingTokenX = fallingTokenX;
  }
  public void setFallingTokenY(int fallingTokenY) {
    this.fallingTokenY = fallingTokenY;
  }
  public void setFinalFallingTokenY(int finalFallingTokenY) {
    this.finalFallingTokenY = finalFallingTokenY;
  }
  public void setFallingToken(ColorableToken fallingToken) {
    this.fallingToken = fallingToken;
  }



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return super.toString();
  }



}
