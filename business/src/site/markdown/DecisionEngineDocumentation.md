# Documentation de l'interface : `DecisionEngine`

## Package
`online.caltuli.business.ai`

## Description
`DecisionEngine` définit une interface pour les moteurs de décision utilisés dans des contextes de jeux AI. Cette interface spécifie les méthodes nécessaires pour mettre à jour l'état du jeu en fonction des mouvements joués et pour déterminer le meilleur mouvement suivant basé sur l'analyse actuelle du jeu.

## Dépendances
- `online.caltuli.model.Column` : Utilisé pour identifier les colonnes dans le cadre de la mise à jour des mouvements et de la détermination du meilleur mouvement.

## Méthodes
### updateWithMove(Column column)
- **Description** : Met à jour l'état du jeu en fonction du coup joué dans la colonne spécifiée.
- **Paramètres** :
    - `column` : La colonne où le coup a été joué.

### getBestMove()
- **Description** : Calcule et retourne le meilleur mouvement possible à jouer ensuite, en fonction de l'état actuel du jeu.
- **Retourne** : `Column` qui représente le meilleur mouvement à jouer.

## Utilisation
```java
// Implémentation hypothétique de DecisionEngine
DecisionEngine engine = new SomeDecisionEngineImplementation();
engine.updateWithMove(Column.C3); // Mise à jour après un coup dans la colonne 3
Column bestMove = engine.getBestMove(); // Obtention du meilleur mouvement suivant
