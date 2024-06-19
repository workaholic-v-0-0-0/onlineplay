# Documentation de la classe : `GameStateUpdateDescription`

## Package
`online.caltuli.batch.userInteractionSimulation.virtualUsers`

## Description
`GameStateUpdateDescription` est une classe qui implémente l'interface `UpdateDescription`. Elle est utilisée pour encapsuler des informations relatives à une mise à jour de l'état du jeu, fournissant des détails sur le nouvel état du jeu dans une simulation.

## Dépendances
- `online.caltuli.model.GameState` : Utilise l'énumération `GameState` pour spécifier l'état actuel du jeu.

## Attributs
- `gameState` : Une instance de `GameState` qui indique l'état actuel du jeu après une mise à jour.

## Constructeurs
### GameStateUpdateDescription(GameState gameState)
- **Description** : Initialise une nouvelle description de mise à jour avec un état de jeu spécifique.
- **Paramètres** :
    - `gameState` : L'état du jeu à décrire.

## Méthodes
### getGameState()
- **Description** : Retourne l'état du jeu associé à cette description de mise à jour.
- **Retourne** : `GameState` indiquant l'état du jeu.

## Utilisation
```java
GameStateUpdateDescription updateDescription = new GameStateUpdateDescription(GameState.WAIT_FIRST_PLAYER_MOVE);
GameState gameState = updateDescription.getGameState();
