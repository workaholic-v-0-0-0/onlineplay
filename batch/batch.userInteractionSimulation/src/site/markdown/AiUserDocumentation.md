# Documentation de la classe : `AiUser`

## Package
`online.caltuli.batch.userInteractionSimulation.virtualUsers`

## Description
`AiUser` est une implémentation d'un utilisateur virtuel qui simule des interactions automatisées dans une application de jeu, en utilisant des techniques de décision basées sur l'intelligence artificielle.

## Dépendances
- `online.caltuli.batch.userInteractionSimulation.config.*` : Configuration réseau et informations utilisateur.
- `online.caltuli.business.ai.MinMaxDecisionEngine` : Moteur de décision basé sur l'algorithme MinMax pour simuler des décisions de jeu.
- `online.caltuli.model.GameState` : Utilisé pour déterminer l'état actuel du jeu.

## Constructeurs
### AiUser(VirtualUserInformationConfig, NetworkConfig, NetworkConfig, ClientConfig)
- **Description** : Initialise un `AiUser` avec des configurations spécifiques pour la communication réseau et la simulation utilisateur.
- **Exceptions** :
    - `NoSuchAlgorithmException`
    - `KeyManagementException`
    - `KeyStoreException`

## Méthodes
### doTask()
- **Description** : Méthode principale qui exécute les tâches automatisées de l'utilisateur virtuel, comme s'enregistrer, s'authentifier, proposer des jeux, jouer, et gérer les états de jeu.
- **Exceptions** : `Exception`

## Utilisation
```java
VirtualUserInformationConfig userConfig = new VirtualUserInformationConfig(...);
NetworkConfig httpsConfig = new NetworkConfig(...);
NetworkConfig wssConfig = new NetworkConfig(...);
ClientConfig clientConfig = new ClientConfig(...);
AiUser aiUser = new AiUser(userConfig, httpsConfig, wssConfig, clientConfig);
aiUser.doTask();
