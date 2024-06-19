# Documentation de la classe : `CoordinatesKeyDeserializer`

## Paquet
`online.caltuli.batch.userInteractionSimulation.jsonUtils`

## Description
`CoordinatesKeyDeserializer` est une classe qui étend `KeyDeserializer` de la bibliothèque Jackson pour personnaliser la désérialisation des clés JSON en objets `Coordinates`. Cette classe est utilisée pour interpréter des chaînes de caractères formatées en paires "x,y" en tant qu'objets `Coordinates`.

## Dépendances
- `com.fasterxml.jackson.databind.KeyDeserializer` : Classe de base pour la désérialisation des clés.
- `online.caltuli.model.Coordinates` : Classe utilisée pour représenter les coordonnées dans un espace bidimensionnel.

## Méthodes
### deserializeKey(String key, DeserializationContext ctxt)
- **Description** : Désérialise une clé JSON sous forme de chaîne en un objet `Coordinates`.
- **Paramètres** :
    - `key` : La clé JSON à désérialiser, qui doit être formatée comme "x,y".
    - `ctxt` : Le contexte de désérialisation.
- **Exceptions** :
    - `IOException` : Lancée si une erreur d'entrée/sortie se produit lors de la désérialisation.
    - `JsonProcessingException` : Lancée si une erreur se produit dans le traitement du JSON.
- **Retourne** : Un objet `Coordinates` représentant les coordonnées désérialisées ou `null` si la clé est invalide ou vide.

## Utilisation
```java
String jsonKey = "10,20";
CoordinatesKeyDeserializer deserializer = new CoordinatesKeyDeserializer();
Coordinates coordinates = (Coordinates) deserializer.deserializeKey(jsonKey, null);
System.out.println("Deserialized coordinates: " + coordinates);
