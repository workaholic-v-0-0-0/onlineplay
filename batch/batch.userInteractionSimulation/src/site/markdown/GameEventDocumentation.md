# Documentation de la classe : `GameEvent`

## Paquet
`online.caltuli.batch.userInteractionSimulation.virtualUsers`

## Description
`GameEvent` est une classe utilisée pour encapsuler les événements relatifs aux mises à jour du jeu au sein d'un environnement de simulation. Elle contient des informations sur l'aspect du jeu qui est mis à jour et inclut une description détaillée de cette mise à jour via une implémentation de l'interface `UpdateDescription`.

## Dépendances
- `online.caltuli.batch.userInteractionSimulation.interfaces.UpdateDescription` : Utilisée pour fournir une interface de description détaillée des mises à jour que `GameEvent` utilise pour transmettre des détails spécifiques sur la mise à jour du jeu.

## Attributs
- `whatToBeUpdated` : Chaîne de caractères indiquant quel aspect du jeu est concerné par l'événement, comme "gameState" ou "colorsGrid".
- `description` : Une instance de `UpdateDescription` qui fournit des informations détaillées sur la mise à jour.

## Constructeurs
### GameEvent(String whatToBeUpdated, UpdateDescription description)
- **Description** : Construit un nouvel événement de jeu avec des détails spécifiés sur la partie du jeu mise à jour et une description de cette mise à jour.
- **Paramètres** :
    - `whatToBeUpdated` : Spécifie l'aspect du jeu qui est mis à jour.
    - `description` : La description détaillée de la mise à jour.

## Méthodes
### getWhatToBeUpdated()
- **Description** : Retourne l'aspect du jeu qui est mis à jour.
- **Retour** : `String` indiquant le composant ou l'aspect de la mise à jour du jeu.

### getDescription()
- **Description** : Retourne la description détaillée de la mise à jour.
- **Retour** : `UpdateDescription` fournissant des détails spécifiques de la mise à jour.

## Utilisation
```java
UpdateDescription description = new GameStateUpdateDescription(GameState.WAIT_FIRST_PLAYER_MOVE);
GameEvent gameEvent = new GameEvent("gameState", description);
String typeDeMiseÀJour = gameEvent.getWhatToBeUpdated();
UpdateDescription detailsDeMiseÀJour = gameEvent.getDescription();
