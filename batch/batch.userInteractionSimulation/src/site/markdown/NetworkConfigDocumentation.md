# Documentation de l'énumération : `NetworkConfig`

## Paquet
`online.caltuli.batch.userInteractionSimulation.config.network`

## Description
`NetworkConfig` est une énumération qui définit différentes configurations réseau pour les simulations d'interaction utilisateur. Elle permet de spécifier les détails de la connexion pour différents environnements, tels que les environnements locaux et en production, en utilisant HTTPS ou WebSocket (WSS).

## Valeurs de l'énumération
- `LOCAL_HTTPS` : Configuration pour les connexions HTTPS locales.
- `DROPLET_HTTPS` : Configuration pour les connexions HTTPS sur un serveur de production.
- `LOCAL_WSS` : Configuration pour les connexions WebSocket locales.
- `DROPLET_WSS` : Configuration pour les connexions WebSocket sur un serveur de production.
- `DROPLET_HTTPS_VERSION_SYLVAIN` : Configuration HTTPS spécifique pour une version nommée Sylvain.
- `DROPLET_WSS_VERSION_SYLVAIN` : Configuration WebSocket spécifique pour une version nommée Sylvain.

## Attributs
- `protocol` : Le protocole utilisé (HTTPS ou WSS).
- `host` : Le nom de l'hôte.
- `port` : Le port sur lequel le serveur est accessible.
- `basePath` : Le chemin de base de l'application sur le serveur.

## Constructeur
### NetworkConfig(String protocol, String host, String port, String basePath)
- **Description** : Initialise une configuration réseau avec les détails spécifiés.
- **Paramètres** :
    - `protocol` : Protocole de communication (HTTPS ou WSS).
    - `host` : Hôte du serveur.
    - `port` : Port du serveur.
    - `basePath` : Chemin de base de l'application.

## Méthodes
### buildHttpUrl()
- **Description** : Construit une URL complète basée sur la configuration HTTP.
- **Retourne** : URL formée.

### buildWsUrl(int gameId)
- **Description** : Construit une URL WebSocket pour un jeu spécifique, basée sur la configuration WebSocket.
- **Paramètres** :
    - `gameId` : Identifiant du jeu.
- **Retourne** : URL WebSocket formée pour le jeu spécifié.

## Utilisation
```java
String urlHttps = NetworkConfig.LOCAL_HTTPS.buildHttpUrl();
String urlWss = NetworkConfig.LOCAL_WSS.buildWsUrl(123); // Pour le jeu avec l'ID 123
System.out.println("HTTPS URL: " + urlHttps);
System.out.println("WebSocket URL: " + urlWss);
