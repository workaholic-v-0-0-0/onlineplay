# Spécification de Classe : Registration

## Package

`online.caltuli.webapp.servlet`

## Description

La servlet `Registration` gère la logique d'enregistrement des nouveaux utilisateurs
dans l'application `Onlineplay`. Elle traite les requêtes HTTP GET pour afficher le
formulaire d'inscription et les requêtes HTTP POST pour soumettre le formulaire
d'inscription.

## Dépendances

### Dépendances Internes
- `online.caltuli.business.exception.BusinessException` : Gère les exceptions liées
  à la logique métier lors de l'enregistrement.
- `online.caltuli.business.UserManager` : Classe du module `business` pour gérer
  les opérations liées aux utilisateurs ; utilisé pour enregistrer un nouvel
  utilisateur dans la base de données.
- `online.caltuli.model.User` : Représente l'entité utilisateur.
- `online.caltuli.model.exceptions.user.UserException` : Exceptions spécifiques à
  l'entité utilisateur.

### Dépendances Tierces
- `jakarta.servlet.ServletException`, `jakarta.servlet.http.HttpServlet`,
  `HttpServletRequest`, `HttpServletResponse` : Composants de Jakarta EE pour gérer
  les requêtes et réponses HTTP.
- `jakarta.inject.Inject` : Utilisé pour l'injection de dépendances.

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

- `Registration()` : Constructeur par défaut.

## Méthodes

- **doGet(HttpServletRequest request, HttpServletResponse response)** :
    - Affiche le formulaire d'inscription en redirigeant vers `registration.jsp`.

- **doPost(HttpServletRequest request, HttpServletResponse response)** :
    - Traite les données du formulaire d'inscription.
    - Récupère les paramètres `username`, `password`, `email`, et `message` du formulaire.
    - Utilise `userManager.registerUser` pour tenter d'enregistrer le nouvel utilisateur.
    - Gère les cas de succès et d'erreurs d'enregistrement en définissant des attributs de
      requête appropriés pour informer l'utilisateur.
    - Redirige vers `registration.jsp`, affichant le résultat de la tentative d'enregistrement.

## Gestion des Exceptions

- `BusinessException` et `UserException` sont capturées et traitées dans `doPost` pour
  gérer les erreurs liées à l'enregistrement, telles que les violations de contraintes
  de la base de données ou les validations spécifiques au domaine métier.

## Points d'Intégration

- La servlet dépend étroitement de `UserManager` pour la logique d'enregistrement
  des utilisateurs et de `registration.jsp` pour l'affichage.

## Diagrammes

[TO DO]

## Exemples d'Utilisation

### Déploiement de la Servlet

La servlet `Registration` est configurée pour gérer les requêtes HTTP soit via
le fichier `web.xml`, soit par annotations.

#### Via `web.xml`

Pour enregistrer la servlet `Registration`, ajoutez cette configuration dans
`web.xml` :

    <servlet>
        <servlet-name>Registration</servlet-name>
        <servlet-class>online.caltuli.webapp.servlet.gui.Registration</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Registration</servlet-name>
        <url-pattern>/register</url-pattern>
    </servlet-mapping>

Cela configure `Registration` pour répondre aux requêtes sur `/register`.

#### Via Annotations

Utilisez l'annotation `@WebServlet` dans le code de `Registration` pour spécifier
son chemin :

    @WebServlet("/register")
    public class Registration extends HttpServlet {
        // Reste de la classe
    }

### Traitement des Requêtes GET

Lorsqu'une requête GET est reçue, typiquement lors de l'accès au formulaire
d'inscription, `doGet` redirige l'utilisateur vers `registration.jsp` :

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
    }

### Traitement des Requêtes POST

Après la soumission du formulaire d'inscription, `doPost` traite la requête POST.
Elle extrait les données du formulaire, tente d'enregistrer un nouvel utilisateur
via `userManager.registerUser`, et gère les résultats ou exceptions :

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        try {
            User user = userManager.registerUser(username, password, email, message);
            if (user == null) {
                request.setAttribute("registrationProblemEncountered", "Username " + username + " already used.");
            } else {
                request.setAttribute("hasJustBeenRegistered", username);
            }
        } catch (BusinessException | UserException e) {
            request.setAttribute("registrationProblemEncountered", e.getMessage());
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
    }

### Description de `registration.jsp`

Cette JSP (`registration.jsp`) constitue le formulaire d'inscription pour les nouveaux
utilisateurs de l'application web `Onlineplay`. Elle utilise l'encodage "UTF-8" et
les balises core de Jakarta via le préfixe "c".

#### Fonctionnalités Clés

- **Formulaire d'Inscription** : Fournit des champs de saisie pour le nom
  d'utilisateur, le mot de passe, l'email, et un message optionnel. Les données
  sont soumises à la servlet `Registration` via une requête POST.

- **Navigation** : Contient des liens vers la page d'accueil (`home`), la page
  d'authentification (`authentication`), et la déconnexion (`log out`), ce dernier
  apparaissant seulement si un utilisateur est actuellement connecté.

- **Gestion des Erreurs** : Utilise des blocs `<c:if>` pour afficher des messages
  d'erreur ou de succès suivant la tentative d'inscription. Par exemple, si le nom
  d'utilisateur choisi est déjà pris ou si l'inscription est réussie.

#### Sécurité

- **Échappement de Caractères** : Emploie `<c:out>` pour l'affichage de tout
  contenu dynamique, prévenant ainsi les vulnérabilités XSS en échappant
  automatiquement les caractères spéciaux.

#### Points d'Intégration

- **Variables de Session** : Affiche les informations détaillées sur
  `sessionScope.userConnection` et `sessionScope.user` pour le débogage,
  incluant l'ID de connexion, l'adresse IP, l'horodatage, etc. Ces informations
  sont utiles pour vérifier l'état de connexion de l'utilisateur durant le
  développement.

#### Commentaires pour le Débogage

- La page inclut des sections dédiées au débogage encadrées par `<!-- begin to 
  debug -->` et `<!-- end to debug -->`. Elles sont destinées à être utilisées
  durant le développement pour afficher des informations de diagnostic sur les
  objets de session. Il est recommandé de les retirer ou de les commenter dans
  l'environnement de production pour des raisons de sécurité et de performance.

