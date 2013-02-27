package database

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
          select id, first_name, last_name, username, password, email, street_address, post_code, city, country_id, description
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
            description = row[Option[String]]("description")
          )
        ).toList
    }
  }

  def getExceptOfId(filters: Option[Map[String, String]], id: String): List[User] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, first_name, last_name, username, password, email, street_address, post_code, city, country_id, description
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
            description = row[Option[String]]("description")
          )
        ).toList
    }
  }

  def create(user: User): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val streetAddressForQuery = user.streetAddress.getOrElse("")
        val postCodeForQuery = user.postCode.getOrElse("")

        val query = """
                       insert into user(first_name, last_name, username, email, password, street_address, post_code, city, country_id, creation_date)
      values("""" + DbUtil.backslashQuotes(user.firstName.get) + """", """" +
          DbUtil.backslashQuotes(user.lastName.get) + """", """" +
          DbUtil.backslashQuotes(user.username.get) + """", """" +
          DbUtil.backslashQuotes(user.email.get) + """", """" +
          DbUtil.backslashQuotes(user.password.get) + """", """" +
          DbUtil.backslashQuotes(streetAddressForQuery) + """", """" +
          DbUtil.backslashQuotes(postCodeForQuery) + """", """" +
          DbUtil.backslashQuotes(user.city.get) + """", """ +
          user.countryId.get + """, """" +
          DbUtil.datetimeToString(new util.Date()) + """");"""

        Logger.info("UserDto.create():" + query)

        SQL(query).executeInsert()
    }
  }

  def update(user: User) {
    DB.withConnection {
      implicit c =>

        val streetAddressForQuery = user.streetAddress.getOrElse("")
        val postCodeForQuery = user.postCode.getOrElse("")
        val descriptionForQuery = user.description.getOrElse("")

        val query = """
                       update user set
          first_name = """" + DbUtil.backslashQuotes(user.firstName.get) + """",
          last_name = """" + DbUtil.backslashQuotes(user.lastName.get) + """",
          email = """" + DbUtil.backslashQuotes(user.email.get) + """",
          password = """" + DbUtil.backslashQuotes(user.password.get) + """",
          street_address = """" + DbUtil.backslashQuotes(streetAddressForQuery) + """",
          post_code = """" + DbUtil.backslashQuotes(postCodeForQuery) + """",
          city = """" + DbUtil.backslashQuotes(user.city.get) + """",
          country_id = """ + user.countryId.get + """,
          description = """" + DbUtil.backslashQuotes(descriptionForQuery) + """"
          where id = """ + user.id.get + """;"""

        Logger.info("UserDto.update():" + query)

        SQL(query).execute()
    }
  }

  def search(searchQuery: String): List[User] = {
    DB.withConnection {
      implicit c =>

        val query = """
            select id, first_name, last_name, username, password, email, street_address, post_code, city, country_id, description
            from user
            where first_name like "%""" + searchQuery + """%"
            or last_name like "%""" + searchQuery + """%"
            or username like "%""" + searchQuery + """%"
            limit 50;"""

        Logger.info("UserDto.search():" + query)

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
            description = row[Option[String]]("description")
          )
        ).toList
    }
  }
}
