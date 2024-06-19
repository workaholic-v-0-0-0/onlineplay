# Documentation de la classe : `EvolutiveGridParser`

## Package
`online.caltuli.business`

## Description
Cette classe permet de suivre l'évolution d'un plateau de jeu en mettant à jour les états en fonction des mouvements des joueurs. Elle gère les changements de couleur des joueurs, détecte les conditions de victoire ou d'égalité, et maintient l'état des lignes jouables sur le plateau.

## Dépendances
- `online.caltuli.model.CellState` : Enumération représentant l'état des cellules (rouge, vert, nul).
- `online.caltuli.model.Coordinates` : Pour représenter les coordonnées sur le plateau de jeu.
- `com.fasterxml.jackson.annotation.JsonProperty` : Annotations utilisées pour marquer les propriétés pour la sérialisation JSON.
- `org.apache.logging.log4j.Logger` : Pour enregistrer les opérations et les erreurs de debug.

## Attributs
- `nextLine` : Tableau indiquant la ligne la plus basse jouable pour chaque colonne.
- `nextColor` : Couleur du joueur dont c'est le tour de jouer.
- `redRowsToNbOfRedCoordinates` : Map associant les rangées où seulement le rouge a joué à leur nombre de coordonnées rouges.
- `greenRowsToNbOfGreenCoordinates` : Map similaire pour le vert.
- `unWinnableCoordinatesRowsSet` : Ensemble des rangées où les deux couleurs sont présentes, rendant la rangée non gagnante pour les deux joueurs.
- `logger` : Logger pour le débogage.

## Constructeurs
- Constructeur sans paramètre qui initialise les structures de données.
- Constructeur avec paramètres pour initialiser l'objet avec des états spécifiques.

## Méthodes
### updateWithMove(int column)
- **Description** : Effectue un mouvement dans la colonne spécifiée et met à jour l'état du plateau.
- **Retourne** : `Coordinates` des coordonnées où le mouvement a été effectué.

### isLegalMove(int column)
- **Description** : Vérifie si un mouvement dans la colonne spécifiée est légal.
- **Retourne** : `boolean` indiquant si le mouvement est légal.

### detectDraw()
- **Description** : Détecte si le jeu est à égalité.
- **Retourne** : `boolean` indiquant si le jeu est à égalité.

### detectWinningColor()
- **Description** : Détecte la couleur gagnante si elle existe.
- **Retourne** : `CellState` de la couleur gagnante ou NULL si aucune n'a gagné.

### Getters et Setters
- Méthodes pour obtenir et définir les états internes comme `nextLine`, `nextColor`, `redRowsToNbOfRedCoordinates`, etc.

## Utilisation
```java
EvolutiveGridParser parser = new EvolutiveGridParser();
Coordinates move = parser.updateWithMove(3); // Joue dans la colonne 3
boolean isDraw = parser.detectDraw(); // Vérifie si le jeu est à égalité
CellState winner = parser.detectWinningColor(); // Vérifie s'il y a un gagnant
