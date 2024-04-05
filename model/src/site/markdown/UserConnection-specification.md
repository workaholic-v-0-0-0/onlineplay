# UserConnection Class Specification

## Package
`online.caltuli.model`

## Imports
- `online.caltuli.model.exceptions.userconnection.UserConnectionException`
- `org.apache.logging.log4j.LogManager`
- `org.apache.logging.log4j.Logger`
- `java.sql.Timestamp`

## Description
La classe `UserConnection` enregistre les détails d'une connexion d'utilisateur,
y compris l'adresse IP, un horodatage, l'ID de l'utilisateur et un indicateur
permettant de savoir si la connexion est autorisée.

## Fields
- `private int id`: Identifiant de la connexion.
- `private String ipAddress`: Adresse IP de la connexion.
- `private Timestamp timestamp`: Horodatage de la connexion.
- `private Integer userId`: Identifiant de l'utilisateur associé.
- `private Boolean isAllowed`: Indique si la connexion est autorisée.
- `private static final Logger logger`: Logger pour les informations de logging.

## Constructors
- `public UserConnection()`: Constructeur par défaut.
- `public UserConnection(String ipAddress, Integer userId) throws UserConnectionException`:
  Constructeur qui initialise une nouvelle connexion utilisateur avec les
  informations fournies et l'horodatage courant.

## Methods

### Setters
- `public void setId(int id)`: Définit l'ID de la connexion.
- `public void setIpAddress(String ipAddress)`: Définit l'adresse IP de la connexion.
- `public void setTimestamp(Timestamp timestamp)`: Définit l'horodatage de la connexion.
- `public void setUserId(int userId)`: Définit l'ID de l'utilisateur associé.
- `public void setIsAllowed(Boolean isAllowed)`: Définit si la connexion est autorisée.

### Getters
- `public int getId()`: Retourne l'ID de la connexion.
- `public String getIpAddress()`: Retourne l'adresse IP de la connexion.
- `public Timestamp getTimestamp()`: Retourne l'horodatage de la connexion.
- `public int getUserId()`: Retourne l'ID de l'utilisateur associé.
- `public Boolean getIsAllowed()`: Retourne si la connexion est autorisée.

## Usage
Cette classe est utilisée pour suivre et enregistrer les détails des connexions
des utilisateurs à un système, permettant ainsi une gestion et une surveillance
des accès utilisateurs.
