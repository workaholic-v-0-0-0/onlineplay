# Documentation de l'interface : `UpdateDescription`

## Paquet
`online.caltuli.batch.userInteractionSimulation.interfaces`

## Description
L'interface `UpdateDescription` sert de base pour créer des descriptions détaillées des mises à jour dans un environnement de simulation de jeu. Cette interface est destinée à être implémentée par des classes qui fourniront des informations spécifiques sur divers types de mises à jour du jeu, telles que les mises à jour de l'état du jeu ou des modifications de la grille de couleurs.

## Utilisation
L'interface `UpdateDescription` ne définit pas de méthodes par elle-même, mais elle est utilisée comme un type de contrat pour assurer la cohérence entre les différentes descriptions de mise à jour qui peuvent être utilisées dans le système. Les classes qui implémentent `UpdateDescription` doivent fournir des détails concrets sur les mises à jour spécifiques liées à leur contexte.

### Exemple de classe implémentant `UpdateDescription`
```java
public class GameStateUpdateDescription implements UpdateDescription {
    private GameState gameState;

    public GameStateUpdateDescription(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
