package db

import play.api.db.DB
import anorm._
import play.api.Play.current
import models.User

object DbAdmin {
  def createTables() {
    createTableCountry()
    createTableUser()
    createTableHelpRequest()
    createTableHelpReply()
    createTableSubscriptionToHelpReplies()
    createTableReferenceRating()
    createTableReference()
    createTableMessage()
    createTableEmailProcessingHelpRequest()
    createTableEmailProcessingHelpReply()
    createTableEmailProcessingNewAccount()
    createTableEmailProcessingMessage()
  }

  def dropTables() {
    dropTableEmailProcessingMessage()
    dropTableEmailProcessingNewAccount()
    dropTableEmailProcessingHelpReply()
    dropTableEmailProcessingHelpRequest()
    dropTableMessage()
    dropTableReference()
    dropTableReferenceRating()
    dropTableSubscriptionToHelpReplies()
    dropTableHelpReply()
    dropTableHelpRequest()
    dropTableUser()
    dropTableCountry()
  }

  def initData() {
    initDataCountry()
    initDataReferenceRating()
    initDataEmailProcessingHelpRequest()
    initDataEmailProcessingHelpReply()
    initDataEmailProcessingNewAccount()
    initDataEmailProcessingMessage()
  }

  private def createTableCountry() {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `country` (
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `name` varchar(45) NOT NULL,
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableUser() {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `user` (
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `first_name` varchar(45) NOT NULL,
          `last_name` varchar(45) NOT NULL,
          `username` varchar(45) NOT NULL,
          `email` varchar(45) NOT NULL,
          `password` varchar(45) NOT NULL,
          `street_address` varchar(100) DEFAULT NULL,
          `post_code` varchar(10) DEFAULT NULL,
          `city` varchar(45) NOT NULL,
          `country_id` int unsigned NOT NULL,
          `description` text DEFAULT NULL,
          `creation_date` datetime NOT NULL,
          `is_subscribed_to_news` BOOLEAN DEFAULT TRUE,
          `subscription_to_new_help_requests` VARCHAR(45) DEFAULT '""" + User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY + """', /* NONE, EACH_NEW_REQUEST, DAILY, WEEKLY */
          PRIMARY KEY (`id`,`username`,`email`) USING BTREE,
          CONSTRAINT `FK_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableHelpRequest() {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `help_request`(
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `requester_id` int unsigned NOT NULL,
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

  private def createTableHelpReply() {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `help_reply`(
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `request_id` int unsigned NOT NULL,
          `replier_id` int unsigned NOT NULL,
          `text` text NOT NULL,
          `creation_date` datetime NOT NULL,
          primary key (`id`),
          constraint `fk_request` foreign key (`request_id`) references `help_request`(`id`),
          constraint `fk_replier` foreign key (`replier_id`) references `user`(`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableSubscriptionToHelpReplies() {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `subscription_to_help_replies`(
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `request_id` int unsigned NOT NULL,
          `subscriber_id` int unsigned NOT NULL,
          primary key (`id`),
          constraint `fk_request1` foreign key (`request_id`) references `help_request`(`id`),
          constraint `fk_subscriber` foreign key (`subscriber_id`) references `user`(`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableReferenceRating() {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `reference_rating` (
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `label` varchar(10) NOT NULL,
          primary key (`id`)
        ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableReference() {
    DB.withConnection {
      implicit c =>

        val query = """
        Create table `reference` (
          `id` int unsigned NOT NULL AUTO_INCREMENT,
          `from_user_id` int unsigned NOT NULL,
          `to_user_id` int unsigned NOT NULL,
          `was_helped` boolean NOT NULL,
          `rating_id` int unsigned NOT NULL,
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

  private def createTableMessage() {
    DB.withConnection {
      implicit c =>

        val query = """
          Create table `message` (
            `id` int unsigned NOT NULL AUTO_INCREMENT,
            `from_user_id` int unsigned NOT NULL,
            `to_user_id` int unsigned NOT NULL,
            `title` varchar(100) DEFAULT NULL,
            `text` text NOT NULL,
            `creation_date` datetime NOT NULL,
            `reply_to_message_id` int unsigned DEFAULT NULL,
            `is_read` BOOLEAN DEFAULT FALSE,
            primary key (`id`),
            constraint `fk_from_user1` foreign key (`from_user_id`) references `user`(`id`),
            constraint `fk_to_user1` foreign key (`to_user_id`) references `user`(`id`)
          ) ENGINE=InnoDB DEFAULT charset=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableEmailProcessingHelpRequest() {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `email_processing_help_request` (
          `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
          `frequency` VARCHAR(45) DEFAULT '""" + User.NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST + """', /* EACH_NEW_REQUEST, DAILY, WEEKLY */
          `id_of_last_processed_request` int UNSIGNED NOT NULL,
          PRIMARY KEY (`id`)/* ,
          CONSTRAINT `fk_request2` FOREIGN KEY (`id_of_last_processed_request`) REFERENCES `help_request`(`id`)  We omit this constraint because it prevents deleting some HRs */
          ) ENGINE=INNODB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableEmailProcessingHelpReply() {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `email_processing_help_reply` (
          `id_of_last_processed_reply` int UNSIGNED NOT NULL
        ) ENGINE=INNODB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableEmailProcessingNewAccount() {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `email_processing_new_account` (
          `id_of_last_processed_new_account` int UNSIGNED NOT NULL
        ) ENGINE=INNODB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def createTableEmailProcessingMessage() {
    DB.withConnection {
      implicit c =>

        val query = """
        CREATE TABLE `email_processing_message` (
          `id_of_last_processed_message` int UNSIGNED NOT NULL
        ) ENGINE=INNODB DEFAULT CHARSET=utf8;"""

        SQL(query).executeUpdate()
    }
  }

  private def dropTableEmailProcessingMessage() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists email_processing_message;").executeUpdate()
    }
  }

  private def dropTableEmailProcessingNewAccount() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists email_processing_new_account;").executeUpdate()
    }
  }

  private def dropTableEmailProcessingHelpReply() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists email_processing_help_reply;").executeUpdate()
    }
  }

  private def dropTableEmailProcessingHelpRequest() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists email_processing_help_request;").executeUpdate()
    }
  }

  private def dropTableMessage() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists message;").executeUpdate()
    }
  }

  private def dropTableReference() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists reference;").executeUpdate()
    }
  }

  private def dropTableReferenceRating() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists reference_rating;").executeUpdate()
    }
  }

  private def dropTableSubscriptionToHelpReplies() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists subscription_to_help_replies;").executeUpdate()
    }
  }

  private def dropTableHelpReply() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists help_reply;").executeUpdate()
    }
  }

  private def dropTableHelpRequest() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists help_request;").executeUpdate()
    }
  }

  private def dropTableUser() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists user;").executeUpdate()
    }
  }

  private def dropTableCountry() {
    DB.withConnection {
      implicit c =>
        SQL("drop table if exists country;").executeUpdate()
    }
  }

  private def initDataCountry() {
    DB.withConnection {
      implicit c =>
        SQL("insert into country(name) values(\"Greece\");").execute()
        SQL("insert into country(name) values(\"Spain\");").execute()
        SQL("insert into country(name) values(\"Sweden\");").execute()
    }
  }

  private def initDataReferenceRating() {
    DB.withConnection {
      implicit c =>
        SQL("insert into reference_rating(label) values(\"Negative\");").execute()
        SQL("insert into reference_rating(label) values(\"Neutral\");").execute()
        SQL("insert into reference_rating(label) values(\"Good\");").execute()
        SQL("insert into reference_rating(label) values(\"Great\");").execute()
    }
  }

  private def initDataEmailProcessingHelpRequest() {
    DB.withConnection {
      implicit c =>
        SQL("insert into email_processing_help_request(frequency, id_of_last_processed_request) values(\"" + User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY + "\", 0);").execute()
        SQL("insert into email_processing_help_request(frequency, id_of_last_processed_request) values(\"" + User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY + "\", 0);").execute()
        SQL("insert into email_processing_help_request(frequency, id_of_last_processed_request) values(\"" + User.NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST + "\", 0);").execute()
    }
  }

  private def initDataEmailProcessingHelpReply() {
    DB.withConnection {
      implicit c =>
        SQL("insert into email_processing_help_reply(id_of_last_processed_reply) values(0);").execute()
    }
  }

  private def initDataEmailProcessingNewAccount() {
    DB.withConnection {
      implicit c =>
        SQL("insert into email_processing_new_account(id_of_last_processed_new_account) values(0);").execute()
    }
  }

  private def initDataEmailProcessingMessage() {
    DB.withConnection {
      implicit c =>
        SQL("insert into email_processing_message(id_of_last_processed_message) values(0);").execute()
    }
  }
}
