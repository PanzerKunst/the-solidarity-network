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
          select id, first_name, last_name, username, email, street_address, post_code, city, country_id
          from user """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("UserDto.get():" + query)

        SQL(query)().map(row =>
          User(
            id = Some(row[Long]("id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = row[String]("username"),
            email = Some(row[String]("email")),

            streetAddress = if (row[String]("street_address") != "")
              Some(row[String]("street_address"))
            else
              None,

            postCode = if (row[String]("post_code") != "")
              Some(row[String]("post_code"))
            else
              None,

            city = Some(row[String]("city")),
            countryId = Some(row[Long]("country_id"))
          )
        ).toList
    }
  }

  def create(user: User) {
    DB.withConnection {
      implicit c =>

        val streetAddressForQuery = user.streetAddress match {
          case Some(streetAddress) => streetAddress
          case None => ""
        }

        val postCodeForQuery = user.postCode match {
          case Some(postCode) => postCode
          case None => ""
        }

        val query = """
                       insert into user(first_name, last_name, username, email, password, street_address, post_code, city, country_id, creation_date)
      values("""" + DbUtil.backslashQuotes(user.firstName.get) + """","""" +
          DbUtil.backslashQuotes(user.lastName.get) + """","""" +
          DbUtil.backslashQuotes(user.username) + """","""" +
          DbUtil.backslashQuotes(user.email.get) + """","""" +
          DbUtil.backslashQuotes(user.password.get) + """","""" +
          DbUtil.backslashQuotes(streetAddressForQuery) + """","""" +
          DbUtil.backslashQuotes(postCodeForQuery) + """","""" +
          DbUtil.backslashQuotes(user.city.get) + """",""" +
          user.countryId.get + ""","""" +
          DbUtil.datetimeToString(new util.Date()) + """");"""

        Logger.info("UserDto.create():" + query)

        SQL(query).execute()
    }
  }
}
