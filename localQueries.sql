use thesolidaritynetwork;

drop table user;
drop table country;

CREATE TABLE `country` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `street_address` varchar(100) NOT NULL,
  `post_code` varchar(10) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `country_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`,`username`,`email`) USING BTREE,
  KEY `FK_country` (`country_id`),
  CONSTRAINT `FK_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


insert into country(name) values("Greece");
insert into country(name) values("Sweden");

select * from user;
select * from country;

insert into user(first_name, last_name, username, email, password, street_address, post_code, city, country_id)
values("Christophe", "Bram", "blackbird", "cbramdit@gmail.com", "tigrou", "Heleneborgsgatan 6C", "11732", "Stockholm",
  (select id from country where name = "Sweden"));
