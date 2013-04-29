package database

import play.api.db.DB
import anorm._
import play.api.Play.current

object DbAdmin {
  def createTables {
    createTableCountry
    createTableUser
    createTableHelpRequest
    createTableHelpReply
    createTableSubscriptionToHelpReplies
    createTableReferenceRating
    createTableReference
    createTableMessage
  }

  def dropTables {
    dropTableMessage
    dropTableReference
    dropTableReferenceRating
    dropTableSubscriptionToHelpReplies
    dropTableHelpReply
    dropTableHelpRequest
    dropTableUser
    dropTableCountry
  }

  def initData {
    initDataCountry
    initDataReferenceRating
  }

  private def createTableCountry {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `country` (
          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
          `name` varchar(45) NOT NULL,
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableUser {
    DB.withConnection {
      implicit c =>

        val query = """
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
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableHelpRequest {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `help_request`(
          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
          `requester_id` int(10) unsigned NOT NULL,
          `title` varchar(100) NOT NULL,
          `description` text NOT NULL,
          `creation_date` datetime NOT NULL,
          `expiry_date` date NOT NULL,
          primary key (`id`),
          constraint `fk_requester` foreign key (`requester_id`) references `user`(`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableHelpReply {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `help_reply`(
          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
          `request_id` int(10) unsigned NOT NULL,
          `replier_id` int(10) unsigned NOT NULL,
          `text` text NOT NULL,
          `creation_date` datetime NOT NULL,
          primary key (`id`),
          constraint `fk_request` foreign key (`request_id`) references `help_request`(`id`),
          constraint `fk_replier` foreign key (`replier_id`) references `user`(`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableSubscriptionToHelpReplies {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `subscription_to_help_replies`(
          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
          `request_id` int(10) unsigned NOT NULL,
          `subscriber_id` int(10) unsigned NOT NULL,
          primary key (`id`),
          constraint `fk_request1` foreign key (`request_id`) references `help_request`(`id`),
          constraint `fk_subscriber` foreign key (`subscriber_id`) references `user`(`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableReferenceRating {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `reference_rating` (
          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
          `label` varchar(10) NOT NULL,
          primary key (`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableReference {
    DB.withConnection {
      implicit c =>

        val query = """
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
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableMessage {
    DB.withConnection {
      implicit c =>

        val query = """
          Create table `message` (
            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
            `from_user_id` int(10) unsigned NOT NULL,
            `to_user_id` int(10) unsigned NOT NULL,
            `title` varchar(100) DEFAULT NULL,
            `text` text NOT NULL,
            `creation_date` datetime NOT NULL,
            `reply_to_message_id` int(10) unsigned DEFAULT NULL,
            primary key (`id`),
            constraint `fk_from_user1` foreign key (`from_user_id`) references `user`(`id`),
            constraint `fk_to_user1` foreign key (`to_user_id`) references `user`(`id`)
          ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def dropTableMessage {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists message;").executeUpdate()
    }
  }

  private def dropTableReference {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists reference;").executeUpdate()
    }
  }

  private def dropTableReferenceRating {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists reference_rating;").executeUpdate()
    }
  }

  private def dropTableSubscriptionToHelpReplies {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists subscription_to_help_replies;").executeUpdate()
    }
  }

  private def dropTableHelpReply {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists help_reply;").executeUpdate()
    }
  }

  private def dropTableHelpRequest {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists help_request;").executeUpdate()
    }
  }

  private def dropTableUser {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists user;").executeUpdate()
    }
  }

  private def dropTableCountry {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists country;").executeUpdate()
    }
  }

  private def initDataCountry {
    DB.withConnection {
      implicit c =>
        SQL("insert into country(name) values(\"Greece\");").execute()
        SQL("insert into country(name) values(\"Spain\");").execute()
        SQL("insert into country(name) values(\"Sweden\");").execute()
    }
  }

  private def initDataReferenceRating {
    DB.withConnection {
      implicit c =>
        SQL("insert into reference_rating(label) values(\"Negative\");").execute()
        SQL("insert into reference_rating(label) values(\"Neutral\");").execute()
        SQL("insert into reference_rating(label) values(\"Good\");").execute()
        SQL("insert into reference_rating(label) values(\"Great\");").execute()
    }
  }
}
