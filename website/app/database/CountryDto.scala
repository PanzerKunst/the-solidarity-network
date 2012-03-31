package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Country


object CountryDto {
  val all = getAll

  def getAll: List[Country] = {

    DB.withConnection {
      implicit c =>

        val query = SQL("""
          select id, name
          from country
          order by name;
          """)

        query().map(row =>
          new Country(
            row[Long]("id"),
            row[String]("name")
          )
        ) toList
    }
  }

  def getOfId(id: Long) = {
    all.filter(c => c.id == id)
  }
}
