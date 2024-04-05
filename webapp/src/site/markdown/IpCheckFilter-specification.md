# Spécification de classe : `IPCheckFilter`

## Package
`online.caltuli.webapp.filter`

## Description
Le `IPCheckFilter` est un filtre conçu pour vérifier si l'adresse IP d'une requête n'est pas interdite, en extrayant cette information de l'attribut `userConnection` dans la session. Si l'IP est interdite, la requête est redirigée vers une page d'erreur avec un message approprié.

## Dépendances

### Dépendances Internes
- `online.caltuli.model.UserConnection`: Utilisé pour accéder à l'information concernant la permission associée à la connexion de l'utilisateur.

### Dépendances Tierces
- `jakarta.servlet.Filter`: Interface pour la création de filtres dans les applications Jakarta EE.
- `jakarta.servlet.http.HttpServletRequest` et `HttpServletResponse`: Pour manipuler les requêtes et réponses HTTP.
- `org.apache.logging.log4j.Logger`: Pour le logging des actions et des erreurs.

## Attributs
- `logger`: Logger pour enregistrer les activités et les erreurs du filtre.

## Constructeurs
- Pas de constructeur explicite autre que celui par défaut.

## Méthodes

### doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
- **Description**: Interroge l'attribut `userConnection` dans la session de la requête HTTP pour déterminer si l'adresse IP est autorisée. Si l'IP est interdite, redirige vers la page d'erreur avec un message spécifique.

## Diagrammes
[TO DO]

## Exemples d'Utilisation

### Configuration de `web.xml`
Pour activer ce filtre dans une application Jakarta EE, ajoutez sa déclaration et son mapping dans le fichier `web.xml` comme suit :

```xml
<filter>
    <filter-name>IPCheckFilter</filter-name>
    <filter-class>online.caltuli.webapp.filter.IPCheckFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>IPCheckFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

Cette configuration garantit que toutes les requêtes passant par le serveur sont évaluées par 
le filtre pour vérifier si l'adresse IP est autorisée à accéder à l'application.

## Sécurité

L'implémentation de ce filtre améliore la sécurité de l'application en restreignant l'accès basé sur les adresses IP, 
protégeant ainsi contre les accès non autorisés.


