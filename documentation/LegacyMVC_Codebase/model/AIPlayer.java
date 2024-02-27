
package model;

import java.util.HashSet;
import java.util.ArrayList;
import java.lang.Math;

// importation utile seulement pour une méthode de débogage
import java.util.Random;

// représente un joueur de type intelligence artificielle
public final class AIPlayer extends AbstractPlayer {



  // ATTRIBUTS



  // constante représentant l'infini ;
  // c'est un très grand nombre inégalable que l'algorithme min-max
  // accordera à des positions de victoires ;
  // son opposé représentera l'évaluation d'une position de défaite
  private static final double INFINITE = Math.pow(10,20);

  // profondeur de l'arbre sur lequel s'exerce l'algorithme min-max ;
  // à partir de la valeur 5, le programme provoque une erreur de
  // débordement de la mémoire sur un ordinateur muni de 10 Go de mémoire
  // virtuelle
  private static int DEPTH = 4;

  // l'arbre des morceaux de parties possibles sur lequel s'exerce
  // l'algorithme min-max ;
  // la racine représente la position courante
  private AITree tree;



  // CONSTRUCTEUR



  public AIPlayer(
    CellState color,
    EvolutiveGridParser evolutiveGridParser,
    CellState playedAtRootColor) {

    // initialisation de la couleur jouée par le joueur IA ;
    // attribut hérité de la classe mère AbstractPlayer
    this.color = color;

    // initialsation de l'arbre sur lequel s'exerce l'algorithme min-max
    this.tree
      = new AITree(

          // parseur évolutif de grille représentant la grille de jeu
          // et les données déduites constantes et évolutives
          // (voir l'héritage Grid-ConstantGridParser-EvolutiveGridParser)
          evolutiveGridParser,

          // couleur du joueur à qui c'est le tour dans la position
          // représentée à la racine de l'arbre
          playedAtRootColor,

          // profondeur de l'arbre des morceaux de parties possibles
          DEPTH

        );

  }



  // MÉTHODES DE JARDINAGE
  // (élagage et croissance d'une génération de l'arbre)



  // réaffecte l'attribut tree représentant l'arbre des morceaux
  // de parties possibles par l'arbre dont le tronc est la branche
  // correspondant à la position obtenue depuis celle représentée par
  // le tronc en jouant dans la colonne représentée par le paramètre
  public void pruning(ColumnNumber cutPoint) {

    this.tree = this.tree.getBranches().get(cutPoint);

  }

  // fait pousser l'arbre d'une génération de branche ;
  // après l'appel de la méthode pruning, l'arbre doit repousser
  // d'une génération de branche pour recouvrer sa profondeur initiale
  public void oneGenerationGrowing(AITree tree) {

    // mise à jour de l'attribut distanceFromLeaves du
    // paramètre tree ;
    // via son incrémentation puisque les nouvelles pousses
    // éloignent les feuilles de tree (en tant
    // que son tronc) d'une unité
    tree.incrementDistanceFromLeaves();

    // si tree n'est pas une feuille
    if (tree.getBranches() != null) {

      // on parcourt l'arborescence jusqu'à atteindre les feuilles
      // pour y ajouter une génération de branche
      for (ColumnNumber columnNumber : tree.getBranches().keySet())
        oneGenerationGrowing(
          tree
            .getBranches()
            .get(columnNumber)
        );

    }

    // si tree est une feuille
    else {

      // on ajoute une génération de branche
      tree.growing(1);

    }

  }



  // MÉTHODES DE MISE À JOUR



  // met à jour l'arbre selon le coup correspondant à la colonne
  // jouée représentée par l'instance de ColumnNumber passée
  // en paramètre
  public void updateWithMove(ColumnNumber columnNumber) {

    // élagage de l'arbre ;
    // on élimine toutes les branches qui ne correspondent pas au coup
    // joué (relatif à columnNumber) et le nouveau tronc est la branche
    // correspondant au coup joué (relatif à columnNumber)
    pruning(columnNumber);

    // on fait repousser le nouvel arbre d'une génération de branche
    // afin de lui faire retrouver sa profondeur initiale
    oneGenerationGrowing(this.tree);

  }



  // MÉTHODES D'ÉVALUATION DES POSITIONS



  // évalue la position correspondant à la valeur de la grille
  // représentée par le parseur évolutif de grille passé en paramètre
  private double evaluation(EvolutiveGridParser evolutiveGridParser) {

    return

      // contribution positive relative aux groupes de quatre cellules
      // alignées non encore jouées ou de la même couleur (représentée
      // par une instance de l'énumaration CellState) que la valeur de
      // l'attribut color (hérité de la classe mère AbstractPlayer)
      // représentant la couleur jouée par le joueur IA
      arrayOfSetOfMonochromaticFourCellsInARowEvaluation(
        evolutiveGridParser
          .getColorToArrayOfSetOfMonochromaticFourCellsInARow()
          .get(
            this.color
          )
      )

      -

      // contribution négative relative aux groupes de quatre cellules
      // alignées non encore jouées ou de la couleur de l'adversaire de
      // l'instance
      arrayOfSetOfMonochromaticFourCellsInARowEvaluation(
        evolutiveGridParser
          .getColorToArrayOfSetOfMonochromaticFourCellsInARow()
          .get(
            this.color.theOtherColor()
          )
      );

  }

  // retourne une évaluation du tableau qui, à un entier de 0 à 4, associe
  // l'ensemble de groupes de quatre cellules alignées non colorées ou
  // d'une couleur donnée
  private double arrayOfSetOfMonochromaticFourCellsInARowEvaluation(
      ArrayList<HashSet<FourCellsInARow>>
      arrayOfSetOfMonochromaticFourCellsInARow) {

    double evaluation;

    evaluation = 0;

    for (
        int i = 0 ;
        i < arrayOfSetOfMonochromaticFourCellsInARow.size() ;
        i++) {

      evaluation
        +=

          // les groupes sont de plus en plus valorisés suivant leur nombre
          // de cellules colorées (de la même couleur)
          Math.pow(10, i)
          *
          setOfMonochromaticFourCellsInARowEvaluation(
            arrayOfSetOfMonochromaticFourCellsInARow
              .get(i)
          );

    }

    return evaluation;

  }

  // retourne une évaluation de l'ensemble de groupes de quatre cellules
  // alignées non colorées ou d'une couleur donnée contenant un nombre
  // constant de cellules colorées
  private double setOfMonochromaticFourCellsInARowEvaluation(
      HashSet<FourCellsInARow> setOfMonochromaticFourCellsInARow) {

    double evaluation;

    evaluation = 0;

    for (FourCellsInARow fourCellsInARow : setOfMonochromaticFourCellsInARow)
      evaluation
        +=
          fourCellsInARowEvaluation(
            fourCellsInARow
          );

    return evaluation;

  }

  // retourne une évaluation du groupe de quatre cellules alignées non
  // colorées ou d'une couleur donnée
  private double fourCellsInARowEvaluation(FourCellsInARow fourCellsInARow) {

    // on valorise les petites altitudes dans la grille ;
    // en effet, on renvoie plus précisément l'altitude moyenne des
    // cellules non colorées du groupe
    // (plus les cellules non encore jouées sont basses, plus on a de chance
    // de compléter le groupe en une combinaison gagnante)
    return fourCellsInARow.uncoloredCellsAltitudeAverage();

  }

  // évalue la position représentée par la racine de l'arbre des morceux de
  // parties possibles suivant l'algorithme min-max ;
  // cette méthode est publique car elle est appelée par la classe AITree
  // instanciant des arbres de morceaux de parties possibles lors de la
  // récursion de l'algorithme min-max
  public double minMaxEvaluation(AITree tree) {

    double minOrMax;

    double evaluation;

    // si l'arbre est une feuille
    if (tree.getBranches() == null)

      return evaluation(tree.getEvolutiveGridParser());

    // si l'arbre n'est pas une feuille et
    // si la couleur du joueur est la couleur du prochain joueur
    // dans le parseur évolutif de grille evolutiveGridParser attribut
    // de tree ;
    else if (this.color == tree.getPlayedAtRootColor()) {

      // calcul du maximum des évaluations des positions qui suivent
      // celle représentée par le parseur évolutif de grille
      // evolutiveGridParser, attribut de l'attribut tree ;
      // on fait pour ainsi dire chercher au joueur IA le meilleur coup
      // pour lui

      minOrMax = -INFINITE;

      for (ColumnNumber columnNumber : tree.getBranches().keySet()) {

        evaluation
          = minMaxEvaluation(
              tree
                .getBranches()
                .get(columnNumber)
            );

        if (evaluation > minOrMax)
          minOrMax = evaluation;

      }

      return minOrMax;

    }

    // si l'arbre n'est pas une feuille et
    // si la couleur du joueur n'est pas la couleur du prochain joueur
    // dans le parseur évolutif de grille evolutiveGridParser, attribut
    // de l'attribut tree ;
    else {

      // calcul du minimum des évaluations des positions qui suivent
      // celle représentée par le parseur évolutif de grille
      // evolutiveGridParser, attribut de l'attribut tree ;
      // on cherche pour ainsi dire le pire coup pour le joueur IA
      // puisque c'est à son adversaire de jouer, en pensant que celui-ci
      // cherche à lui nuire au maximum

      minOrMax = INFINITE;

      for (ColumnNumber columnNumber : tree.getBranches().keySet()) {

        evaluation
          = minMaxEvaluation(
              tree
                .getBranches()
                .get(columnNumber)
            );

        if (evaluation < minOrMax)
          minOrMax = evaluation;

      }

      return minOrMax;

    }

  }



  // MÉTHODE DE DÉCISION



  // renvoie la colonne à jouer élue par l'algorithme min-max
  public ColumnNumber wantedColumn() {

    ColumnNumber wantedColumn;

    double evaluation;

    double max;

    wantedColumn = null;

    max = -INFINITE;

    for (ColumnNumber columnNumber : this.tree.getBranches().keySet()) {

      evaluation
        = minMaxEvaluation(
            this
              .tree
              .getBranches()
              .get(columnNumber)
          );

      if (evaluation > max) {

        max = evaluation;
        wantedColumn = columnNumber;

      }

    }

    return wantedColumn;

  }



  // ASSESSEURS



  public AITree getTree() {return this.tree;}



  // MÉTHODES DE DÉBOGAGE



  private ColumnNumber aColumnNumberChosenAtRandom() {

    int randomIndex;
    int index;
    HashSet<ColumnNumber> columnNumberToNextLineNumber;
    int size;

    columnNumberToNextLineNumber
      = new
        HashSet<ColumnNumber>(
          this
            .tree
            .getEvolutiveGridParser()
            .getColumnNumberToNextLineNumber()
            .keySet()
        );

    size = columnNumberToNextLineNumber.size();

    randomIndex = new Random().nextInt(size);

    index = 0;

    for (ColumnNumber columnNumber :
        this
          .tree
          .getEvolutiveGridParser()
          .getColumnNumberToNextLineNumber()
          .keySet()) {

      if (index == randomIndex) return columnNumber;
      index++;

    }

    return null;

  }

  public void showAITreeWithEvaluation() {

    this.tree.showAITree(this);

  }


}
