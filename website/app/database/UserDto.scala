package database

import anorm._
import models.User
import play.api.db.DB
import play.api.Play.current
import play.api.Logger


object UserDto {
  def get(filters: Option[Map[String, String]]): List[User] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, first_name, last_name, username, email, password, street_address, post_code, city, country_id
          from user """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("UserDto.getUsersWhere():" + query)

        SQL(query)().map(row =>
          new User(
            row[Long]("id"),
            row[String]("username"),
            row[String]("first_name"),
            row[String]("last_name"),
            row[String]("email"),
            row[String]("password"),
            row[String]("street_address"),
            row[String]("post_code"),
            row[String]("city"),
            row[Long]("country_id")
          )
        ).toList
    }
  }

  def create(user: User) {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into user(first_name, last_name, username, email, password, street_address, post_code, city, country_id)
      values("""" + DbUtil.backslashQuotes(user.firstName) + """","""" +
          DbUtil.backslashQuotes(user.lastName) + """","""" +
          DbUtil.backslashQuotes(user.username) + """","""" +
          DbUtil.backslashQuotes(user.email) + """","""" +
          DbUtil.backslashQuotes(user.password) + """","""" +
          DbUtil.backslashQuotes(user.streetAddress) + """","""" +
          DbUtil.backslashQuotes(user.postCode) + """","""" +
          DbUtil.backslashQuotes(user.city) + """",""" +
          user.countryId + ");"

        Logger.info("UserDto.create():" + query)

        SQL(query).execute()
    }
  }
}
