use thesolidaritynetwork;

drop table reference;
drop table reference_rating;
drop table help_response;
drop table help_request;
drop table user;
drop table country;
/* drop table file_data;
drop table file_meta_data;

Create table `file_meta_data`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `media_type` varchar(60) NOT NULL,
  `name` varchar(120) NOT NULL,
  `size_bytes` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  primary key (`id`)
) ENGINE=InnoDB DEFAULT charset=utf8;

Create table `file_data`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `meta_data_id` int(10) unsigned NOT NULL,
  `data` blob NOT NULL,
  primary key (`id`),
  constraint `fk_meta_data` foreign key (`meta_data_id`) references `file_meta_data`(`id`)
) ENGINE=InnoDB DEFAULT charset=utf8; */

CREATE TABLE `country` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `street_address` varchar(100) DEFAULT NULL,
  `post_code` varchar(10) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `country_id` int(10) unsigned NOT NULL,
  `description` text DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY (`id`,`username`,`email`) USING BTREE,
  CONSTRAINT `FK_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


Create table `help_request`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `requester_id` int(10) unsigned NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `creation_date` datetime NOT NULL,
  `expiry_date` date NOT NULL,
  primary key (`id`),
  constraint `fk_requester` foreign key (`requester_id`) references `user`(`id`)
) ENGINE=InnoDB DEFAULT charset=utf8;


Create table `help_response`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `request_id` int(10) unsigned NOT NULL,
  `responder_id` int(10) unsigned NOT NULL,
  `text` text NOT NULL,
  `creation_date` datetime NOT NULL,
  primary key (`id`),
  constraint `fk_request` foreign key (`request_id`) references `help_request`(`id`),
  constraint `fk_responder` foreign key (`responder_id`) references `user`(`id`)
) ENGINE=InnoDB DEFAULT charset=utf8;

Create table `reference_rating` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(10) NOT NULL,
  primary key (`id`)
) ENGINE=InnoDB DEFAULT charset=utf8;

Create table `reference` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `from_user_id` int(10) unsigned NOT NULL,
  `to_user_id` int(10) unsigned NOT NULL,
  `was_helped` boolean NOT NULL,
  `rating_id` int(10) unsigned NOT NULL,
  `text` text NOT NULL,
  `creation_date` datetime NOT NULL,
  primary key (`id`),
  constraint `fk_from_user` foreign key (`from_user_id`) references `user`(`id`),
  constraint `fk_to_user` foreign key (`to_user_id`) references `user`(`id`),
  constraint `fk_rating` foreign key (`rating_id`) references `reference_rating`(`id`)
) ENGINE=InnoDB DEFAULT charset=utf8;

insert into country(name) values("Greece");
insert into country(name) values("Spain");
insert into country(name) values("Sweden");

select * from country;

insert into user(first_name, last_name, username, email, password, street_address, post_code, city, country_id, creation_date)
values("Christophe", "Bram", "blackbird", "cbramdit@gmail.com", "tigrou", "Heleneborgsgatan 6C", "11732", "Stockholm",
  (select id from country where name = "Sweden"), CURRENT_TIMESTAMP);

select * from user;

insert into reference_rating(label) values("Negative");
insert into reference_rating(label) values("Neutral");
insert into reference_rating(label) values("Good");
insert into reference_rating(label) values("Great");

select * from reference_rating;

insert into reference(from_user_id, to_user_id, was_helped, rating_id, text, creation_date)
values(1, 1, true, (select id from reference_rating where label = "Great"), "Foo ref", current_timestamp);

insert into reference(from_user_id, to_user_id, was_helped, rating_id, text, creation_date)
values(2, 1, false, (select id from reference_rating where label = "Neutral"), "Bar ref", current_timestamp);

select * from reference;

select * from help_request;

select * from help_response;
