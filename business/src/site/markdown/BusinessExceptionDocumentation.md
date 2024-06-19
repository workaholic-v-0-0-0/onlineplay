# Documentation de la classe : `BusinessException`

## Package
`online.caltuli.business.exception`

## Description
`BusinessException` est une classe d'exception personnalisée utilisée pour signaler des erreurs spécifiques aux opérations métier dans l'application. Cette exception est utilisée pour encapsuler des erreurs qui ne sont pas liées directement aux systèmes internes mais plutôt aux règles métier ou aux logiques appliquées dans l'application.

## Constructeurs
### BusinessException(String message)
- **Description** : Construit une nouvelle exception avec un message spécifique qui décrit l'erreur.
- **Paramètres** :
    - `message` : Le message décrivant l'erreur.

## Utilisation
```java
try {
    // Code qui pourrait lancer une BusinessException
} catch (BusinessException e) {
    System.out.println(e.getMessage());
}
