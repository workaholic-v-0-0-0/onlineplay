# Documentation de la classe : `HttpClientSSLContext`

## Paquet
`online.caltuli.batch.userInteractionSimulation.clients`

## Description
`HttpClientSSLContext` est une classe conçue pour gérer les configurations SSL pour les clients HTTP utilisés dans les simulations d'interactions utilisateur. Elle permet la création d'un client HTTP avec des paramètres spécifiques de sécurité SSL, y compris la gestion des certificats.

## Dépendances
- `java.net.http.HttpClient` : Utilisé pour créer et gérer des requêtes HTTP.
- `javax.net.ssl.*` : Pour configurer et gérer le contexte SSL des connexions HTTP.
- `java.security.*` : Utilisé pour la gestion des exceptions liées à la sécurité et aux certificats.
- `java.net.CookieManager` : Gère les cookies pour les sessions HTTP.

## Attributs
- `httpClient` : Client HTTP configuré avec le contexte SSL approprié.
- `url` : URL de base pour les requêtes HTTP.

## Constructeurs
### HttpClientSSLContext(NetworkConfig httpsNetworkConfig, ClientConfig clientConfig)
- **Description** : Initialise le contexte SSL basé sur la configuration réseau et les préférences du client.
- **Exceptions** :
    - `NoSuchAlgorithmException`
    - `KeyManagementException`
    - `KeyStoreException`

## Méthodes
### cleanupCookies()
- **Description** : Nettoie tous les cookies stockés dans le gestionnaire de cookies du client HTTP.
- **Retourne** : Void.

### sendGetRequest(String urlSuffix)
- **Description** : Envoie une requête GET à une URL spécifiée et retourne la réponse.
- **Paramètres** :
    - `urlSuffix` : Suffixe d'URL pour compléter l'URL de base pour la requête.
- **Retourne** : `String` contenant le corps de la réponse HTTP.

### sendPostRequest(String urlSuffix, String postParams)
- **Description** : Envoie une requête POST à une URL spécifiée avec des paramètres donnés et retourne la réponse.
- **Paramètres** :
    - `urlSuffix` : Suffixe d'URL pour la requête.
    - `postParams` : Paramètres à inclure dans le corps de la requête POST.
- **Retourne** : `String` contenant le corps de la réponse HTTP.

### getHttpClient()
- **Description** : Retourne l'instance du client HTTP configurée.
- **Retourne** : `HttpClient`.

## Utilisation
```java
NetworkConfig httpsConfig = new NetworkConfig(...);
ClientConfig clientConfig = new ClientConfig(...);
HttpClientSSLContext sslContext = new HttpClientSSLContext(httpsConfig, clientConfig);
String response = sslContext.sendGetRequest("exampleEndpoint");
