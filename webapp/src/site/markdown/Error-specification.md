# Spécification de classe : `Error`

## Package
`online.caltuli.webapp.servlet`

## Description
Gère l'affichage des messages d'erreur personnalisés en récupérant un message d'erreur depuis la session de l'utilisateur, si disponible, ou en affichant un message par défaut.

## Dépendances

### Dépendances Internes

- `jakarta.servlet.http.HttpServlet` : Base pour la création de servlets HTTP.

### Dépendances Tierces

- `org.apache.logging.log4j.Logger` : Pour le logging des erreurs et des informations.

## Attributs

- `logger` : Logger pour enregistrer les activités et les erreurs de la servlet.

## Constructeurs

- `Error()` : Constructeur par défaut.

## Méthodes

### doGet(HttpServletRequest request, HttpServletResponse response)

- **Description** : Récupère le message d'erreur de la session, le supprime de la session, invalide la session, et redirige vers `error.jsp` avec le message d'erreur.

### doPost(HttpServletRequest request, HttpServletResponse response)

- **Description** : Réutilise `doGet` pour traiter les requêtes POST de la même manière que les requêtes GET.

## Diagrammes

[TO DO]

## Exemples d'Utilisation

### Déploiement de la Servlet

#### Via `web.xml`

```xml

<servlet>
    <servlet-name>Error</servlet-name>
    <servlet-class>online.caltuli.webapp.servlet.gui.Erroronline.caltuli.webapp.servlet.gui.Error</servlet-class>
</servlet>
<servlet-mapping>
<servlet-name>Error</servlet-name>
<url-pattern>/error</url-pattern>
</servlet-mapping>
```

#### Via Annotations
Dans le code de la classe `Error`, vous pouvez spécifier le mappage de la servlet en utilisant l'annotation `@WebServlet` :

    @WebServlet("/error")
    public class Error extends HttpServlet {
    // Implémentation
    }

### Traitement des Requêtes GET

Quand une requête GET est reçue, la servlet récupère l'attribut errorMessage de la session, le présente à l'utilisateur via la JSP error.jsp, puis supprime cet attribut de la session.

### Traitement des Requêtes POST

Les requêtes POST sont redirigées vers la méthode doGet, assurant une gestion unifiée des erreurs, peu importe la méthode HTTP utilisée.

