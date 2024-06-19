# Documentation de l'énumération : `ClientConfig`

## Paquet
`online.caltuli.batch.userInteractionSimulation.config.network`

## Description
`ClientConfig` est une énumération qui définit des configurations pour la gestion de la sécurité des certificats SSL dans les clients réseau utilisés dans les simulations d'interaction utilisateur. Elle permet de configurer facilement le comportement des clients en matière de validation des certificats SSL.

## Valeurs de l'énumération
- `TRUST` : Configure les clients pour faire confiance à tous les certificats, ce qui peut être utile dans des environnements de test.
- `CHECK` : Configure les clients pour vérifier les certificats SSL, ce qui est recommandé pour les environnements de production ou ceux nécessitant une sécurité accrue.

## Attributs
- `trustAllCertificate` : Un booléen qui indique si tous les certificats doivent être automatiquement approuvés.

## Constructeur
### ClientConfig(boolean trustAllCertificate)
- **Description** : Initialise la configuration du client avec la stratégie de gestion des certificats SSL spécifiée.
- **Paramètres** :
    - `trustAllCertificate` : Si `true`, tous les certificats SSL sont approuvés sans vérification.

## Méthodes
### isTrustAllCertificate()
- **Description** : Renvoie un booléen indiquant si la configuration du client est définie pour faire confiance à tous les certificats SSL.

## Utilisation
```java
ClientConfig config = ClientConfig.CHECK;
System.out.println("Trust all certificates: " + config.isTrustAllCertificate());
