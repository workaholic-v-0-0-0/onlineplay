# Documentation de la classe : `EvaluatedEvolutiveGridParser`

## Package
`online.caltuli.business.ai`

## Description
Cette classe étend `EvolutiveGridParser` en évaluant dynamiquement le plateau de jeu en fonction des mouvements effectués. Elle utilise un système de poids pour les différentes configurations de lignes de coordonnées afin de calculer une évaluation numérique du plateau de jeu qui aide à prendre des décisions stratégiques.

## Dépendances
- `online.caltuli.business.ConstantGridParser` : Utilise pour accéder aux tableaux de coordonnées prédéfinis.
- `online.caltuli.model.CellState` : Pour gérer les états des cellules du jeu (rouge ou vert).
- `online.caltuli.model.Coordinates` : Pour représenter les coordonnées sur le plateau de jeu.

## Attributs
- `ALTITUDE_WEIGHT` : Poids utilisé pour évaluer l'altitude des coordonnées.
- `INFINITY` et `MINUS_INFINITY` : Valeurs utilisées pour représenter une évaluation infinie positive ou négative, indiquant une victoire ou une défaite.
- `coordinatesRowToWeight` : `HashMap` associant des lignes de `Coordinates` à leur poids spécifique pour l'évaluation.
- `evaluation` : Évaluation courante du plateau de jeu.

## Constructeurs
- Le constructeur initialise `evaluation` à zéro et prépare le plateau pour le jeu.

## Méthodes
### updateWithMove(int column)
- **Description** : Met à jour le plateau de jeu avec un nouveau mouvement dans la colonne spécifiée, recalculant l'évaluation basée sur ce mouvement.
- **Retourne** : `Coordinates` des coordonnées où le mouvement a été effectué.

### getEvaluation()
- **Description** : Retourne l'évaluation courante du plateau de jeu.

### setEvaluation(long evaluation)
- **Description** : Définit l'évaluation du plateau de jeu.

## Utilisation
```java
EvaluatedEvolutiveGridParser parser = new EvaluatedEvolutiveGridParser();
parser.updateWithMove(3); // Joue dans la colonne 4
long score = parser.getEvaluation(); // Obtient l'évaluation du plateau après le mouvement
