# Documentation de la classe : `UserContainer`

## Paquet
`online.caltuli.batch.userInteractionSimulation.jsonUtils`

## Description
`UserContainer` est une classe utilitaire conçue pour encapsuler un objet `User`. Cette structure est particulièrement utile pour la gestion des données utilisateur lors des processus de désérialisation et de sérialisation dans les interactions basées sur des services web ou des API.

## Dépendances
- `online.caltuli.model.User` : Classe utilisée pour représenter une entité utilisateur dans le modèle de données.

## Attributs
- `user` : Instance de `User` stockée dans le conteneur.

## Méthodes
### getUser()
- **Description** : Retourne l'objet `User` contenu dans le conteneur.
- **Retourne** : L'objet `User` actuellement stocké.

### setUser(User user)
- **Description** : Définit l'objet `User` à stocker dans le conteneur.
- **Paramètres** :
    - `user` : L'objet `User` à stocker.

## Utilisation
```java
User user = new User();
UserContainer container = new UserContainer();
container.setUser(user);
User retrievedUser = container.getUser();
System.out.println("User retrieved from container: " + retrievedUser);
