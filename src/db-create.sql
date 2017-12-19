
DROP DATABASE IF EXISTS practice8;

CREATE DATABASE practice8;

USE practice8;

CREATE TABLE users (
  user_id int AUTO_INCREMENT PRIMARY KEY,
  login VARCHAR(25) UNIQUE NOT NULL
);

CREATE TABLE groups (
  group_id int AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(25) UNIQUE NOT NULL
);

CREATE TABLE users_groups(
  user_id INT REFERENCES users(user_id),
  group_id INT REFERENCES groups(group_id)
);

INSERT INTO users (login) VALUES ("admin");
INSERT INTO users (login) VALUES ("client");
INSERT INTO users (login) VALUES ("anonym");

INSERT INTO groups (name) VALUES ("for_admin");
INSERT INTO groups (name) VALUES ("for_client");

INSERT INTO users_groups VALUES (1, 1);
INSERT INTO users_groups VALUES (2, 2);
INSERT INTO users_groups VALUES (3, 2);

