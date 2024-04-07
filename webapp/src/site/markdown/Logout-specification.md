# Spécification de classe : `Logout`

## Package
`online.caltuli.webapp.servlet`

## Description
La classe `Logout` gère les requêtes HTTP GET et POST pour déconnecter les utilisateurs. Elle invalide la session en cours et redirige l'utilisateur vers la page d'accueil.

## Dépendances
- `jakarta.inject.Inject` : Utilisé pour l'injection de dépendances.
- `online.caltuli.business.UserManager` : Pour gérer les opérations liées aux utilisateurs, notamment la déconnexion.
- `org.apache.logging.log4j.Logger` : Pour logger les activités et potentielles erreurs lors de la déconnexion.
- `jakarta.servlet.http.*` : Pour la gestion des requêtes et réponses HTTP, ainsi que la session.

## Attributs
- `userManager` : Instance de `UserManager` injectée permettant la déconnexion des utilisateurs.
- `logger` : Utilisé pour le logging des activités de déconnexion.

## Constructeurs
- `Logout()` : Constructeur par défaut.

## Méthodes
### doGet(HttpServletRequest request, HttpServletResponse response)
Traite les requêtes GET en déconnectant l'utilisateur, en invalidant la session et en redirigeant vers la page d'accueil.

### doPost(HttpServletRequest request, HttpServletResponse response)
Redirige les requêtes POST vers `doGet` pour réutiliser la logique de déconnexion.

## Exemples d'Utilisation
### Déploiement de la Servlet
La servlet `Logout` peut être déployée via `web.xml` ou par annotations pour intercepter les requêtes de déconnexion.

#### Via `web.xml`
    <servlet>
        <servlet-name>Logout</servlet-name>
        <servlet-class>online.caltuli.webapp.servlet.gui.Logout</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Logout</servlet-name>
        <url-pattern>/logout</url-pattern>
    </servlet-mapping>

#### Via Annotations
    @WebServlet("/logout")
    public class Logout extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            HttpSession session = request.getSession(false);
            if (session != null) {
                userManager.disconnectUser(((User) session.getAttribute("user")).getId());
                session.invalidate();
            }
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : "home");
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request, response);
        }
    }

### Traitement des Requêtes GET

Quand une requête GET est reçue, Logout invalide la session de l'utilisateur 
pour déconnecter et redirige vers la page précédente ou vers la page d'accueil 
si le référent n'est pas spécifié.

### Traitement des Requêtes POST


Cette spécification fournit les détails sur la manière dont la servlet `Logout` 
est définie, déployée, et son fonctionnement, le tout en un seul bloc pour une 
intégration facile dans un document markdown.

