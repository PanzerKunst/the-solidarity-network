package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Country
import play.api.Logger


object CountryDto {
  def get(filters: Option[Map[String, String]]): List[Country] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, name
          from country """ + DbUtil.generateWhereClause(filters) + """
          order by name desc;"""

        Logger.info("CountryDto.get():" + query)

        SQL(query)().map(row =>
          Country(
            id = row[Long]("id"),
            name = row[String]("name")
          )
        ).toList
    }
  }
}
