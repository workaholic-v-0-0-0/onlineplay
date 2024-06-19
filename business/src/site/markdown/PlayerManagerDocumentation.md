# Documentation de la classe : `PlayerManager`

## Package
`online.caltuli.business`

## Description
`PlayerManager` étend `UserManager` pour gérer spécifiquement les interactions liées aux joueurs dans les jeux. Cette classe fournit des méthodes pour initier des parties entre joueurs, gérer les invitations à des jeux, et maintenir les états des parties en cours.

## Dépendances
- `online.caltuli.consumer.dao.*` : Utilise les DAO pour l'accès aux données des utilisateurs et des jeux.
- `online.caltuli.model.*` : Utilise les modèles de données comme `User`, `Game`, et `Player`.
- `online.caltuli.business.exception.BusinessException` : Gestion des exceptions liées aux opérations métier.
- `jakarta.enterprise.context.ApplicationScoped`, `jakarta.enterprise.inject.Specializes` : Annotations pour la gestion des beans dans le contexte CDI.

## Attributs
- Les attributs hérités de `UserManager` pour la gestion des utilisateurs.

## Constructeurs
- Constructeur par défaut qui initialise les états nécessaires pour la gestion des joueurs.

## Méthodes
### makeUserProposeGame(User user_who_proposed_the_game)
- **Description** : Permet à un utilisateur de proposer une nouvelle partie.
- **Exceptions** : `BusinessException` si la communication avec la base de données échoue ou si la création du jeu dans la base échoue.
- **Retourne** : `GameManager` qui gère la partie proposée.

### makeUserPlayWithUser(int id_of_user_who_proposed_the_game, User user_who_joins_the_game)
- **Description** : Connecte un utilisateur à une partie proposée par un autre utilisateur.
- **Exceptions** : `BusinessException` si la récupération des données utilisateur ou jeu échoue, ou si le jeu n'existe pas.
- **Retourne** : `GameManager` associé à la partie mise à jour.

### getGameProposedByUser(User user_who_proposed_game)
- **Description** : Récupère une partie proposée par un utilisateur spécifique.
- **Retourne** : `Game` ou `null` si aucune partie n'est trouvée.

### usersToPlayer(Map<Integer, User> users)
- **Description** : Convertit une carte d'utilisateurs en une carte de joueurs.
- **Retourne** : `Map<Integer, Player>` convertie.

### gamesToGameSummaries(Map<Integer, Game> games)
- **Description** : Convertit une carte de jeux en résumés de jeux.
- **Retourne** : `Map<Integer, GameSummary>` de résumés de jeux.

### getWaitingToPlayUser()
- **Description** : Retourne la liste des utilisateurs en attente de jeu.

### getGames()
- **Description** : Retourne la carte des jeux actuellement en cours.

## Utilisation
```java
PlayerManager playerManager = new PlayerManager();
GameManager gameManager = playerManager.makeUserProposeGame(user);
gameManager = playerManager.makeUserPlayWithUser(userId, anotherUser);
