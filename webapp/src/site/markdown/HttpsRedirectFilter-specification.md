# Spécification de classe : `HttpsRedirectFilter`

## Package
`online.caltuli.webapp.filter`

## Description
Le `HttpsRedirectFilter` est un filtre qui redirige les requêtes HTTP vers HTTPS, améliorant ainsi la sécurité de l'application. Ce filtre s'assure que toutes les interactions avec le serveur se font via une connexion sécurisée.

## Dépendances

### Dépendances Internes

- N/A

### Dépendances Tierces

- `jakarta.servlet.Filter`: Interface pour la création de filtres dans les applications Jakarta EE.
- `jakarta.servlet.http.HttpServletRequest`: Pour manipuler les requêtes HTTP entrantes.
- `jakarta.servlet.http.HttpServletResponse`: Pour manipuler les réponses HTTP.

## Attributs

- Ce filtre n'utilise pas explicitement d'attributs définis par l'utilisateur.

## Constructeurs

- Le filtre n'a pas de constructeur explicite et utilise le constructeur par défaut.

## Méthodes

### doFilter(ServletRequest req, ServletResponse res, FilterChain chain)

- **Description**: Intercepte les requêtes HTTP entrantes, vérifie si la requête est sécurisée (HTTPS). Si la requête est non sécurisée (HTTP), elle est redirigée vers HTTPS en construisant une URL sécurisée basée sur le nom du serveur et l'URI de la requête. Si la requête contient une chaîne de requête, elle est également incluse dans l'URL de redirection.

## Diagrammes

[TO DO]

## Exemples d'Utilisation

### Configuration de `web.xml`

Pour activer ce filtre, vous devez le déclarer et le mapper dans votre fichier `web.xml` de votre application Jakarta EE. Voici un exemple de déclaration et de mapping :

```xml
<filter>
    <filter-name>HttpsRedirectFilter</filter-name>
    <filter-class>online.caltuli.webapp.filter.HttpsRedirectFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HttpsRedirectFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```