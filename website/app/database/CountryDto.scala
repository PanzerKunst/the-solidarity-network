package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Country
import scala.collection.JavaConversions._


object CountryDto {
  val all = getAll

  def getAll: java.util.List[Country] = {

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
        ).toList
    }
  }

  def getOfId(id: Long) = {
    all.filter(c => c.id == id)
  }
}
