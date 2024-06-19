# Documentation de l'énumération : `VirtualUserInformationConfig`

## Paquet
`online.caltuli.batch.userInteractionSimulation.config.virtualUsers`

## Description
`VirtualUserInformationConfig` est une énumération qui fournit des configurations pré-définies pour différents types d'utilisateurs virtuels dans un environnement de simulation de jeu. Chaque instance de cette énumération contient des informations d'identification et des messages spécifiques qui peuvent être utilisés pour simuler des comportements d'utilisateur distincts.

## Valeurs de l'énumération
- `AI_USER` : Configure un utilisateur virtuel intelligent avec des informations d'identification spécifiques.
- `RANDOM_USER` : Configure un utilisateur virtuel aléatoire avec des informations d'identification moins sophistiquées.

## Attributs
- `username` : Le nom d'utilisateur pour l'utilisateur virtuel.
- `password` : Le mot de passe pour l'utilisateur virtuel.
- `message` : Un message descriptif associé à l'utilisateur virtuel.
- `email` : L'adresse email associée à l'utilisateur virtuel.

## Constructeur
### VirtualUserInformationConfig(String username, String password, String message, String email)
- **Description** : Initialise une configuration d'utilisateur virtuel avec des détails spécifiques.
- **Paramètres** :
    - `username` : Le nom d'utilisateur de l'utilisateur virtuel.
    - `password` : Le mot de passe de l'utilisateur virtuel.
    - `message` : Un message personnalisé pour l'utilisateur virtuel.
    - `email` : L'adresse email de l'utilisateur virtuel.

## Méthodes
### getUsername(), getPassword(), getMessage(), getEmail()
- **Description** : Méthodes d'accès pour obtenir le nom d'utilisateur, le mot de passe, le message et l'email de la configuration.

## Utilisation
```java
VirtualUserInformationConfig aiConfig = VirtualUserInformationConfig.AI_USER;
System.out.println("Username: " + aiConfig.getUsername());
System.out.println("Message: " + aiConfig.getMessage());
