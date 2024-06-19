# Documentation de la classe : `GameManager`

## Package
`online.caltuli.business`

## Description
`GameManager` gère la logique de jeu pour un jeu spécifique, incluant le suivi des mouvements, la vérification de la légalité des mouvements, et la mise à jour de l'état du jeu en fonction des actions des joueurs.

## Dépendances
- `online.caltuli.model.*` : Utilise les classes `Game`, `Coordinates`, `CellState`, et `GameState` pour gérer l'état du jeu.
- `online.caltuli.business.exception.BusinessException` : Pour gérer les exceptions spécifiques liées aux règles du jeu.

## Attributs
- `game` : Instance de `Game` contenant l'état actuel du jeu.
- `egp` : Instance de `EvolutiveGridParser` utilisée pour interpréter et mettre à jour les mouvements sur la grille de jeu.

## Constructeurs
- `GameManager(Game game)` : Initialise le gestionnaire avec un jeu spécifique.

## Méthodes
### isLegalMove(int column)
- **Description** : Vérifie si un mouvement est légal dans la colonne donnée.
- **Retourne** : `boolean` indiquant si le mouvement est légal.

### playMove(int column)
- **Description** : Effectue un mouvement dans la colonne spécifiée si celui-ci est légal, sinon lance une `BusinessException`.
- **Retourne** : `Coordinates` de l'emplacement où le pion a été placé.
- **Exceptions** : `BusinessException` si le mouvement n'est pas légal.

### updateGameWithMove(Coordinates coordinatesPlayed)
- **Description** : Met à jour le jeu après qu'un mouvement a été joué, incluant l'état de la grille et l'état du jeu.
- **Accès** : Privée.

### switchPlayer()
- **Description** : Échange les joueurs de positions, modifiant ainsi qui est le premier et le second joueur.

### Getters et Setters
- Méthodes pour obtenir et définir `game` et `egp`.

## Utilisation
```java
GameManager gameManager = new GameManager(game);
try {
    Coordinates move = gameManager.playMove(3); // Joue dans la colonne 3
} catch (BusinessException e) {
    System.out.println(e.getMessage());
}
