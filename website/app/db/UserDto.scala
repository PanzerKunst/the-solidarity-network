package db

import anorm._
import models.User
import play.api.db.DB
import play.api.Play.current
import play.api.Logger
import java.util


object UserDto {
  def get(filters: Option[Map[String, String]]): List[User] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, first_name, last_name, username, password, email, street_address, post_code, city, country_id, description, skills_and_interests, is_subscribed_to_news, subscription_to_new_help_requests
          from user """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("UserDto.get():" + query)

        SQL(query)().map(row =>
          User(
            id = Some(row[Long]("id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = Some(row[String]("username")),
            password = Some(row[String]("password")),
            email = Some(row[String]("email")),
            streetAddress = row[Option[String]]("street_address"),
            postCode = row[Option[String]]("post_code"),
            city = Some(row[String]("city")),
            countryId = Some(row[Long]("country_id")),
            description = row[Option[String]]("description"),
            skillsAndInterests = row[Option[String]]("skills_and_interests"),
            isSubscribedToNews = row[Boolean]("is_subscribed_to_news"),
            subscriptionToNewHelpRequests = row[String]("subscription_to_new_help_requests")
          )
        ).toList
    }
  }

  def getExceptOfId(filters: Option[Map[String, String]], id: String): List[User] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, first_name, last_name, username, password, email, street_address, post_code, city, country_id, description, skills_and_interests, is_subscribed_to_news, subscription_to_new_help_requests
          from user """ + DbUtil.generateWhereClause(filters) +
          """ and id <> """ + DbUtil.backslashQuotes(id) + ";"

        Logger.info("UserDto.getExceptOfId():" + query)

        SQL(query)().map(row =>
          User(
            id = Some(row[Long]("id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = Some(row[String]("username")),
            password = Some(row[String]("password")),
            email = Some(row[String]("email")),
            streetAddress = row[Option[String]]("street_address"),
            postCode = row[Option[String]]("post_code"),
            city = Some(row[String]("city")),
            countryId = Some(row[Long]("country_id")),
            description = row[Option[String]]("description"),
            skillsAndInterests = row[Option[String]]("skills_and_interests"),
            isSubscribedToNews = row[Boolean]("is_subscribed_to_news"),
            subscriptionToNewHelpRequests = row[String]("subscription_to_new_help_requests")
          )
        ).toList
    }
  }

  def create(user: User): Option[Long] = {
    DB.withConnection {
      implicit c =>

        var streetAddressForQuery = "NULL"
        if (user.streetAddress.isDefined && user.streetAddress.get != "")
          streetAddressForQuery = "\"" + DbUtil.backslashQuotes(user.streetAddress.get) + "\""

        var postCodeForQuery = "NULL"
        if (user.postCode.isDefined && user.postCode.get != "")
          postCodeForQuery = "\"" + DbUtil.backslashQuotes(user.postCode.get) + "\""

        val query = """
                       insert into user(first_name, last_name, username, email, password, city, country_id, creation_date, street_address, post_code)
        values("""" + DbUtil.backslashQuotes(user.firstName.get) + """", """" +
          DbUtil.backslashQuotes(user.lastName.get) + """", """" +
          DbUtil.backslashQuotes(user.username.get) + """", """" +
          DbUtil.backslashQuotes(user.email.get) + """", """" +
          DbUtil.backslashQuotes(user.password.get) + """", """" +
          DbUtil.backslashQuotes(user.city.get) + """", """ +
          user.countryId.get + """, """" +
          DbUtil.datetimeToString(new util.Date()) + """",""" +
          streetAddressForQuery + """,""" +
          postCodeForQuery + """);"""

        Logger.info("UserDto.create():" + query)

        SQL(query).executeInsert()
    }
  }

  def update(user: User) {
    DB.withConnection {
      implicit c =>

        var streetAddressForQuery = "NULL"
        if (user.streetAddress.isDefined && user.streetAddress.get != "")
          streetAddressForQuery = "\"" + DbUtil.backslashQuotes(user.streetAddress.get) + "\""

        var postCodeForQuery = "NULL"
        if (user.postCode.isDefined && user.postCode.get != "")
          postCodeForQuery = "\"" + DbUtil.backslashQuotes(user.postCode.get) + "\""

        var descriptionForQuery = "NULL"
        if (user.description.isDefined && user.description.get != "")
          descriptionForQuery = "\"" + DbUtil.backslashQuotes(user.description.get.replaceAll("\n", "\\\\n")) + "\""

        var skillsAndInterestsForQuery = "NULL"
        if (user.skillsAndInterests.isDefined && user.skillsAndInterests.get != "")
          skillsAndInterestsForQuery = "\"" + DbUtil.backslashQuotes(user.skillsAndInterests.get.replaceAll("\n", "\\\\n")) + "\""

        val query = """
                       update user set
          first_name = """" + DbUtil.backslashQuotes(user.firstName.get) + """",
          last_name = """" + DbUtil.backslashQuotes(user.lastName.get) + """",
          email = """" + DbUtil.backslashQuotes(user.email.get) + """",
          password = """" + DbUtil.backslashQuotes(user.password.get) + """",
          city = """" + DbUtil.backslashQuotes(user.city.get) + """",
          country_id = """ + user.countryId.get + """,
          street_address = """ + streetAddressForQuery + """,
          post_code = """ + postCodeForQuery + """,
          description = """ + descriptionForQuery + """,
          skills_and_interests = """ + skillsAndInterestsForQuery + """,
          is_subscribed_to_news = """ + user.isSubscribedToNews + """,
          subscription_to_new_help_requests = """" + user.subscriptionToNewHelpRequests + """"
          where id = """ + user.id.get + """;"""

        Logger.info("UserDto.update():" + query)

        SQL(query).executeUpdate()
    }
  }

  def searchExceptOfId(searchQuery: String, id: Long): List[User] = {
    DB.withConnection {
      implicit c =>

        val query = """
            select id, first_name, last_name, username, password, email, street_address, post_code, city, country_id, description, skills_and_interests, is_subscribed_to_news, subscription_to_new_help_requests
            from user
            where (first_name like "%""" + searchQuery + """%"
            or last_name like "%""" + searchQuery + """%"
            or username like "%""" + searchQuery + """%")
            and id <> """ + id + """
            limit 50;"""

        Logger.info("UserDto.searchExceptOfId():" + query)

        SQL(query)().map(row =>
          User(
            id = Some(row[Long]("id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = Some(row[String]("username")),
            password = Some(row[String]("password")),
            email = Some(row[String]("email")),
            streetAddress = row[Option[String]]("street_address"),
            postCode = row[Option[String]]("post_code"),
            city = Some(row[String]("city")),
            countryId = Some(row[Long]("country_id")),
            description = row[Option[String]]("description"),
            skillsAndInterests = row[Option[String]]("skills_and_interests"),
            isSubscribedToNews = row[Boolean]("is_subscribed_to_news"),
            subscriptionToNewHelpRequests = row[String]("subscription_to_new_help_requests")
          )
        ).toList
    }
  }
}
