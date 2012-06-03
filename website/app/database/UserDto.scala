package database

import anorm._
import models.User
import play.api.db.DB
import play.api.Play.current


object UserDto {
  def getAUserWhere(filters: Option[Map[String, String]]): Option[User] = {
    DB.withConnection {
      implicit c =>

        val stream = SQL("""
          select id, first_name, last_name, username, email, password, street_address, post_code, city, country_id
          from user
          """ + DbUtil.generateWhereClause(filters) + ";"
        )
          .apply()

        if (stream.isEmpty)
          None
        else {
          val firstRow = stream.head

          Some(new User(
            firstRow[Long]("id"),
            firstRow[String]("username"),
            firstRow[String]("first_name"),
            firstRow[String]("last_name"),
            firstRow[String]("email"),
            firstRow[String]("password"),
            firstRow[String]("street_address"),
            firstRow[String]("post_code"),
            firstRow[String]("city"),
            firstRow[Long]("country_id")
          ))
        }
    }
  }

  def create(user: User) {
    DB.withConnection {
      implicit c =>

        SQL("""insert into user(first_name, last_name, username, email, password, street_address, post_code, city, country_id)
      values("""" + DbUtil.backslashQuotes(user.firstName) + """","""" +
          DbUtil.backslashQuotes(user.lastName) + """","""" +
          DbUtil.backslashQuotes(user.username) + """","""" +
          DbUtil.backslashQuotes(user.email) + """","""" +
          DbUtil.backslashQuotes(user.password) + """","""" +
          DbUtil.backslashQuotes(user.streetAddress) + """","""" +
          DbUtil.backslashQuotes(user.postCode) + """","""" +
          DbUtil.backslashQuotes(user.city) + """",""" +
          user.countryId + ");"
        )
          .execute()
    }
  }
}
