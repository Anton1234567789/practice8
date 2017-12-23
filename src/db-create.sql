
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
  user_id INT,
  group_id INT,
  PRIMARY KEY (user_id, group_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE,
  FOREIGN KEY (group_id) REFERENCES groups(group_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

INSERT INTO users (login) VALUES ("admin");
INSERT INTO users (login) VALUES ("client");
INSERT INTO users (login) VALUES ("anonym");

INSERT INTO groups (name) VALUES ("for_admin");
INSERT INTO groups (name) VALUES ("for_client");

INSERT INTO users_groups VALUES (1, 1);
INSERT INTO users_groups VALUES (2, 2);
INSERT INTO users_groups VALUES (3, 2);

