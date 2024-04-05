# Spécification de classe : `SessionManagement`

## Package
`online.caltuli.webapp.filter`

## Description
La classe `SessionManagement` est responsable de la gestion des sessions dans l'application, en s'assurant que chaque session est correctement initialisée avec une instance `UserConnection`. Cette instance enregistre les informations relatives à la connexion de l'utilisateur, y compris l'adresse IP et le moment de la connexion.

## Dépendances

### Dépendances Internes
- `online.caltuli.model.UserConnection` : Utilisé pour stocker les informations de connexion de l'utilisateur.
- `online.caltuli.business.UserManager` : Pour enregistrer et récupérer les informations de connexion des utilisateurs dans la base de données.

### Dépendances Tierces
- `jakarta.servlet.http.HttpServletRequest` et `HttpServletResponse` : Pour manipuler les requêtes et réponses HTTP.
- `jakarta.enterprise.context.ApplicationScoped` : Indique que `SessionManagement` est un bean CDI à portée d'application.
- `org.apache.logging.log4j.Logger` : Pour le logging des activités et des erreurs.

## Attributs
- `logger` : Logger pour enregistrer les activités de la classe.
- `userManager` : Injecté pour accéder aux opérations liées à la gestion des connexions utilisateur.

## Constructeurs
- N/A

## Méthodes

### initialiseSessionIfNot(HttpServletRequest request, HttpServletResponse response)
- **Description** : Vérifie si la session actuelle est initialisée avec un attribut `userConnection`. Si non, crée une nouvelle session, enregistre les informations de connexion de l'utilisateur dans la base de données et attribue ces informations à la session.

### isAuthenticated(HttpSession session)
- **Description** : Vérifie si la session courante correspond à un utilisateur authentifié en vérifiant si l'attribut `userConnection` associé à la session a un `userId` différent de -1.

## Diagrammes
[TO DO]

## Exemples d'Utilisation

Cette classe est principalement utilisée par les filtres de servlet pour s'assurer que chaque session est correctement initialisée et pour vérifier l'authentification de l'utilisateur basée sur la session.

### Configuration dans l'application
`SessionManagement` est un bean CDI à portée d'application et peut être injecté dans d'autres composants de l'application, tels que les filtres servlet, pour gérer la création de sessions et la vérification de l'authentification.

### Sécurité
La gestion correcte des sessions et la vérification de l'authentification augmentent la sécurité de l'application en s'assurant que les informations sensibles sont uniquement accessibles aux utilisateurs authentifiés.
