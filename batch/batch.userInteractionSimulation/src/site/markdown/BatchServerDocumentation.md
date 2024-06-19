# Documentation de la classe : `BatchServer`

## Paquet
`online.caltuli.batch.userInteractionSimulation`

## Description
`BatchServer` est une classe conçue pour configurer et démarrer un serveur HTTP basique destiné à la simulation de batch et aux tâches de traitement dans un contexte d'application web. Elle initialise une instance `HttpServer` qui écoute sur le port 8000, et la configure avec des contextes spécifiques pour différentes tâches de simulation.

## Dépendances
- `com.sun.net.httpserver.HttpServer` : Utilisé pour créer et gérer un serveur HTTP.
- `online.caltuli.batch.userInteractionSimulation.virtualUsers.AiUser` : Utilisé pour simuler des comportements d'utilisateurs dans des contextes de test.
- `java.net.InetSocketAddress` : Utilisé pour spécifier l'adresse du socket pour le serveur HTTP.
- `org.apache.logging.log4j.Logger` : Utilisé pour la journalisation des informations et des erreurs.

## Méthodes principales
### main(String[] args)
- **Description** : Point d'entrée principal pour démarrer le serveur. Configure et lance le serveur HTTP, prépare les utilisateurs virtuels pour les simulations, et définit les contextes de serveur pour différentes simulations.
- **Paramètres** :
    - `args` : Arguments de ligne de commande (non utilisés ici).

## Utilisation
```java
// Pour démarrer le serveur depuis la ligne de commande :
java online.caltuli.batch.userInteractionSimulation.BatchServer
