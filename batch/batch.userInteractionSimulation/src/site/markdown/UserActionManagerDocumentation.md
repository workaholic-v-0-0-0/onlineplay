# Documentation de la classe : `UserActionManager`

## Package
`online.caltuli.batch.userInteractionSimulation.virtualUsers`

## Description
`UserActionManager` est une classe responsable de la gestion des actions d'un utilisateur virtuel dans une simulation de jeu, gérant les interactions via HTTP et WebSocket pour simuler des activités de jeu réelles comme l'enregistrement, l'authentification, le jeu, et l'observation des parties.

## Dépendances
- `com.fasterxml.jackson.databind.*` : Pour la sérialisation et la désérialisation des données JSON.
- `online.caltuli.model.*` : Utilise des classes telles que `Game`, `User`, `Coordinates`, `Column` pour les opérations de jeu.
- `online.caltuli.batch.userInteractionSimulation.clients.*` : Clients HTTP et WebSocket pour les interactions réseau.
- `java.net.http.HttpClient` : Client HTTP pour envoyer des requêtes.
- `org.apache.logging.log4j.Logger` : Logger pour enregistrer les opérations et les erreurs.

## Attributs
- `httpClient` : Client HTTP pour les communications réseau.
- `gameWebSocketClient` : Client WebSocket pour la connexion aux jeux en temps réel.
- `httpClientSSLContext` : Contexte SSL pour la gestion sécurisée des requêtes HTTP.

## Constructeurs
### UserActionManager(VirtualUserInformationConfig, NetworkConfig, NetworkConfig, ClientConfig)
- **Description** : Initialise un `UserActionManager` avec des configurations spécifiques pour la communication réseau et la simulation utilisateur.
- **Exceptions** :
    - `NoSuchAlgorithmException`
    - `KeyManagementException`
    - `KeyStoreException`

## Méthodes
### initializeClients()
- **Description** : Initialise les clients nécessaires pour la communication HTTP et WebSocket.
- **Exceptions** : `NoSuchAlgorithmException`, `KeyManagementException`, `KeyStoreException`

### cleanupClients()
- **Description** : Nettoie et réinitialise les ressources client après utilisation.

### register(), authenticate(), logout()
- **Description** : Méthodes pour gérer l'enregistrement, l'authentification et la déconnexion de l'utilisateur.

### proposeNewGame(), makeMeObserveAndPlayGame(GameObserver), waitOpponent(), playMove(Column)
- **Description** : Méthodes pour gérer les actions de jeu comme proposer un jeu, observer et jouer une partie, attendre un adversaire, et jouer un coup.

### fetchUser(), fetchGame()
- **Description** : Récupère les informations de l'utilisateur et du jeu en cours à partir du serveur.

## Utilisation
```java
UserActionManager actionManager = new UserActionManager(userConfig, httpsConfig, wssConfig, clientConfig);
actionManager.register();
// Suivi par d'autres interactions de jeu
