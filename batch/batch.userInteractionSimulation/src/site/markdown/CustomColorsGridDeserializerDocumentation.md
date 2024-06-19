# Documentation de la classe : `CustomColorsGridDeserializer`

## Paquet
`online.caltuli.batch.userInteractionSimulation.jsonUtils`

## Description
`CustomColorsGridDeserializer` est une classe qui étend `JsonDeserializer` de la bibliothèque Jackson pour personnaliser la désérialisation des grilles de couleurs. Elle est utilisée pour transformer des données JSON en une carte (`Map`) associant des objets `Coordinates` à des valeurs `CellState`.

## Dépendances
- `com.fasterxml.jackson.databind.JsonDeserializer` : Classe de base pour la désérialisation JSON.
- `online.caltuli.model.Coordinates` : Classe utilisée pour représenter des coordonnées dans une grille.
- `online.caltuli.model.CellState` : Énumération représentant les états possibles d'une cellule dans la grille.

## Méthodes
### deserialize(JsonParser p, DeserializationContext ctxt)
- **Description** : Désérialise un objet JSON en une `Map` de `Coordinates` à `CellState`.
- **Paramètres** :
    - `p` : Le parser JSON utilisé pour lire l'arbre JSON.
    - `ctxt` : Contexte de désérialisation fournissant des informations de configuration.
- **Exceptions** :
    - `IOException` : Lancée si une erreur d'entrée/sortie se produit lors de la lecture du JSON.
    - `JsonProcessingException` : Lancée si une erreur se produit dans le traitement du JSON.
- **Retourne** : Une `Map<Coordinates, CellState>` représentant la grille de couleurs désérialisée.

## Utilisation
```java
JsonParser parser = ...; // Configuration du parser
CustomColorsGridDeserializer deserializer = new CustomColorsGridDeserializer();
Map<Coordinates, CellState> grid = deserializer.deserialize(parser, null);
System.out.println("Deserialized grid: " + grid);
