<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
    <p>
        Error : ${errorMessage}
    </p>
</body>
</html>

### Description de `error.jsp`

La page JSP `error.jsp` est utilisée pour afficher des messages d'erreur aux utilisateurs.
Elle est conçue pour afficher un message d'erreur qui est passé à la page via un attribut
de requête nommé `errorMessage`.

#### Fonctionnalités Clés

- **Affichage de Message d'Erreur** : Affiche le message d'erreur récupéré de la session utilisateur
  ou un message par défaut si aucun message spécifique n'est trouvé.

#### Code de `error.jsp`

```html
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
    <p>Error : ${errorMessage}</p>
</body>
</html>

#### Sécurité

L'utilisation de l'expression ${errorMessage} assure que le message d'erreur est échappé
correctement pour prévenir les attaques XSS (Cross-Site Scripting).

#### Points d'Intégration

La servlet Error est responsable de définir l'attribut errorMessage dans l'objet requête
avant de transférer la requête vers error.jsp. Cette page affiche alors dynamiquement
le contenu de cet attribut.

