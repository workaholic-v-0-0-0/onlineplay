# Spécification de classe : `UserManager`

## Package
`online.caltuli.business`

## Description
La classe `UserManager` est responsable de la gestion des utilisateurs dans l'application. Elle fournit des fonctionnalités pour enregistrer une connexion utilisateur, mettre à jour les informations de connexion, enregistrer un nouvel utilisateur, authentifier un utilisateur, et déconnecter un utilisateur. Elle utilise une `Map` pour garder une liste des utilisateurs actuellement connectés.

## Dépendances

### Dépendances Internes
- `online.caltuli.model.*` : Pour utiliser les classes du modèle comme `User` et `UserConnection`.
- `online.caltuli.consumer.dao.*` : Pour accéder aux objets d'accès aux données (DAO).

### Dépendances Tierces
- `jakarta.enterprise.context.ApplicationScoped` : Indique que `UserManager` est un bean à portée d'application dans le contexte CDI.
- `org.apache.logging.log4j.Logger` : Pour le logging des opérations et des erreurs.
- `org.mindrot.jbcrypt.BCrypt` : Utilisé pour hasher et vérifier les mots de passe des utilisateurs.

## Attributs
- `connectedUserList` : Une `Map` conservant les utilisateurs connectés, indexés par leur ID.
- `logger` : Logger pour enregistrer les activités et les erreurs.

## Constructeurs
- Pas de constructeur explicite; utilise le constructeur par défaut.

## Méthodes

### logUserConnection(String ipAddress, Timestamp timestamp)
- **Description** : Enregistre une nouvelle connexion utilisateur dans la base de données et retourne une instance `UserConnection` associée.

### updateUserConnection(UserConnection userConnection)
- **Description** : Met à jour les informations de connexion d'un utilisateur dans la base de données.

### registerUser(String username, String password, String email, String message)
- **Description** : Enregistre un nouvel utilisateur dans la base de données après avoir hashé le mot de passe et vérifié l'unicité du nom d'utilisateur.

### authenticateUser(String username, String password)
- **Description** : Authentifie un utilisateur en vérifiant son nom d'utilisateur et son mot de passe hashé.

### disconnectUser(int userId)
- **Description** : Supprime un utilisateur de la liste des utilisateurs connectés.

### getConnectedUserList()
- **Description** : Retourne la liste des utilisateurs actuellement connectés.

## Diagrammes
[TO DO]

## Exemples d'Utilisation

L'utilisation de cette classe est centrale dans l'application pour la gestion des utilisateurs. Elle est typiquement appelée par des servlets ou des filtres qui gèrent l'authentification, l'enregistrement, et la déconnexion des utilisateurs.

### Sécurité
L'utilisation de BCrypt pour hasher les mots de passe renforce la sécurité en protégeant les mots de passe stockés contre les attaques de type brute force ou rainbow table.
