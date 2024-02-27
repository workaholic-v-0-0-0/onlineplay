
package view;

// importation pour gérer les widgets de l'interface graphique
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.BorderLayout;

// importations de la classe java.util.EventListener
// et de classes du package java.awt.event
// pour gérer les évènements liées à l'intrface graphique, i.e. les
// évènements liés aux actions des utilisateurs
import java.util.EventListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// importations du package java.bean ;
// pour gérer les écoutes de changement de propriété liée
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

// importations du package model représentant le modèle
import model.Model;
import model.Cell;
import model.Coordinates;
import model.ColumnNumber;

// la vue ;
// la vue définit l’affichage : comment l’état du modèle est rendu
// visible à l’utilisateur ;
// la vue génère les évènements correspondant aux actions des
// utilisateurs ;
// ces évènements sont destinés à être écoutés par le contrôleur
// (instance de classe controller) ;
// le contrôleur peut faire des notifications à l'intention des
// utilisateurs via cette classe view ;
// en résumé, les deux cas dans lesquels une instance de la classe
// view change sont lors de ces denières notifications (commandées par
// le contrôleur) et des changements de propriétés liées du modèle
// instance de model) que la vue doit refléter à l'utilisateur ;
// ce deuxième type de changement se fait car la vue écoute le modèle,
// plus précisément, la classe vue implémente l'interface
// PropertyChangeListener et est réceptive aux évènements de la classe
// PropertyChangeEvent gérer par un attribut de la classe représentant
// chaque cellule de la grille de jeu (la classe Cell), un attribut de
// classe PropertyChangeSupport ;
// la vue est aussi réceptive à un autre changement de propriété
// correspondant à une demande de nouvelle partie d'un utilisateur

public class View
    extends JFrame
    implements PropertyChangeListener {



  // ATTRIBUTS



  // l'objet que l'instance de View reflète à l'utilisateur via l'affichage,
  // i.e. le modèle (il correspond à la grille de jeu)
  Model model;

  // attributs gérant la barre de menus

  // barre de menu
  private JMenuBar menuBar;
  // menu pour commencer une nouvelle partie
  private JMenu newGameMenu;
  // élément de menu pour commencer une nouvelle partie
  private JMenuItem newGameItem;
  // menu pour changer les paramètres
  private JMenu parameterMenu;
  // bouton radio de menu pour jouer contre l'ordinateur à la
  // prochaine partie
  private JRadioButtonMenuItem humanVsComputerRadioButtonMenuItem;
  // bouton radio de menu pour faire jouer un humain contre contre
  // un autre à la prochaine partie
  private JRadioButtonMenuItem humanVsHumanRadioButtonMenuItem;
  // groupe de boutons permettant de rendre exclusif le choix entre faire
  // jouer deux humains ou non à la prochaine partie
  private ButtonGroup opponentTypesButtonGroup;

  // bouton radio de menu pour que l'utilisateur commencer la prochaine
  // partie contre l'IA
  private JRadioButtonMenuItem humanBeginMenuItem;
  // bouton radio de menu pour laisser l'IA commencer la prochaine partie
  private JRadioButtonMenuItem aIBeginMenuItem;
  // groupe de boutons permettant de rendre exclusif le choix entre commencer
  // la prochaine partie ou non dans le cas ou le choix de jouer contre l'IA
  // a été fait ;
  // les bouton de ce groupe ne sont visible que si le choix susmentionné
  // a été fait
  private ButtonGroup humanBeginOrNotButtonGroup;

  // menu pour faire jouer l'ordinateiur contre lui-même
  private JMenu demonstrationMenu;
  // élément de menu pour commencer une nouvelle partie
  // où les deux joueurs sont des IA
  private JMenuItem aIVsAIItem;

  // conteneur principal
  JPanel wrapping;

  // zone de texte supportant les messages informatifs à
  // l'intention de l'utilisateur
  JLabel comments;

  // conteneur qui va supporter la représentation graphique
  // du jeu de puissance 4
  Drawing drawing;



  // CONSTRUCTEUR



  public View(Model model) {

    super("Puissance 4");

    this.model = model;

    menuBarInitialisation();

    // initialisation de l'attribut wrapping
    this.wrapping = new JPanel();
    this.wrapping.setLayout(new BorderLayout());
    this.wrapping.setBackground(Color.WHITE);

    // déclaration de wrapping en tant que conteneur principal
    this.setContentPane(wrapping);

    // initialisation de l'attribut comments
    this.comments = new JLabel();
    this.comments.setHorizontalTextPosition(SwingConstants.CENTER);
    this.comments.setText("");

    // initialisation de l'attribut drawing
    this.drawing = new Drawing();

    // emboîtement des composants
    this.wrapping.add(this.comments, BorderLayout.PAGE_START);
    this.wrapping.add(this.drawing, BorderLayout.CENTER);

    // règle des paramètres de l'instance via des méthodes des
    // classes dont sa classe hérite
    this.setLocation(1000, 0);
    this.setResizable(false);
	  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // assemble et rend visible l'instance
    this.pack();
    this.setVisible(true);

  }



  // MÉTHODES D'INITIALISATION



  // initialisation des attributs menuBar, newGameMenu, newGameItem,
  // parameterMenu,
  private void menuBarInitialisation() {

    // initialisation de l'attribut menuBar
    this.menuBar = new JMenuBar();

    // initialisation de l'attribut newGameMenu
    this.newGameMenu = new JMenu("nouveau");

    // initialisation de l'attribut newGameItem
    this.newGameItem = new JMenuItem("nouvelle partie");

    // initialisation de l'attribut parameterMenu
    this.parameterMenu = new JMenu("paramètres");

    // initialisation de l'attribut humanVsComputerRadioButtonMenuItem
    this.humanVsComputerRadioButtonMenuItem
      = new
        JRadioButtonMenuItem(
          "jouer contre l'IA à la prochaine partie",
          true
        );
    this.humanVsComputerRadioButtonMenuItem.addItemListener(
      new ItemListener() {
          public void itemStateChanged(ItemEvent e) {
            if (humanVsComputerRadioButtonMenuItem.isSelected()) {
              enableHumanBeginOrNotButtonGroup(true);
            }
            else {
              enableHumanBeginOrNotButtonGroup(false);
            }
          }
        }
    );

    // initialisation de l'attribut humanVsHumanRadioButtonMenuItem
    this.humanVsHumanRadioButtonMenuItem
      = new JRadioButtonMenuItem("jouer entre humains à la prochaine partie");

    // initialisation de l'attribut opponentTypesButtonGroup
    this.opponentTypesButtonGroup = new ButtonGroup();

    // initialisation de l'attribut humanBeginMenuItem
    this.humanBeginMenuItem
      = new
        JRadioButtonMenuItem(
          "commencer la prochaine partie",
          true
        );

    // initialisation de l'attribut AIBeginMenuItem
    this.aIBeginMenuItem
      = new
        JRadioButtonMenuItem(
          "laisser l'IA commencer la prochaine partie"
        );

    // initialisation de l'attribut humanBeginOrNotButtonGroup
    this.humanBeginOrNotButtonGroup = new ButtonGroup();

    // initialisation de l'attribut demonstrationMenu
    this.demonstrationMenu = new JMenu("démonstration");

    // initialisation de l'attribut aIVsAIItem
    this.aIVsAIItem
      = new JMenuItem("faire jouer l'ordinateur contre l'ordinateur");

    // assemblage et ajout de la barre de menus
    this.newGameMenu.add(this.newGameItem);
    this.menuBar.add(this.newGameMenu);
    this.opponentTypesButtonGroup.add(this.humanVsComputerRadioButtonMenuItem);
    this.opponentTypesButtonGroup.add(this.humanVsHumanRadioButtonMenuItem);
    this.humanBeginOrNotButtonGroup.add(this.humanBeginMenuItem);
    this.humanBeginOrNotButtonGroup.add(this.aIBeginMenuItem);
    this.parameterMenu.add(this.humanVsComputerRadioButtonMenuItem);
    this.parameterMenu.add(this.humanVsHumanRadioButtonMenuItem);
    this.parameterMenu.addSeparator();
    this.parameterMenu.add(this.humanBeginMenuItem);
    this.parameterMenu.add(this.aIBeginMenuItem);
    this.menuBar.add(this.parameterMenu);
    this.demonstrationMenu.add(this.aIVsAIItem);
    this.menuBar.add(this.demonstrationMenu);
    this.setJMenuBar(this.menuBar);

  }

  // pour cacher ou afficher le groupe de boutons relatif au choix de
  // l'utilisateur de commencer ou non la prochaine partie contre l'IA dans
  // le cas où celui-ci a sélectionné l'option de jouer contre l'IA à
  // la prochaine partie
  public void enableHumanBeginOrNotButtonGroup(boolean bool) {

    this.humanBeginMenuItem.setEnabled(bool);
    this.aIBeginMenuItem.setEnabled(bool);

  }



  // MÉTHODES UTILISÉES PAR LE CONTRÔLEUR POUR MODIFIER LA VUE



  // écrit un commentaire dans la partie supérieure de l'interface
  // graphique
  public void notification(String comments, boolean... completion) {

    if ((completion.length != 0) && (completion[0]))
      this.comments.setText(
        this.comments.getText()
        + " "
        + comments
      );
    else
      this.comments.setText(comments);

    // raffraichissement de l'affichage de l'instance de JLabel comments
    this.comments.paintImmediately(this.comments.getVisibleRect());

  }

  // rend le dessin de la grille de jeu réactive en affichant
  // un jeton avec un point d'interrogation représentant le prochain
  // jeton joué par l'utilisateur si celui-ci clique dans la colonne
  // de la grille de jeu
  public void becomeInteractive() {

    // fait s'écouter lui-même l'attribut drawing représentant le dessin
    // de la grille de jeu
    this
      .drawing
      .addMouseMotionListener(
        this
          .drawing
      );

    // ajoute l'objet représentant l'hypothétique prochain jeton
    // joué par un utilisateur dans l'ensemble des objets que l'attribut
    // drawing dessine lors de l'appel à la méthode paint
    this
      .drawing
      .showPaintablePossibleNextToken();

  }

  // cesse de rendre le dessin de la grille de jeu réactive en cachant
  // le jeton avec un point d'interrogation représentant le prochain
  // jeton joué par l'utilisateur si celui-ci clique
  public void stopBeingInteractive() {

    // cese de faire écouter lui-même l'attribut drawing représentant le dessin
    // de la grille de jeu
    this
      .drawing
      .removeMouseMotionListener(
        this
          .drawing
      );

    // enlève l'objet représentant l'hypothétique prochain jeton
    // joué par un utilisateur de l'ensemble des objets que l'attribut
    // drawing dessine lors de l'appel à la méthode paint
    this
      .drawing
      .hidePaintablePossibleNextToken();

  }



  // MÉTHODE DE CONNECTION AU MODÈLE



  // une instance de PropertyChangeEvent représente l'évènement d'un
  // changement de valeur d'une propriété liée ;
  // ici, il s'agit du changement d'une cellule de la grille de jeu
  // ou d'une demande de nouvelle partie d'un utilisateur
  public synchronized void propertyChange(PropertyChangeEvent evt)  {

      // si l'évènement correspond à un changement de valeur d'une
      // cellule de la grille de jeu
      if (evt.getSource() instanceof Cell) {

        // la cellule jouée
        Cell cell = (Cell) evt.getSource();

        // de dessin de la grille de jeu sans les jetons
        PaintableGrid paintableGrid = this.drawing.getPaintableGrid();

        // (ré)initialisation de l'instance de ColorableToken représentant le
        // dernier jeton joué (permet d'identifier le jeton que l'on doit
        // représenter en train de chuter via l'animation déclenchée par
        // l'attribut fallingTokenTimer) ;
        // on lui donne une position au dessus de la grille de jeu au niveau
        // de la colonne jouée
        this.drawing.setFallingTokenX(
          paintableGrid.getX()
            + (cell.getCoordinates().getX() - 1) * paintableGrid.getDx()
        );
        this.drawing.setFallingTokenY(
          - 100 // pour le mettre au-dessus de la grille
        );
        this.drawing.setFinalFallingTokenY(
          paintableGrid.getY()
            + (cell.getCoordinates().getY() - 1) * paintableGrid.getDy()
        );

        // adjonction à l'ensemble des éléments à dessiner sur drawing
        // qui ne sont pas la grille de jeu d'une instance de ColorableToken
        // représentant le nouveau jeton joué
        this.drawing.setFallingToken(
          new ColorableToken(
            this.drawing.getFallingTokenX(),
            this.drawing.getFallingTokenY(),
            paintableGrid.getRadius(),
            this.drawing.getCellStateToColor().get(cell.getCellState())
          )
        );
        this
          .drawing
          .getSetOfDrawableNotPaintableGrid()
          .add(
            this.drawing.getFallingToken()
          );

        // déclenchement l'animation de la chute du nouveau jeton
        this.drawing.getFallingTokenTimer().start();

        // notification à l'intention des utilisateurs pour anoncer quel
        // coup est joué (cette notification ne concerne que la vue, et non
        // le contrôlleur)
        notification(
          "Les "
          + this
            .drawing
            .getCellStateToColorAsString()
            .get(
              cell.getCellState()
            )
          + "s ont joué colonne "
          + cell.getCoordinates().getX()
          + "."
        );


        // mise à jour de l'attribut setOfInvisibleColumnAreas de
        // l'attribut Drawing() ;
        // on incrémente la hauteur du futur jeton joué dans la colonne
        // qui vient d'être jouée
        for (InvisibleColumnArea invisibleColumnArea :
            this.drawing.getSetOfInvisibleColumnAreas()) {

          if (invisibleColumnArea.getColumnNumber()
              == ColumnNumber.intToColumnNumber(
                  cell.getCoordinates().getX()
                )) {

            invisibleColumnArea.setNextLineNumber(
              invisibleColumnArea.getNextLineNumber().decrement()
            );

          }

        }

      }

      // si l'évènement correspond à une demande de nouvelle partie
      // d'un utilisateur
      if (evt.getSource() instanceof Model) {

        //
        MouseListener[] mouseListenerArray;

        // tableau des instances de classe PropertyChangeListener qui écoutent
        //
        PropertyChangeListener[] propertyChangeListenerArray;

        // le modèle (ce que doit refléter la vue via l'interface graphique)
        Model model = (Model) evt.getSource();

        // on enlève l'attribut représentant le dessin du jeu de l'interface
        // graphique
        this.wrapping.remove(this.drawing);

        // on récupère les instances de MouseListeners qui écoutent
        // l'attribut représentant le dessin du jeu
        mouseListenerArray = this.drawing.getMouseListeners();

        // on récupère dans un tableau les instances de propertyChangeListener
        // qui écoutent l'attribut représentant le dessin du jeu
        propertyChangeListenerArray
          = this
              .drawing
                .getFallingTokenTimer()
                .getObservable()
                .getPropertyChangeListeners();

        // on crée une nouvelle instance de classe Drawing sur laquelle
        // on fait pointer l'attribut drawing représentant le dessin du jeu ;
        // (le grabage-collector de Java détruit donc l'ancien objet de
        // cette classe puisque celui-ci n'a plus de référence)
        this.drawing = new Drawing();

        // on fait écouter cette nouvelle instance de classe Drawing (sur
        // laquelle pointe l'attribut drawing) par tous les écouteurs
        // précédement récupérés
        for (int i = 0 ; i < mouseListenerArray.length ; i++)
          this
            .drawing
            .addMouseListener(
              mouseListenerArray[i]
            );
        for (int i = 0 ; i < propertyChangeListenerArray.length ; i++)
          this
            .drawing
            .getFallingTokenTimer()
            .addPropertyChangeListener(
              propertyChangeListenerArray[i]
            );

        // on ajoute cette nouvelle instance de classe Drawing (sur
        // laquelle pointe l'attribut drawing) à l'interface graphique
        this.wrapping.add(this.drawing, BorderLayout.CENTER);

        // on assemble l'interface graphique
        this.pack();

      }

      // refraichissemnt de l'affichage
	    repaint();

  }



  // MÉTHODE DE CONNECTION AU CONTRÔLEUR



  // pour faire écouter la vue par le contrôleur
  public void listenedBy(EventListener controller) {

    // le contrôleur en tant qu'instance d'une classe implémentant
    // l'interface ActionListener ;
    // pour que le contrôleur écoute les sélections des utilisateurs
    // des items des menus correspondant à une demande de nouvelle partie
    // entre deux utilisateurs ou contre l'IA, ou bien une demande de
    // voir deux IA s'affronter l'une contre l'autre
    ActionListener actionListener = (ActionListener) controller;

    // le contrôleur en tant qu'instance d'une classe implémentant
    // l'interface MouseListener ;
    // pour que le contrôleur écoute la souris afin
    // d'en déduire quelle colonne veut jouer les utilisateurs
    MouseListener mouseListener = (MouseListener) controller;

    // le contrôleur en tant qu'instance d'une classe implémentant
    // l'interface PropertyChangeListener ;
    // pour que le contrôleur écoute l'arrêt du déclencheur
    // de classe ListenableTimer, attribut de l'attribut drawing
    // représentant le dessin du jeu, afin d'en déduire que l'animation
    // représentant la chute d'un jeton est terminé, et de permettre
    // à la partie de se dérouler en mettant les éventuelles IA à jour (en
    // fonction du dermier coup joué) puis en faisant jouer le prochain
    // joueur si c'est une IA ou en rendant le dessin du jeu sur l'interface
    // graphique (à nouveau) réactive au clic de la souris pour qu'
    // utilisateur puisse jouer le prochain coup
    PropertyChangeListener propertyChangeListener
      = (PropertyChangeListener) controller;

    // en résumé :

    // pour une demande de nouvelle partie contre l'IA ou entre deux
    // utilisateurs
    this.newGameItem.addActionListener(actionListener);

    // pour une demande de voir s'affronter deux IA
    this.aIVsAIItem.addActionListener(actionListener);

    // pour que les utilsateurs puissent jouer en cliquant sur le
    // dessin représentant le jeu
    this.drawing.addMouseListener(mouseListener);

    // pour que le contrôleur puisse continuer de gérer la partie à
    // la fin de l'animation de la chute d'un jeton
    this.drawing.getFallingTokenTimer().addPropertyChangeListener(
      (PropertyChangeListener) controller
    );

  }



  // ASSESSEURS



  public JRadioButtonMenuItem getHumanVsComputerRadioButtonMenuItem() {
    return this.humanVsComputerRadioButtonMenuItem;
  }
  public JRadioButtonMenuItem getHumanBeginMenuItem() {
    return this.humanBeginMenuItem;
  }
  public JLabel getComments() {return this.comments;}
  public Drawing getDrawing() {return this.drawing;}

}
