# Documentation de la classe : `MinMaxDecisionEngine`

## Package
`online.caltuli.business.ai`

## Description
`MinMaxDecisionEngine` est une implémentation de l'interface `DecisionEngine` qui utilise l'algorithme MinMax pour déterminer le meilleur mouvement possible dans un jeu basé sur l'état actuel de l'arbre de décision géré par `TreeManager`. Cette classe est conçue pour des jeux où deux joueurs alternent les coups, typiquement dans les contextes où chaque joueur essaie de maximiser ses chances de gagner tout en minimisant celles de l'adversaire.

## Dépendances
- `online.caltuli.model.Column` : Utilisé pour spécifier les colonnes du jeu.
- `java.util.Map` : Utilisé pour parcourir les branches de l'arbre.

## Attributs
- `treeManager` : Une instance de `TreeManager` utilisée pour gérer l'état actuel de l'arbre de décision du jeu.

## Constructeurs
### MinMaxDecisionEngine()
- **Description** : Initialise le moteur de décision avec un nouveau gestionnaire d'arbre.

## Méthodes
### updateWithMove(Column column)
- **Description** : Met à jour l'arbre de décision après un coup joué, en élaguant l'arbre pour conserver uniquement la branche correspondant à la colonne jouée et en faisant croître cette branche d'une génération.
- **Paramètres** :
    - `column` : La colonne où le coup a été joué.

### getBestMove()
- **Description** : Détermine le meilleur mouvement possible en utilisant l'évaluation MinMax de l'arbre de décision.
- **Retourne** : `Column` qui représente le meilleur mouvement.

### minMaxEvaluation(Tree tree)
- **Description** : Fonction récursive qui calcule la valeur MinMax d'un arbre de décision, utilisée pour évaluer les différents états possibles du jeu à partir d'un certain point.
- **Paramètres** :
    - `tree` : L'arbre à évaluer.
- **Retourne** : `long` représentant l'évaluation MinMax de l'arbre.
- **Accès** : Privée.

## Utilisation
```java
MinMaxDecisionEngine engine = new MinMaxDecisionEngine();
engine.updateWithMove(Column.C3); // Mise à jour de l'arbre après un coup dans la colonne 3
Column bestMove = engine.getBestMove(); // Détermination du meilleur mouvement suivant
