# Documentation de la classe : `TreeManager`

## Package
`online.caltuli.business.ai`

## Description
`TreeManager` gère un arbre de décision pour les jeux en utilisant un modèle d'évaluation évolutif. Cette classe initialise et étend un arbre en générant de nouveaux états de jeu à chaque niveau jusqu'à une profondeur spécifique. Elle facilite la croissance et la gestion de l'arbre, permettant des analyses prédictives des mouvements dans le cadre de stratégies de jeu.

## Dépendances
- `online.caltuli.model.*` : Utilise `Column` pour les indices de colonnes et `Coordinates` pour les positions dans le jeu.
- `java.util.*` : Utilise `LinkedList`, `Queue`, et `Map` pour la gestion de la structure de l'arbre et des duplications d'état.

## Attributs
- `tree` : L'instance de `Tree` qui représente l'arbre de décision courant.

## Constructeurs
### TreeManager()
- **Description** : Construit un `TreeManager` en initialisant un arbre de décision et en le faisant croître jusqu'à la profondeur maximale définie.

## Méthodes
### grow()
- **Description** : Fait croître l'arbre de décision jusqu'à la profondeur maximale spécifiée dans `Tree.DEPTH`.

### growOneGeneration()
- **Description** : Étend l'arbre d'une génération en dupliquant et en évaluant les nouveaux nœuds pour chaque mouvement légal du jeu.

### duplicateExceptCoordinatesRow(EvaluatedEvolutiveGridParser eegp)
- **Description** : Duplique l'état du jeu en conservant l'état actuel des lignes et colonnes sans réinitialiser les configurations de jeu.
- **Accès** : Privée.

### prune(Column column)
- **Description** : Élaguage de l'arbre, ne conservant que la branche sous la colonne spécifiée, permettant ainsi de suivre un chemin de jeu spécifique.

### Getters et Setters
- Méthodes pour obtenir et définir `tree`.

## Utilisation
```java
TreeManager manager = new TreeManager();
manager.grow(); // Fait croître l'arbre à la profondeur complète
manager.prune(Column.COLUMN3); // Conserve uniquement la branche sous la colonne 3
Tree tree = manager.getTree(); // Obtient l'arbre actuel
