# User Class Specification

## Package
`online.caltuli.model`

## Imports
- `online.caltuli.model.exceptions.user.*`
- `org.apache.logging.log4j.LogManager`
- `org.apache.logging.log4j.Logger`
- `java.util.ArrayList`
- `java.util.List`

## Description
La classe `User` représente un utilisateur, incluant son identifiant, son nom d'utilisateur,
son hash de mot de passe, son adresse e-mail et un message. Elle intègre des validations
pour chacun de ces champs et lance une `UserException` avec les erreurs de validation
si nécessaire.

## Fields
- `private int id`: Identifiant de l'utilisateur.
- `private String username`: Nom d'utilisateur.
- `private String passwordHash`: Hash du mot de passe.
- `private String email`: Adresse e-mail.
- `private String message`: Message utilisateur.
- `private static final Logger logger`: Logger pour les informations de logging.

## Constructors
- `public User()`: Constructeur par défaut.
- `public User(String username, String passwordHash, String email, String message) throws UserException`:
  Constructeur avec validation des entrées.

## Methods

### Setters
- `public void setId(int id)`: Définit l'ID de l'utilisateur.
- `public void setUsername(String username) throws InvalidUsernameException`:
  Définit le nom d'utilisateur après validation.
- `public void setPasswordHash(String passwordHash) throws InvalidPasswordHashException`:
  Définit le hash du mot de passe après validation.
- `public void setEmail(String email) throws InvalidMailException`:
  Définit l'e-mail après validation.
- `public void setMessage(String message) throws InvalidMessageException`:
  Définit le message après validation.

### Getters
- `public int getId()`: Retourne l'ID de l'utilisateur.
- `public String getUsername()`: Retourne le nom d'utilisateur.
- `public String getPasswordHash()`: Retourne le hash du mot de passe.
- `public String getEmail()`: Retourne l'adresse e-mail.
- `public String getMessage()`: Retourne le message utilisateur.

## Exception Handling
Les méthodes de validation dans le constructeur et les setters lancent des exceptions
spécifiques (`InvalidUsernameException`, `InvalidPasswordHashException`,
`InvalidMailException`, `InvalidMessageException`) si les données fournies ne
respectent pas les critères requis. Les erreurs de validation sont accumulées et
loguées, et une `UserException` est lancée si nécessaire.
