# Création de la base de donnée

Pour créer la base de données et les tables nécessaires, exécutez les commandes
suivantes dans le client MySQL :

```sh
../env/sgbdr/mysql/current-client -h localhost -u root -p -P 3307

CREATE DATABASE onlineplay default character set utf8 collate utf8_general_ci;

USE onlineplay;

CREATE TABLE users (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(25) NOT NULL,
	password_hash VARCHAR(128) NOT NULL,
	email VARCHAR(254),
	message VARCHAR(254)
);

CREATE TABLE connections (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ip_address VARCHAR(45) NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	user_id INT,
	is_allowed TINYINT(1),
	FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE games (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    state ENUM('in_progress', 'finished', 'pending', 'cancelled') NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    first_player_id INT,
    second_player_id INT,
    winner_id INT,
    FOREIGN KEY (first_player_id) REFERENCES users(id),
    FOREIGN KEY (second_player_id) REFERENCES users(id)
);

CREATE TABLE moves (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    user_id INT NOT NULL,
    number INT NOT NULL,
    move INT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
