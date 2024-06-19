# Documentation de la classe : `CustomCoordinatesDeserializer`

## Paquet
`online.caltuli.batch.userInteractionSimulation.jsonUtils`

## Description
`CustomCoordinatesDeserializer` est une classe qui étend `JsonDeserializer` de Jackson pour personnaliser la désérialisation des objets `Coordinates` à partir de données JSON. Elle est conçue pour interpréter des structures JSON spécifiques où les coordonnées sont représentées par des clés "x" et "y".

## Dépendances
- `com.fasterxml.jackson.databind.JsonDeserializer` : Classe de base pour la désérialisation JSON.
- `online.caltuli.model.Coordinates` : Classe utilisée pour représenter des coordonnées dans un espace bidimensionnel.

## Méthodes
### deserialize(JsonParser p, DeserializationContext ctxt)
- **Description** : Désérialise un objet JSON en un objet `Coordinates`.
- **Paramètres** :
    - `p` : Le parser JSON utilisé pour lire l'arbre JSON.
    - `ctxt` : Contexte de désérialisation fournissant des informations de configuration.
- **Exceptions** :
    - `IOException` : Lancée si une erreur d'entrée/sortie se produit lors de la lecture du JSON.
    - `JsonProcessingException` : Lancée si une erreur se produit dans le traitement du JSON.
- **Retourne** : Un objet `Coordinates` représentant les coordonnées désérialisées.

## Utilisation
```java
JsonParser parser = ...; // Configuration du parser
CustomCoordinatesDeserializer deserializer = new CustomCoordinatesDeserializer();
Coordinates coordinates = deserializer.deserialize(parser, null);
System.out.println("Deserialized coordinates: " + coordinates);
