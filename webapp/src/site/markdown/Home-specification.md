# Spécification de classe : `Home`

## Package
`online.caltuli.webapp.servlet`

## Description
La classe Home est une servlet qui gère les requêtes HTTP GET et POST 
pour la page d'accueil de l'application web. Elle est responsable 
d'afficher la liste des utilisateurs actuellement connectés. Cette 
servlet fait partie de la couche de présentation dans l'architecture 
multi-tiers de l'application, interagissant directement avec les 
utilisateurs finaux.

## Dépendances

### Dépendances Internes

- `online.caltuli.business.exception.BusinessException` : Classe d'exception
  personnalisée du module `business`, utilisée pour gérer les exceptions liées à
  la logique métier de l'application.
- `online.caltuli.business.UserManager` : Classe du module `business` pour gérer
  les opérations liées aux utilisateurs ; utilisée par Home pour récupérer la liste
  des utilisateurs actuellement connectés.
- `online.caltuli.model.UserConnection` : Classe du module `model` représentant
  la connexion d'un utilisateur. Ce module contient les classes de modèle qui
  définissent la structure des données manipulées par l'application.

### Dépendances Tierces

- `jakarta.inject.Inject` : Utilisé pour l'injection de dépendances.
- `jakarta.servlet.ServletException`, `jakarta.servlet.http.HttpServlet`,
  `HttpServletRequest`, `HttpServletResponse`, `HttpSession` : Classes et
  interfaces de Jakarta EE pour gérer les requêtes et réponses HTTP, ainsi que
  les sessions utilisateurs.
- `org.apache.logging.log4j.LogManager`, `Logger` : Classes fournies par Apache
  Logging pour configurer et effectuer le logging des activités et des erreurs
  de l'application.

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

- `Home()` : Constructeur par défaut.

## Méthodes

### doGet(HttpServletRequest request, HttpServletResponse response)

- **Description** : Traite les requêtes GET en récupérant la liste des
  utilisateurs connectés et en transférant la requête vers la page JSP de la
  page d'accueil.
- **Paramètres** :
    - `HttpServletRequest request` : La requête HTTP entrante.
    - `HttpServletResponse response` : La réponse HTTP à envoyer.
- **Exceptions** : `ServletException`, `IOException`

### doPost(HttpServletRequest request, HttpServletResponse response)

- **Description** : Traite les requêtes POST en les redirigeant vers la méthode
  `doGet` pour réutiliser la logique de récupération et d'affichage des
  utilisateurs connectés.
- **Paramètres** :
    - `HttpServletRequest request` : La requête HTTP entrante.
    - `HttpServletResponse response` : La réponse HTTP à envoyer.
- **Exceptions** : `ServletException`, `IOException`

## Diagrammes

[TO DO]

## Exemples d'Utilisation

### Déploiement de la Servlet

La servlet `Home` peut être déployée et configurée pour répondre aux requêtes HTTP via le fichier `web.xml` ou par annotations directement dans le code.

#### Via `web.xml`

Dans le fichier `web.xml`, ajoutez la configuration suivante pour enregistrer la servlet `Home` :

    <servlet>
        <servlet-name>Home</servlet-name>
        <servlet-class>online.caltuli.webapp.servlet.gui.Home</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Home</servlet-name>
        <url-pattern>/home</url-pattern>
    </servlet-mapping>

Cette configuration spécifie que la servlet `Home` doit répondre aux requêtes sur le chemin `/home`.

#### Via Annotations

Dans le code de la classe `Home`, vous pouvez spécifier le mappage de la servlet en utilisant l'annotation `@WebServlet` :

    @WebServlet("/home")
    public class Home extends HttpServlet {
        // Reste de la classe
    }

### Traitement des Requêtes GET

Lorsqu'une requête GET est reçue pour `/home`, la servlet traite la requête comme suit :

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("connectedUserList", userManager.getConnectedUserList());
        this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }

Cette méthode récupère la liste des utilisateurs connectés et la passe à la page JSP `home.jsp` pour l'affichage.

### Traitement des Requêtes POST

Pour les requêtes POST à `/home`, la servlet redirige simplement vers `doGet` pour réutiliser la logique de récupération et d'affichage des utilisateurs connectés :

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

### Description de `home.jsp`

Cette JSP (`home.jsp`) sert de page d'accueil pour l'application web `Onlineplay`.
Elle est configurée pour utiliser l'encodage "UTF-8" et les balises core de Jakarta
à travers le préfixe "c".

#### Fonctionnalités Clés

- **Affichage Conditionnel** : La page utilise `<c:if>` pour afficher des
  informations basées sur la présence d'objets dans `sessionScope`, tel que
  `userConnection` et `user`. Cela inclut l'ID de connexion, l'adresse IP,
  l'horodatage de connexion, l'ID de l'utilisateur, l'autorisation d'accès,
  l'identifiant de l'utilisateur, le nom d'utilisateur, le hash du mot de
  passe, l'email et les messages de l'utilisateur.

- **Liens de Navigation** : Des liens sont fournis pour l'inscription (`Sign up`),
  la connexion (`log in`), la déconnexion (`log out`), l'accès au site du projet
  généré par `maven-site-plugin`, et les sources du projet sur GitHub.

- **Liste des Utilisateurs Connectés** : Un `<c:forEach>` itère sur
  `connectedUserList` pour afficher la liste des noms d'utilisateurs connectés.

#### Sécurité

La page utilise `<c:out>` pour afficher le contenu des variables, ce qui aide à
prévenir les attaques XSS en échappant automatiquement les caractères spéciaux.

#### Points d'Intégration

- **Variables de Session** : `home.jsp` dépend des variables de session
  `userConnection` et `user` pour afficher les informations de l'utilisateur
  et de sa connexion.

- **Liste des Utilisateurs Connectés** : La servlet `Home` doit fournir
  `connectedUserList` à la JSP via `request.setAttribute`.

#### Commentaires pour le Débogage

La JSP inclut des sections marquées `<!-- begin to debug -->` et `<!-- end to 
debug -->` qui affichent des détails sur les objets `sessionScope.userConnection`
et `sessionScope.user` pour le débogage. Ces sections peuvent être supprimées ou
commentées en production pour des raisons de sécurité et de performance.
