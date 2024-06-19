# Documentation de la classe : `Tree`

## Package
`online.caltuli.business.ai`

## Description
La classe `Tree` représente un arbre de décision utilisé dans le cadre d'algorithmes de jeu avancés. Chaque nœud de l'arbre est associé à un état de jeu évalué à travers un `EvaluatedEvolutiveGridParser`. L'arbre permet de représenter les différentes branches de jeu possibles à partir d'un état donné, facilitant ainsi la prise de décision AI.

## Dépendances
- `online.caltuli.model.CellState` : Utilisé pour déterminer les conditions de victoire ou de match nul.
- `online.caltuli.model.Column` : Utilisé pour identifier les colonnes dans les branches de l'arbre.
- `java.util.HashMap` : Utilisé pour stocker les branches de l'arbre, associant une colonne à un sous-arbre.

## Attributs
- `DEPTH` : Profondeur maximale de l'arbre.
- `root` : Instance de `EvaluatedEvolutiveGridParser` qui sert de racine à l'arbre.
- `depth` : Profondeur actuelle du nœud dans l'arbre.
- `branches` : `HashMap` contenant les sous-arbres, où chaque clé est une colonne et chaque valeur est un `Tree`.

## Constructeurs
### Tree(EvaluatedEvolutiveGridParser root)
- **Description** : Initialise l'arbre avec une racine spécifiée et configure les branches en fonction de l'état de jeu de la racine.

## Méthodes
### canGrow()
- **Description** : Vérifie si l'arbre peut encore croître, c'est-à-dire s'il n'est pas à un état terminal (match nul ou victoire).
- **Retourne** : `boolean` indiquant si l'arbre peut croître.

### isOnBorder()
- **Description** : Vérifie si le nœud courant est à la bordure de l'arbre (à la profondeur maximale définie).
- **Retourne** : `boolean` indiquant si le nœud est à la bordure.

### incrementDepth()
- **Description** : Incrémente la profondeur de l'arbre de un.

### Getters et Setters
- Méthodes pour obtenir et définir les attributs `root`, `depth`, et `branches`.

## Utilisation
```java
EvaluatedEvolutiveGridParser parser = new EvaluatedEvolutiveGridParser();
Tree decisionTree = new Tree(parser);
if (decisionTree.canGrow()) {
    // Ajouter des branches au besoin
}
