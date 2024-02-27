
package model;

import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

// le modèle ;
// le modèle regroupe l'ensemble des données relatives à la grille de jeu,
// à ses données déduites, et aux joueurs ;
// le modèle est écouté par la vue afin de le refléter à l'utilisateur
// via une interface graphique
public final class Model {



  // ATTRIBUTS



  // couleur de joueur qui commence la partie
  private static final CellState BEGINNING_COLOR = CellState.RED;

  // couleur de joueur qui ne commence pas la partie
  private static final CellState NOT_BEGINNING_COLOR = CellState.YELLOW;

  // joueur qui commence la partie
  private AbstractPlayer beginningPlayer;

  // joueur qui ne commence pas la partie
  private AbstractPlayer notBeginningPlayer;

  // joueur qui doit jouer le prochain coup
  private AbstractPlayer nextTurnPlayer;

  // pour faire observer par la vue une éventuelle réinitialisation
  // traduisant une nouvelle partie
  private PropertyChangeSupport evolutiveGridParserResetObservable;

  // gère les données évolutives de la grille de jeu et hérite de la
  // classe ConstantGridParser qui gère les données constantes de la
  // grille de jeu et qui hérite à son tour de la classe Grid qui ne
  // traite que des données directement liées aux cases de la grille
  private EvolutiveGridParser evolutiveGridParser;



  // CONSTRUCTEUR



  public Model() {

    // initialisation de l'attribut evolutiveGridParserResetObservable
    this.evolutiveGridParserResetObservable = new PropertyChangeSupport(this);

    // initialisation de l'attribut evolutiveGridParser
    this.evolutiveGridParser
      = new EvolutiveGridParser();

  }



  // MÉTHODES D'INITIALISATION



  // initialise le parseur évolutif de grille evolutiveGridParser
  // représentant la grille de jeu et ses données déduites ; initialise
  // les attributs représentant les deux joueurs
  public synchronized void reset(
      boolean humanVsComputer,
      boolean humanBegin,
      boolean aIVsAI) {

    // réinitialisation de l'attribut evolutiveGridParserReset en en informant
    // les écouteurs de changement de propriété liée associés au support de
    // changement de propriété liée evolutiveGridParserResetObservable

    // récupère les tableau des écouteurs de changement de propriété liée
    // associés au support de changement de propriété liée
    // evolutiveGridParserResetObservable
    PropertyChangeListener[] propertyChangeListenerArray
      = this.evolutiveGridParserResetObservable.getPropertyChangeListeners();

    // pour cesser de faire écouter les états des cellules
    // de l'ancienne grille par les écouteurs passés en paramètre
    for (int i = 0 ; i < propertyChangeListenerArray.length ; i++)
      for (Cell cell : this.evolutiveGridParser)
        cell
          .getObservable()
          .removePropertyChangeListener(
            propertyChangeListenerArray[i]
          );

    // mémorisation de l'ancienne valeur de l'attribut
    // evolutiveGridParser
    EvolutiveGridParser oldEvolutiveGridParser = this.evolutiveGridParser;

    // réinitialisation de l'attribut evolutiveGridParser
    this.evolutiveGridParser = new EvolutiveGridParser();

    // informe les écouteurs du changement de valeur de l'attribut
    // evolutiveGridParser
    this.evolutiveGridParserResetObservable.firePropertyChange(
        "EvolutiveGridParserReset",
        oldEvolutiveGridParser,
        this.evolutiveGridParser
    );

    // pour faire écouter les états des cellules de la nouvelle
    // grille par l'écouteur
    for (int i = 0 ; i < propertyChangeListenerArray.length ; i++)
      for (Cell cell : this.evolutiveGridParser)
        cell
          .getObservable()
          .addPropertyChangeListener(
            propertyChangeListenerArray[i]
          );

    // si deux joueur IA doivent s'affronter à la prochaine partie
    if (aIVsAI) {

      // initialisation de l'attribut beginningPlayer représentant
      // le joueur qui commence la partie
      this.beginningPlayer
        = new
          AIPlayer(
            BEGINNING_COLOR,
            this.evolutiveGridParser,
            BEGINNING_COLOR
          );

      // initialisation de l'attribut notBeginningPlayer représentant
      // le joueur qui ne commence pas la partie
      this.notBeginningPlayer
        = new
          AIPlayer(
            NOT_BEGINNING_COLOR,
            this.evolutiveGridParser,
            BEGINNING_COLOR
          );

    }

    // si la prochaine partie n'est pas disputée par deux joueurs IA
    else {

      // réinitialisation (ou initialisation) des attributs pointant des
      // objets de la classe AbstractPlayer représentant les deux adversaires
      // de la nouvelle partie et le joueur qui doit jouer le prochain coup
      // (donc à ce stade celui qui va commencer la partie)

      // si un utilisateur affronte un joueur IA à la prochaine partie
      if (humanVsComputer) {

        // si l'utilisateur commence la partie
        if (humanBegin) {

          // initialsation des attributs représentant les joueurs
          this.beginningPlayer = new HumanPlayer(BEGINNING_COLOR);
          this.notBeginningPlayer
            = new
              AIPlayer(
                NOT_BEGINNING_COLOR,
                this.evolutiveGridParser,
                BEGINNING_COLOR
              );

        }

        // si l'utilisateur ne commence pas la partie
        else {

          // initialsation des attributs représentant les joueurs
          this.beginningPlayer
            = new
              AIPlayer(
                BEGINNING_COLOR,
                this.evolutiveGridParser,
                BEGINNING_COLOR
              );
          this.notBeginningPlayer = new HumanPlayer(NOT_BEGINNING_COLOR);

        }

      }

      // si deux utilisateurs doivent s'affronter à la prochaine partie
      else {

        // initialsation des attributs représentant les joueurs
        this.beginningPlayer = new HumanPlayer(BEGINNING_COLOR);
        this.notBeginningPlayer = new HumanPlayer(NOT_BEGINNING_COLOR);

      }

    }

    // initialsation de l'attribut nextTurnPlayer représentant le joueur
    // qui doit jouer le prochain coup
    this.nextTurnPlayer = this.beginningPlayer;

  }



  // MÉTHODE DE CONNECTION À LA VUE



  // fait observer par le paramètre les changements de valeurs
  // de la propriété liée cellState de chaque cellule et de la
  // propriété liée evolutiveGridParser
  public void observedBy(PropertyChangeListener view) {

    // pour faire écouter les états des cellules par l'écouteur
    // passé en paramètre
    for (Cell cell : this.evolutiveGridParser)
      cell.getObservable().addPropertyChangeListener(view);

    // pour faire écouter une éventuelle réinitialisation de
    // l'attribut EvolutiveGridParser traduisant une nouvelle
    // partie par l'écouteur passé en paramètre
    this.evolutiveGridParserResetObservable.addPropertyChangeListener(view);

  }



  // MÉTHODES POUR SE RENDRE OBSERVABLE PAR LES ÉCOUTEURS



  public synchronized void addPropertyChangeListener(
      PropertyChangeListener listener) {
    this
      .evolutiveGridParserResetObservable
      .addPropertyChangeListener(listener);
  }

  public synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    this
      .evolutiveGridParserResetObservable
      .removePropertyChangeListener(listener);
  }



  // MÉTHODES DIVERSES



  // change la valeur de l'attribut nextTurnPlayer représentant le prochain
  // joueur qui doit jouer le prochain coup
  public void switchNextTurnPlayer() {
    this.nextTurnPlayer
      = (this.nextTurnPlayer == this.beginningPlayer) ?
        this.notBeginningPlayer
        :
        this.beginningPlayer;
  }



  // ASSESSEURS



  public AbstractPlayer getBeginningPlayer() {
    return this.beginningPlayer;
  }

  public AbstractPlayer getNotBeginningPlayer() {
    return this.notBeginningPlayer;
  }

  public AbstractPlayer getNextTurnPlayer() {
    return this.nextTurnPlayer;
  }

  public EvolutiveGridParser getEvolutiveGridParser() {
    return this.evolutiveGridParser;
  }



  //MUTATEURS



  public void setNextTurnPlayer(AbstractPlayer nextTurnPlayer) {
    this.nextTurnPlayer = nextTurnPlayer;
  }



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return
      super.toString();
  }


}
