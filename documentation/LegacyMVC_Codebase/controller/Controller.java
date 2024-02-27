
package controller;

// le contrôleur ;
// il définit la manière dont l’état du modèle change : c’est ce qui
// fait le lien entre la vue et le modèle.
// le contrôleur effectue les changements dans le modèle
// en écoutant certains évènements de la vue ;
// la vue écoute certains changements du modèle (la couleurs des
// cellules de la grille de jeu) pour le refléter
// à l'utilisateur mais le contrôleur peut aussi modifier
// directement la vue pour écrire des notification à
// l'intention des utilisateurs ou pour la rendre ou non réactive
// à la souris pour permettre aux utilisateurs de jouer un jeton
// dans une colonne

// extrait du cours "Écrivez du code Java maintenable avec MVC et SOLID"
// sur openclassroom : "Le contrôleur englobe la gestion du flux de
// l'application. Il est le chef d’orchestre des interactions entre
// l'utilisateur et le système. L'utilisateur interagit avec la vue,
// qui interagit ensuite avec le contrôleur. Le contrôleur prend les
// décisions : il apporte les modifications nécessaires aux objets du
// modèle, en crée de nouveaux ou supprime ceux qui ne sont plus utiles.
// Dans l'exemple du restaurant, le contrôleur correspondrait à l'ensemble
// des règles définies par le propriétaire pour s’occuper d'un client"

// permet de faire le lien entre la vue et le modèle lorsqu'une action
// utilisateur est intervenue sur la vue. C'est cet objet qui aura pour
// rôle de contrôler les données.

// c'est une classe de contrôle qui contient l'ensemble des listeners et qui,
// lorsque des événements parviennent via la vue, prévient le modèle
// en conséquence.

// importation de classes du package java.awt.event
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// importation de classes du package java.beans
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

// importation de classes du package model
import model.Model;
import model.EvolutiveGridParser;
import model.AbstractPlayer;
import model.AIPlayer;
import model.CellState;
import model.ColumnNumber;
import model.LineNumber;

// importation de classes du package view
import view.View;
import view.PaintablePossibleNextToken;



public class Controller
  implements
    MouseListener,
    ActionListener,
    PropertyChangeListener {



  // ATTRIBUTS



  // le modèle
  private Model model;

  // la vue
  private View view;

  // indique si une partie est en cours
  private boolean aGameIsPlaying;

  // indique si on attend qu'un utilisateur joue (en écoutant la vue)
  private boolean aUserCanPlay;

  // stocke l'instance de ColumnNumber représentant la dernière
  // colonne jouée
  private ColumnNumber playedColumnNumber;



  // CONSTRUCTEUR



  public Controller(Model model, View view) {

    // initialisation des attributs représentant le modèle et la vue
    this.model = model;
    this.view = view;

    // initialisation de l'attribut aGameIsPlaying avec la valeur
    // false pour signifier qu'aucune partie n'est en cours
    this.aGameIsPlaying = false;

    // on rend la partie de l'interface graphique représentant la
    // grille de jeu non réactive puisqu'aucune partie n'est en cours
    this.view.stopBeingInteractive();

    // on écrit une notification sur l'interface graphique invitant
    // les utilisateurs à choisir un type de partie via la
    // barre de menu pour en commencer une
    this
      .view
      .notification(
        "Bonjour. Veuillez faire votre choix via la barre de menu ci-dessus."
      );

  }



  // MÉTHODES DE MODIFICATION DU MODÈLE



  // fait jouer un joueur humain ou IA et met le modèle à jour
  // en fonction de ce coup sauf en ce qui concerne les joueurs
  // IA ; ces derniers seront mis à jour en fonction du coup joué
  // à l'arrêt de l'animation représentant la chute du jeton joué
  private void makeAPlayerPlayAndUpdateModelExceptAIs() {

    // le joueur qui joue actuellement
    AbstractPlayer player;

    // la cellule de la grille de jeu qui est actuellement
    // jouée
    CellState playedColor;

    // pour stocker le parseur évolutif relatif à la grille de
    // jeu réelle (et non ceux relatifs aux parseurs virtuels
    // constituant les arbres des joueurs IA)
    EvolutiveGridParser evolutiveGridParser;

    // le joueur qui joue actuellement
    player
      = this
        .model
        .getNextTurnPlayer();

    // la cellule de la grille de jeu qui est actuellement
    // jouée
    playedColor
      = this
        .model
        .getNextTurnPlayer()
        .getColor();

    // pour stocker le parseur évolutif relatif à la grille de
    // jeu réelle (et non ceux relatifs aux parseurs virtuels
    // constituant les arbres des joueurs IA)
    evolutiveGridParser
      = this
          .model
          .getEvolutiveGridParser();

    // on rend la partie de l'interface graphique représentant la
    // grille de jeu non réactive puisque, un coup viennant d'être choisi
    // par une joueur humain ou IA, cette fonctionnalité doit être
    // suspendue pour ne pas polluer l'affichage au moins le temps de
    // l'animation représentant la chute du jeton et au moins jusqu'à
    // ce qu'un jour humain doit jouer
    this
      .view
      .stopBeingInteractive();

    // si le joueur qui joue actuellement est une IA
    if (player instanceof AIPlayer) {

      // on notifie sur la vue qu'une IA cherche un coup à jouer
      this
        .view
        .notification(
          "Une IA cherche un coup à jouer...",
          true
        );

      // on affecte l'attribut playedColumnNumber par l'instance
      // de la classe ColumnNumber représentant la colonne choisie
      // par le joueur IA
      this.playedColumnNumber = ((AIPlayer) player).wantedColumn();

    }

    // si le joueur qui joue actuellement est un utilisateur
    else

    // on affecte l'attribut playedColumnNumber par l'instance
    // de la classe ColumnNumber représentant la colonne choisie
    // par le joueur utilisateur, i.e. celle survolée par la souris
      this.playedColumnNumber
        = this
            .view
            .getDrawing()
            .getOverfliedColumnNumber();

    // modification de la grille de jeu et des données évolutives
    // déduites représentant le coup joué dans la colonne
    // représentée par playedColumnNumber par le joueur player
    evolutiveGridParser
      .updateWithMove(
        this.playedColumnNumber,
        playedColor
      );

    // si le dernier coup joué est gagnant
    if (!
        evolutiveGridParser
          .getColorToArrayOfSetOfMonochromaticFourCellsInARow()
          .get(playedColor)
          .get(4)
          .isEmpty()) {

      // mise à jour de l'attribut aGameIsPlaying pour signifier
      // que plus aucune partie n'est en cours
      aGameHasJustEnded();

      // on cesse de rendre réctive la partie de l'interface graphique
      // représentant la grille de jeu puisque plus aucune partie
      // n'est en cours
      this
        .view
        .stopBeingInteractive();

      // on notifie via la vue le joueur vainqueur
      this
        .view
        .notification(
          playedColor
          + " a gagné la partie."
        );

    }

    // si la partie est nulle car il n'y a plus de
    // combinaisons gagnables
    else if (
        evolutiveGridParser
        .getSetOfFourCellsInARow()
        .isEmpty()) {

      // mise à jour de l'attribut aGameIsPlaying pour signifier
      // que plus aucune partie n'est en cours
      aGameHasJustEnded();

      // on cesse de rendre réctive la partie de l'interface graphique
      // représentant la grille de jeu puisque plus aucune partie
      // n'est en cours
      this
        .view
        .stopBeingInteractive();

      // on notifie via la vue que la partie est nulle
      this
        .view
        .notification(
          "Plus aucune combinaison n'est gagnable. La partie est nulle."
        );

  }

    // mise à jour de l'attribut du modèle représentant le
    // joueur qui doit jouer le prochain coup
    this.model.switchNextTurnPlayer();

  }

  // met à jour les arbres des joueurs IA en fonction du dernier
  // coup joué (stocké via l'attribut playedColumnNumber)
  private void AIsUpdate() {

    // si le joueur qui a commencé la partie est une IA
    if (this.model.getBeginningPlayer() instanceof AIPlayer) {

      // on notifie via la vue que le joueur qui a commencé la partie
      // (qui est une IA) est en train de mettre à jour son arbre
      // des parties possibles
      this
        .view
        .notification(
          "Une IA prend connaissance de ce coups.",
          true
        );

      // mise à jour de l'arbre du joueur qui a commencé la partie
      // (qui est une IA)
      ((AIPlayer)
      this
        .model
        .getBeginningPlayer())
        .updateWithMove(
          this.playedColumnNumber
        );

    }

    // si le joueur qui n'a pas commencé la partie est une IA
    if (this.model.getNotBeginningPlayer() instanceof AIPlayer) {

      // on notifie via la vue que le joueur qui n'a pas commencé la partie
      // (qui est une IA) est en train de mettre à jour son arbre
      // des parties possibles
      this
        .view
        .notification(
          "Une IA prend connaissance de ce coup.",
          true
        );

      // mise à jour de l'arbre du joueur qui n'a pas commencé la partie
      // (qui est une IA)
      ((AIPlayer)
      this
        .model
        .getNotBeginningPlayer())
        .updateWithMove(
          this.playedColumnNumber
        );

    }

  }



  // ASSESSEURS ET MUTATEURS PRIVÉS RELATIFS À DES CHANGEMENT DE PHASE



  // met à jour l'attribut aGameIsPlaying pour signifier
  // qu'une partie est en cours
  private void aGameHasJustStarted() {this.aGameIsPlaying = true;}

  // met à jour l'attribut aGameIsPlaying pour signifier
  // que plus aucune partie n'est en cours
  private void aGameHasJustEnded() {this.aGameIsPlaying = false;}

  // met à jour l'attribut aUserCanPlay pour signifier
  // qu'un utilisateur doit jouer le prochain coup
  private void aUserCanPlay() {
      this.aUserCanPlay = true;
      this.view.becomeInteractive(); // mise à jour de la vue
  }

  // met à jour l'attribut aUserCanPlay pour signifier
  // qu'un joueur IA doit jouer le prochain coup
  private void noUserCanPlay() {
      this.aUserCanPlay = false;
      this.view.stopBeingInteractive(); // mise à jour de la vue
  }



  // MÉTHODES DE GESTIONS DES ÉVÈNEMENTS DE LA VUE


  // GESTION DES DEMANDES DE JOUER DANS UNE COLONNE
  // DES JOUEURS HUMAINS VIA L'INTERFACE GRAPHIQUE
  // (implémentation de l'interface MouseListener)



  public void mouseClicked(MouseEvent e) {

    // colonne de la grille de jeu survolée par la souris
    ColumnNumber overfliedColumnNumber;

    // ligne dans laquelle on peut mettre un jeton
    // dans la colonne de la grille de jeu survolée par la souris
    LineNumber nextAltitudeLine = LineNumber.OUT;

    // si une partie est en cours et si l'interface
    // est réceptive aux actions de l'utilisateur
    if ((this.aGameIsPlaying) && (this.aUserCanPlay)) {

      // colonne de la grille de jeu survolée par la souris
      overfliedColumnNumber
        = this
            .view
            .getDrawing()
            .getOverfliedColumnNumber();

      // si le joueur à qui c'est le tour de jouer est une IA
      if (this.model.getNextTurnPlayer() instanceof AIPlayer) {

        // informe l'utilisateur que ce n'est pas à lui de
        // jouer en écrivant une notification sur l'interface graphique
        this
          .view
          .notification(
            "C'est à l'IA de jouer."
          );

      }

      // si le joueur à qui c'est le tour de jouer est un
      // utilisateur
      else {

        // si la colonne de la grille de jeu survollée par la souris
        // est pleine
        if (model
              .getEvolutiveGridParser()
              .getColumnNumberToNextLineNumber(
                overfliedColumnNumber
              )
            ==
            LineNumber.OUT) {

          // informe l'utilisateur que la colonne qu'il cherche
          // à jouer est pleine
          this
            .view
            .notification(
              "Cette colonne est pleine."
            );

        }

        // si la colonne de la grille de jeu survollée par la souris
        // n'est pas pleine (et que ce n'est pas à une IA de jouer)
        else {

          // un utilisateur joue dans la colonne overfliedColumnNumber

          // interruption de l'affichage du jeton représentant un coup
          // potentiel
          this
            .view
            .stopBeingInteractive();

          // on fait jouer l'utilisateur en mettant à jour
          // l'attribut evolutiveGridParser du modèle (model)
          // représentant la grille de jeu et les données
          // constantes et évolutives déduites ;
          // la vue se mettra à jour à l'issue d'une
          // animation traduisant la chute du jeton joué en
          // captant un évènement émis par un changement de valeur
          // de l'attribut evolutiveGridParser du modèle (model)
          makeAPlayerPlayAndUpdateModelExceptAIs();

        }

      }

    }

  }

  // les méthodes de l'interface MouseListener qui ne sont pas
  // mouseClicked(MouseEvent) ne sont pas implémentées
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}



  // GESTION D'UNE DEMANDE DE NOUVELLE PARTIE PAR
  // UN UTILISATEUR VIA L'INTERFACE GRAPHIQUE
  // (implémentation de l'interface ActionListener)



  public void actionPerformed(ActionEvent e) {

    // indique si les deux advercaires de la nouvelle partie
    // sont tous les deux des IA
    boolean demonstration;

    // met à jour l'attribut aGameIsPlaying pour signifier
    // qu'une partie est en cours
    aGameHasJustStarted();

    // indique si les deux advercaires de la nouvelle partie
    // sont tous les deux des IA
    demonstration
      = (e
          .getActionCommand()
          .equals("faire jouer l'ordinateur contre l'ordinateur"));

    // initialisation du parseur évolutif de grille et des joueurs
    // pour préparer une nouvelle partie en précédant cette
    // initialisation d'une notification pour en informer les
    // utilisateurs via la vue
    this
      .view
      .notification(
        "Réinitialisation de la grille de jeu..."
      );
    this.model.reset(
      this.view.getHumanVsComputerRadioButtonMenuItem().isSelected(),
      this.view.getHumanBeginMenuItem().isSelected(),
      demonstration
    );

    // si le joueur qui doit commencer la patie est une IA
    if (this.model.getNextTurnPlayer() instanceof AIPlayer) {

      // si le joueur qui doit jouer en premier est une IA,
      // on le fait jouer ;
      // le contrôleur orchestrera les prochains tours des joueurs
      // non humain lors de captage d'évènements de classe
      // PropertyChangeEvent indiquant la fin des animations
      // représentant la chute d'un jeton ;
      // ces dernières orchestrations sont donc traitées dans
      // la méthode propertyChange(PropertyChangeEvent)

      // on fait jouer le joueur IA en mettant à jour
      // l'attribut evolutiveGridParser du modèle (model)
      // représentant la grille de jeu et les données
      // constantes et évolutives déduites ;
      // la vue se mettra à jour à l'issue d'une
      // animation traduisant la chute du jeton joué en
      // captant un évènement émis par un changement de valeur
      // de l'attribut evolutiveGridParser du modèle (model)

      // on rend la partie de l'interface graphique représentant la
      // grille de jeu non réactive puisque c'est à un joueur IA
      // de jouer le prochain coup
      this
        .view
        .stopBeingInteractive();

      // on notifie via la vue quelle couleur doit jouer le
      // prochain coup
      this
        .view
        .notification(
          this
            .model
            .getNextTurnPlayer()
            .getColor()
          + " doit jouer."
        );

      // on fait jouer le joueur IA en mettant à jour
      // l'attribut evolutiveGridParser du modèle (model)
      // représentant la grille de jeu et les données
      // constantes et évolutives déduites ;
      // la vue se mettra à jour à l'issue d'une
      // animation traduisant la chute du jeton joué en
      // captant un évènement émis par un changement de valeur
      // de l'attribut evolutiveGridParser du modèle (model)
      makeAPlayerPlayAndUpdateModelExceptAIs();

    }

    // si le joueur qui doit commencer la patie est un utilisateur
    else {

      // met à jour l'attribut aUserCanPlay pour signifier
      // qu'un utilisateur doit jouer le prochain coup
      aUserCanPlay();

      // on notifie via la vue quelle couleur doit jouer le
      // prochain coup
      this
        .view
        .notification(
          this
            .model
            .getNextTurnPlayer()
            .getColor()
          + " doit jouer."
        );

    }

  }



  // GESTION DES TOURS DE JEU DES JOUEURS NON HUMAIN (IA)
  // VIA L'ÉCOUTE DU BEAN fallingTokenTimer DE CLASSE
  // ListenableTimer (HÉRITANT DE LA CLASSE TIMER) ÉMETTANT
  // UN ÉVÈNEMENT DE CLASSE PropertyChangeEvent À L'APPEL
  // DE LA MÉTHODE stop() (RÉIMPLÉMENTÉE ET APPELANT SON
  // HOMONYME DE LA CLASSE Timer) POUR SIGNIFIER LA FIN DE
  // L'ANIMATION DE LA CHUTE D'UN JETON MARQUANT LA FIN
  // DU TOUR DU JOUEUR PRÉCÉDENT
  // (implémentation de l'interface PropertyChangeListener)



  public void propertyChange(PropertyChangeEvent evt)  {

    // si une partie est en cours
    if (this.aGameIsPlaying) {

      // mise à jour des joueurs AI (i.e. modification de leur
      // arbres des parties possibles) précédée par une notification
      // de la vue de cette action
      // (on rappelle que cette méthode est appelée à la fin de
      // l'animation représentant la chute du dernier jeton joué)
      AIsUpdate();

      // si le joueur qui doit joueur le prochain coup est une IA
      // on fait jouer les joueurs qui doivent jouer les prochains coups
      // tant que ce sont des joueurs non humain (IA) ;
      // il est inutile de faire une boucle while car cette méthode est
      // de fait à nouveau appelée à la fin de l'animation représentant
      // la chute d'un jeton si le joueur suivant n'est pas humain
      if (this.model.getNextTurnPlayer() instanceof AIPlayer) {

        // on fait jouer le joueur IA en mettant à jour
        // l'attribut evolutiveGridParser du modèle (model)
        // représentant la grille de jeu et les données
        // constantes et évolutives déduites ;
        // la vue se mettra à jour à l'issue d'une
        // animation traduisant la chute du jeton joué en
        // captant un évènement émis par un changement de valeur
        // de l'attribut evolutiveGridParser du modèle (model)
        makeAPlayerPlayAndUpdateModelExceptAIs();

      }

      // si le joueur qui doit joueur le prochain coup est
      // un utilisateur
      else {

        // met à jour l'attribut aUserCanPlay pour signifier
        // qu'un utilisateur doit jouer le prochain coup
        aUserCanPlay();

        // on notifie via la vue quelle couleur doit jouer le
        // prochain coup
        this
          .view
          .notification(
            this
              .model
              .getNextTurnPlayer()
              .getColor()
            + " doit jouer.",
            true
          );

      }

    }

  }

}
