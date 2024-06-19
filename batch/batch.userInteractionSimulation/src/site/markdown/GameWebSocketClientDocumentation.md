# Documentation de la classe : `GameWebSocketClient`

## Paquet
`online.caltuli.batch.userInteractionSimulation.clients`

## Description
`GameWebSocketClient` est une classe responsable de la gestion des connexions WebSocket pour une simulation de jeu. Elle permet de connecter, recevoir des mises à jour et envoyer des commandes via WebSocket en fonction de l'interaction du jeu.

## Dépendances
- `java.net.http.HttpClient` : Utilisé pour créer des connexions WebSocket.
- `java.net.http.WebSocket` : Interface WebSocket pour la communication en temps réel.
- `org.apache.logging.log4j.Logger` : Utilisé pour la journalisation des informations et des erreurs.

## Attributs
- `webSocket` : Instance de WebSocket utilisée pour la communication en temps réel.
- `observers` : Liste des observateurs du jeu qui sont notifiés des mises à jour.

## Constructeurs
### GameWebSocketClient(HttpClient httpClient, NetworkConfig wssNetworkConfig)
- **Description** : Initialise le client avec des configurations spécifiques et prépare la connexion WebSocket.
- **Paramètres** :
    - `httpClient` : Client HTTP utilisé pour établir la connexion WebSocket.
    - `wssNetworkConfig` : Configuration réseau pour les détails de l'URL WebSocket.

## Méthodes
### connectToGame(int gameId)
- **Description** : Connecte le client à un jeu spécifique via WebSocket en utilisant l'ID de jeu.
- **Paramètres** :
    - `gameId` : Identifiant du jeu pour la connexion.

### disconnectFromGame()
- **Description** : Déconnecte le client du jeu et ferme la connexion WebSocket.

### sendMessage(String message)
- **Description** : Envoie un message via WebSocket.
- **Paramètres** :
    - `message` : Message à envoyer.

### addObserver(GameObserver observer), removeObserver(GameObserver observer)
- **Description** : Ajoute ou supprime un observateur de la liste des observateurs notifiés lors des mises à jour du jeu.

### notifyObservers(GameEvent gameEvent)
- **Description** : Notifie tous les observateurs d'un événement de jeu.

## Utilisation
```java
HttpClient httpClient = HttpClient.newHttpClient();
NetworkConfig wssConfig = new NetworkConfig("wss://example.com/game");
GameWebSocketClient client = new GameWebSocketClient(httpClient, wssConfig);
client.connectToGame(123); // Connecte à un jeu avec l'ID 123
client.sendMessage("Hello, World!"); // Envoie un message
client.disconnectFromGame(); // Déconnexion du jeu
