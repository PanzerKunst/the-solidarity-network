package database

import anorm._
import models.User
import play.api.db.DB
import play.api.Play.current


object UserDto {
  def getUserOfUsername(username: String): User = {

    DB.withConnection {
      implicit c =>

        val firstRow = SQL("""
        select id, username from user
        where username = {username};
      """)
          .on("username" -> username)
          .apply()
          .head

        new User(
          firstRow[Long]("id"),
          firstRow[String]("username")
        )
    }
  }
}
