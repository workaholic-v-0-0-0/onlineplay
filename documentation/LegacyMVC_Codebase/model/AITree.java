
package model;

import java.util.HashMap;

public final class AITree {



  // ATTRIBUTS



  // parseur évolutif de grille représentant la grille de jeu
  // et ses données déduites constantes et évolutives correspondant
  // à la position courante (racine de l'instance en tant qu'arbre, et
  // non nécessairement la position courante ; en fait, une position
  // constituant l'une des branches dans une instance attribut
  // d'une instance de AIPlayer)
  private EvolutiveGridParser evolutiveGridParser;

  // couleur du joueur à qui c'est le tour dans la position
  // représentée par la racine de l'arbre
  private CellState playedAtRootColor;

  // distance de la racine jusqu'au feuille (profondeur)
  private int distanceFromLeaves;

  // dictionnaire qui, à une instance de ColumnNumber représentant
  // une colonne de la grille de jeu, associe l'arbre dont la
  // racine correspond à la position obtenue de celle correspondant
  // à l'attribut evolutiveGridParser en jouant dans cette colonne ;
  // c'est cet attribut qui confère à l'instance une structure d'arbre
  private HashMap<ColumnNumber,AITree> branches;

  // indique si l'arbre est une feuille, i.e. s'il n'est constitué
  // que de sa racine ;
  // c'est cas si la racine correspond à une position gagnante, une
  // partie nulle (plus de combinaison gagnante possible), ou s'il est
  // "situé" à une distance de la racine de l'attribut tree d'une instance
  // de classe AIPlayer (représentant un joueur IA) égale à l'attribut
  // DEPTH de ce-dernier (indiquant la profondeur de son arbre)
  private boolean endgameLeave;



  // CONSTRUCTEUR



  public AITree(
      EvolutiveGridParser evolutiveGridParser,
      CellState playedAtRootColor,
      int distanceFromLeaves) {

    // initialisation du parseur évolutif de grille représentant
    // la grille de jeu et les données déduites constantes et
    // évolutives correspondant à la position représentée par la
    // racine de l'arbre
    this.evolutiveGridParser = evolutiveGridParser;

    // initialisation de la couleur qui doit être jouée à la position
    // correspondant à la racine de l'arbre
    this.playedAtRootColor = playedAtRootColor;

    // initialisation de la profondeur de l'arbre
    this.distanceFromLeaves = distanceFromLeaves;

    // initialisation de l'attribut indiquant si l'arbre est une feuille
    this.endgameLeave
      = (
          // si combinaison gagnante au coup précédent
          (
            !
            this
              .evolutiveGridParser
              .getColorToArrayOfSetOfMonochromaticFourCellsInARow()
              .get(
                this
                  .playedAtRootColor
                  .theOtherColor()
                )
              .get(4)
              .isEmpty()
          )
        || // ou
          // s'il n'y a plus de combinaison gagnable (i.e. fin de
          // partie nulle)
          (
            this.evolutiveGridParser
              .getSetOfFourCellsInARow()
              .isEmpty()
          )
        );

    // maintenant que la racine est bien déterminée, on fait pousser
    // l'arbre jusqu'à sa dernière génération de branches
    growing(distanceFromLeaves);

  }



  // MÉTHODES D'INITIALISATION



  // initialisation de l'attribut branches ;
  // fait pousser l'instance d'autant de générations de branche
  // que la valeur du paramètre passé
  public void growing(int depth) {

    EvolutiveGridParser nextEvolutiveGridParser;

    // si l'arbre est une feuille
    if ((depth == 0) || (this.endgameLeave)) {

      this.branches = null;

    }

    // si l'arbre n'est pas une feuille
    else {

      this.branches = new HashMap<ColumnNumber,AITree>();

      for (ColumnNumber columnNumber : ColumnNumber.SET_OF_COLUMN_NUMBERS) {

        // si la colonne correspondant à columnNumber n'est pas pleine
        if (evolutiveGridParser.getColumnNumberToNextLineNumber(columnNumber)
            != LineNumber.OUT) {

          // initialisation de l'arbre associé à la colonne
          // correspondant à columnNumber

          // initialisation du parseur évolutif de grille, attribut
          // de l'arbre associé à la colonne correspondant à columnNumber,
          // par un clone du parseur évolutif de grille qui est attribut
          // de cette instance ;
          // on changera sa valeur à la prochaine étape en le mettant à
          // jour selon le coup dans la colonne correspondant à columnNumber
          nextEvolutiveGridParser
            =
            CloneWithSameCoordinatesFactory
              .cloneEvolutiveGridParser(
                this.evolutiveGridParser
              );

          // mise à jour du parseur évolutif de grille, attribut
          // de l'arbre associé à la colonne correspondant à columnNumber,
          // selon le coup dans la colonne correspondant à columnNumber
          nextEvolutiveGridParser.updateWithMove(
            columnNumber,
            playedAtRootColor
          );

          // adjonction de l'association faisant correspondre columnNumber
          // au parseur évolutif de grille représentant la position obtenue
          // depuis la position représentée par l'attribut evolutiveGridParser
          // de cette instance en jouant dans la colonne correspondant
          // à columnNumber
          this.branches.put(
            columnNumber,
            new AITree(
              nextEvolutiveGridParser,
              playedAtRootColor.theOtherColor(),
              depth - 1
            )
          );

        }

      }

    }

  }



  // ASSESSEURS



  public EvolutiveGridParser getEvolutiveGridParser() {
    return this.evolutiveGridParser;
  }

  public CellState getPlayedAtRootColor() {
    return this.playedAtRootColor;
  }

  public HashMap<ColumnNumber,AITree> getBranches() {
    return this.branches;
  }

  public int getDistanceFromLeaves() {
    return this.distanceFromLeaves;
  }



  // MUTATEURS



  public void setEvolutiveGridParser(EvolutiveGridParser evolutiveGridParser) {
    this.evolutiveGridParser = evolutiveGridParser;
  }

  public void incrementDistanceFromLeaves () {
    this.distanceFromLeaves++;
  }



  // MÉTHODES DE DÉBOGAGE



  public void showAITree(AIPlayer aIPlayer) {

    this.evolutiveGridParser.display(
      "dist = "
      + this.distanceFromLeaves
      + ", "
      + "rootColor = "
      + this.playedAtRootColor
      + ", "
      + "end = "
      + this.endgameLeave
      + ", "
      + "eval = "
      + aIPlayer
          .minMaxEvaluation(
            this
          )
    );

    if (this.branches != null) {

      for (ColumnNumber columnNumber : this.branches.keySet()) {

        this.branches.get(columnNumber).showAITree(aIPlayer);

      }

    }

  }

}
