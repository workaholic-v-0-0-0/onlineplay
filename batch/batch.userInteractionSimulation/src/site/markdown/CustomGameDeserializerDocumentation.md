# Documentation de la classe : `CustomGameDeserializer`

## Paquet
`online.caltuli.batch.userInteractionSimulation.jsonUtils`

## Description
`CustomGameDeserializer` est une classe qui étend `JsonDeserializer` pour personnaliser la désérialisation d'objets `Game` à partir de données JSON. Elle permet de convertir une représentation JSON complexe d'un jeu en un objet `Game`, incluant les informations sur les joueurs, l'état du jeu et la grille de couleurs.

## Dépendances
- `com.fasterxml.jackson.databind.JsonDeserializer` : Classe de base pour la désérialisation JSON.
- `online.caltuli.model.*` : Utilise des classes telles que `Game`, `Player`, `Coordinates`, et `CellState`.

## Méthodes
### deserialize(JsonParser p, DeserializationContext ctxt)
- **Description** : Désérialise un objet JSON en un objet `Game`.
- **Paramètres** :
    - `p` : Le parser JSON utilisé pour lire l'arbre JSON.
    - `ctxt` : Contexte de désérialisation fournissant des informations de configuration.
- **Exceptions** :
    - `IOException` : Lancée si une erreur d'entrée/sortie se produit lors de la lecture du JSON.
    - `JsonProcessingException` : Lancée si une erreur se produit dans le traitement du JSON.
- **Retourne** : Un objet `Game` représentant le jeu désérialisé.

## Utilisation
```java
JsonParser parser = ...; // Configuration du parser
CustomGameDeserializer deserializer = new CustomGameDeserializer();
Game game = deserializer.deserialize(parser, null);
System.out.println("Deserialized game: " + game);
