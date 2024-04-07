# Spécification de classe : `Authentication`

## Package
`online.caltuli.webapp.servlet`

## Description
La classe `Authentication` est une servlet responsable de gérer les requêtes HTTP GET et POST pour l'authentification des utilisateurs. Elle affiche le formulaire d'authentification et traite les soumissions pour authentifier les utilisateurs.

## Dépendances

### Dépendances Internes

- `online.caltuli.business.UserManager` : Utilisé pour authentifier les utilisateurs.
- `online.caltuli.model.User` : Représente l'utilisateur tentant de s'authentifier.
- `online.caltuli.model.UserConnection` : Contient les informations de connexion de l'utilisateur.

### Dépendances Tierces

- `jakarta.servlet.http.HttpServlet`, `HttpServletRequest`, `HttpServletResponse`, `HttpSession` : Utilisés pour la gestion des requêtes et réponses HTTP et la session de l'utilisateur.
- `jakarta.inject.Inject` : Pour l'injection de `UserManager`.
- `org.apache.logging.log4j.Logger` : Pour logger les activités et erreurs.

## Attributs

- `userManager` : Cette instance de `UserManager`, obtenue par injection CDI, est
  responsable de la gestion des opérations liées aux utilisateurs, comme l'enregistrement
  de nouveaux utilisateurs, la mise à jour des informations utilisateur, et la validation
  des identifiants utilisateur. L'annotation `@Inject` facilite cette injection,
  démontrant l'application des principes de CDI pour encourager un couplage faible,
  augmenter la modularité et améliorer la testabilité par l'externalisation de la
  création des instances. Avec l'annotation `@ApplicationScoped`, `UserManager` est
  déclaré comme un bean à portée d'application, ce qui signifie qu'une seule instance
  est créée et utilisée partout dans l'application. Cette unique instance partagée aide
  à optimiser l'utilisation des ressources et à assurer l'uniformité des opérations
  utilisateurs à travers l'application. L'adoption de `@ApplicationScoped` reflète
  l'objectif d'avoir un ensemble de services utilisateur cohérents et réutilisables
  sans nécessiter la création répétée d'instances. La considération pour la
  synchronisation devient pertinente lors de l'accès à ces services partagés en
  environnement multithread pour éviter les conflits d'accès concurrents.
- `logger` : Instance de `Logger` utilisée pour enregistrer les activités de la
  servlet.

## Constructeurs

- `Authentication()` : Constructeur par défaut.

## Méthodes

### doGet(HttpServletRequest request, HttpServletResponse response)

Redirige vers `authentication.jsp`, le formulaire d'authentification.

### doPost(HttpServletRequest request, HttpServletResponse response)

Traite la soumission du formulaire d'authentification, authentifie l'utilisateur et met à jour la session en conséquence.

## Diagrammes

[TO DO]

## Exemples d'Utilisation

### Déploiement de la Servlet

La servlet `Authentication` peut être déployée et configurée pour répondre aux requêtes HTTP via le fichier `web.xml` ou par annotations directement dans le code.

#### Via `web.xml`

Dans le fichier `web.xml`, ajoutez la configuration suivante pour enregistrer la servlet `Authentication` :

    <servlet>
        <servlet-name>Authentication</servlet-name>
        <servlet-class>online.caltuli.webapp.servlet.gui.Authentication</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Authentication</servlet-name>
        <url-pattern>/authentication</url-pattern>
    </servlet-mapping>

Cette configuration spécifie que la servlet `Authentication` doit répondre aux requêtes sur le chemin `/authentication`.

#### Via Annotations

Dans le code de la classe `Authentication`, vous pouvez spécifier le mappage de la servlet en utilisant l'annotation `@WebServlet` :

    @WebServlet("/authentication")
    public class Authentication extends HttpServlet {
        // Reste de la classe
    }

### Traitement des Requêtes GET

Lorsqu'une requête GET est reçue pour `/authentication`, la servlet redirige l'utilisateur vers la page JSP `authentication.jsp` pour l'affichage du formulaire d'authentification.

### Traitement des Requêtes POST

Pour les requêtes POST à `/authentication`, la servlet traite la soumission du formulaire d'authentification :

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Logique d'authentification et de gestion de session
    }

### Description de `authentication.jsp`

Cette JSP (`authentication.jsp`) sert de page d'authentification pour l'application web `Onlineplay`. Elle est configurée pour utiliser l'encodage "UTF-8" et les balises core de Jakarta à travers le préfixe "c".

#### Fonctionnalités Clés

- **Formulaire d'Authentification** : Propose des champs pour le nom d'utilisateur et le mot de passe.
- **Gestion des Messages** : Affiche les messages d'erreur ou de confirmation d'authentification.

#### Sécurité

- Utilise `<c:out>` pour l'affichage sécurisé du contenu dynamique, prévenant les risques d'attaques XSS.

#### Points d'Intégration

- **Gestion de Session** : Utilise `sessionScope` pour afficher ou gérer les informations de l'utilisateur authentifié.

#### Commentaires pour le Débogage

- Inclut des sections marquées pour le débogage qui peuvent être utiles en développement mais devraient être retirées ou commentées en production.
