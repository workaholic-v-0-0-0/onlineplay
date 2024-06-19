# Documentation de la classe : `ColorsGridUpdateDescription`

## Package
`online.caltuli.batch.userInteractionSimulation.virtualUsers`

## Description
`ColorsGridUpdateDescription` est une classe implémentant l'interface `UpdateDescription`. Elle est utilisée pour décrire une mise à jour de la grille de couleurs dans une simulation de jeu, spécifiquement pour indiquer la colonne affectée par une action de jeu.

## Dépendances
- `online.caltuli.model.Column` : Utilise l'énumération `Column` pour spécifier la colonne mise à jour.

## Attributs
- `column` : Une instance de `Column` qui indique la colonne affectée par la mise à jour.

## Constructeurs
### ColorsGridUpdateDescription(Column column)
- **Description** : Construit une description de mise à jour pour une colonne spécifique.
- **Paramètres** :
    - `column` : La colonne qui sera mise à jour.

## Méthodes
### getColumn()
- **Description** : Retourne la colonne associée à cette description de mise à jour.
- **Retourne** : `Column` indiquant la colonne mise à jour.

## Utilisation
```java
ColorsGridUpdateDescription updateDescription = new ColorsGridUpdateDescription(Column.C1);
Column column = updateDescription.getColumn();
