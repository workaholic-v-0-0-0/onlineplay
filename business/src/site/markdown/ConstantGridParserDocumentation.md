# Documentation de la classe : `ConstantGridParser`

## Package
`online.caltuli.business`

## Description
Cette classe sert à parser et organiser une grille de coordonnées de manière constante et optimisée. Elle utilise une paramétrisation bidimensionnelle pour récupérer une instance de `Coordinates` à partir de ses coordonnées et stocke les informations dans des tableaux multidimensionnels pour un accès rapide.

## Dépendances
- `online.caltuli.model.*` : Utilise `BidimensionalParametrizationOfSetOfCoordinatesFactory` et `Coordinates` pour manipuler les coordonnées.

## Attributs
- `CP` : Factory statique pour la paramétrisation bidimensionnelle des ensembles de coordonnées.
- `CA` : Tableau statique de `Coordinates` pour un parcours optimal de toutes les instances de coordonnées.
- `arrayOfCoordinatesRowsStartingFromBottomWithCoordinates` et `arrayOfCoordinatesRowsContainingCoordinates` : Tableaux complexes pour stocker les lignes de coordonnées basées sur des critères spécifiques.
- `NUMBER_OF_COORDINATES_ROWS` : Constante indiquant le nombre de lignes de coordonnées.

## Constructeurs
- La classe n'a pas de constructeur explicite; elle initialise les attributs statiques dans un bloc statique.

## Méthodes
Aucune méthode publique n'est exposée ; les attributs sont statiques et accessibles directement.

## Utilisation
- Pour obtenir une instance de `Coordinates` basée sur la position : `Coordinates coordinates = ConstantGridParser.CP.get(x, y);`
- Pour accéder à une ligne de coordonnées spécifique : `Coordinates[][] coordinatesRow = ConstantGridParser.arrayOfCoordinatesRowsStartingFromBottomWithCoordinates[x][y];`

## Exemples d'Utilisation
```java
Coordinates coordinates = ConstantGridParser.CP.get(3,4);
Coordinates[][] coordinatesRow = ConstantGridParser.arrayOfCoordinatesRowsStartingFromBottomWithCoordinates[0][0][1];
