USE thesolidaritynetwork;

DROP TABLE message;
DROP TABLE reference;
DROP TABLE reference_rating;
DROP TABLE subscription_to_help_responses;
DROP TABLE help_response;
DROP TABLE help_request;
DROP TABLE USER;
DROP TABLE country;

CREATE TABLE `country` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `street_address` VARCHAR(100) DEFAULT NULL,
  `post_code` VARCHAR(10) DEFAULT NULL,
  `city` VARCHAR(45) NOT NULL,
  `country_id` INT(10) UNSIGNED NOT NULL,
  `description` TEXT DEFAULT NULL,
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`,`username`,`email`) USING BTREE,
  CONSTRAINT `FK_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `help_request`(
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `requester_id` INT(10) UNSIGNED NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `expiry_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_requester` FOREIGN KEY (`requester_id`) REFERENCES `user`(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `help_response`(
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id` INT(10) UNSIGNED NOT NULL,
  `responder_id` INT(10) UNSIGNED NOT NULL,
  `text` TEXT NOT NULL,
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_request` FOREIGN KEY (`request_id`) REFERENCES `help_request`(`id`),
  CONSTRAINT `fk_responder` FOREIGN KEY (`responder_id`) REFERENCES `user`(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `subscription_to_help_responses`(
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id` INT(10) UNSIGNED NOT NULL,
  `subscriber_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_request1` FOREIGN KEY (`request_id`) REFERENCES `help_request`(`id`),
  CONSTRAINT `fk_subscriber` FOREIGN KEY (`subscriber_id`) REFERENCES `user`(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `reference_rating` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `reference` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `from_user_id` INT(10) UNSIGNED NOT NULL,
  `to_user_id` INT(10) UNSIGNED NOT NULL,
  `was_helped` BOOLEAN NOT NULL,
  `rating_id` INT(10) UNSIGNED NOT NULL,
  `text` TEXT NOT NULL,
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_from_user` FOREIGN KEY (`from_user_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_to_user` FOREIGN KEY (`to_user_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_rating` FOREIGN KEY (`rating_id`) REFERENCES `reference_rating`(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `message` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `from_user_id` INT(10) UNSIGNED NOT NULL,
  `to_user_id` INT(10) UNSIGNED NOT NULL,
  `title` VARCHAR(100) DEFAULT NULL,
  `text` TEXT NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `reply_to_message_id` INT(10) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_from_user1` FOREIGN KEY (`from_user_id`) REFERENCES `user`(`id`),
  CONSTRAINT `fk_to_user1` FOREIGN KEY (`to_user_id`) REFERENCES `user`(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO country(NAME) VALUES("Greece");
INSERT INTO country(NAME) VALUES("Spain");
INSERT INTO country(NAME) VALUES("Sweden");

SELECT * FROM country;

INSERT INTO USER(first_name, last_name, username, email, PASSWORD, street_address, post_code, city, country_id, creation_date)
VALUES("Christophe", "Bram", "blackbird", "cbramdit@gmail.com", "tigrou", "Heleneborgsgatan 6C", "11732", "Stockholm",
  (SELECT id FROM country WHERE NAME = "Sweden"), CURRENT_TIMESTAMP);

SELECT * FROM USER;

INSERT INTO reference_rating(label) VALUES("Negative");
INSERT INTO reference_rating(label) VALUES("Neutral");
INSERT INTO reference_rating(label) VALUES("Good");
INSERT INTO reference_rating(label) VALUES("Great");

SELECT * FROM reference_rating;

INSERT INTO reference(from_user_id, to_user_id, was_helped, rating_id, TEXT, creation_date)
VALUES(1, 1, TRUE, (SELECT id FROM reference_rating WHERE label = "Great"), "Foo ref", CURRENT_TIMESTAMP);

INSERT INTO reference(from_user_id, to_user_id, was_helped, rating_id, TEXT, creation_date)
VALUES(2, 1, FALSE, (SELECT id FROM reference_rating WHERE label = "Neutral"), "Bar ref", CURRENT_TIMESTAMP);

SELECT * FROM reference;

SELECT * FROM help_request;

SELECT * FROM help_response;

SELECT * FROM subscription_to_help_responses;

SELECT * FROM message;

DELETE FROM USER WHERE email = "panzrkunst@yahoo.fr";

DELETE FROM message;