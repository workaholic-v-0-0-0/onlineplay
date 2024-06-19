# Documentation de la classe : `GameContainer`

## Paquet
`online.caltuli.batch.userInteractionSimulation.jsonUtils`

## Description
`GameContainer` est une classe utilitaire conçue pour encapsuler un objet `Game`. Elle est souvent utilisée dans des scénarios de désérialisation où les données JSON incluent un objet de jeu, permettant ainsi de manipuler plus facilement les données de jeu après leur chargement.

## Dépendances
- `online.caltuli.model.Game` : Classe utilisée pour représenter l'entité de jeu.

## Attributs
- `game` : Instance de `Game` stockée dans le conteneur.

## Méthodes
### getGame()
- **Description** : Retourne l'objet `Game` contenu dans le conteneur.
- **Retourne** : L'objet `Game` actuellement stocké dans le conteneur.

### setGame(Game game)
- **Description** : Définit l'objet `Game` à stocker dans le conteneur.
- **Paramètres** :
    - `game` : L'objet `Game` à stocker.

## Utilisation
```java
Game game = new Game();
GameContainer container = new GameContainer();
container.setGame(game);
Game retrievedGame = container.getGame();
System.out.println("Game retrieved from container: " + retrievedGame);
