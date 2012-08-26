package database

import anorm._
import play.api.db.DB
import models.{User, HelpRequest}
import play.api.Play.current
import play.api.Logger
import java.util

object HelpRequestDto {
  def get(filters: Option[Map[String, String]]): List[HelpRequest] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, requester_id, title, description, creation_date, expiry_date
          from help_request """ + DbUtil.generateWhereClause(filters) + """
          order by expiry_date, creation_date;"""

        Logger.info("HelpRequestDto.get():" + query)

        SQL(query)().map(row =>
          HelpRequest(
            id = Some(row[Long]("id")),
            requesterId = Some(row[Long]("requester_id")),
            title = row[String]("title"),
            description = row[String]("description"),
            creationDatetime = Some(row[util.Date]("creation_date")),
            expiryDate = row[util.Date]("expiry_date")
          )
        ).toList
    }
  }

  def searchGeneric(searchQuery: Option[String]): List[(HelpRequest, User)] = {
    DB.withConnection {
      implicit c =>

        val query = searchQuery match {
          case Some(sq) =>
            val processedSearchQuery = DbUtil.backslashQuotes(sq.replaceAll("\\*", "%"))

            """
            select hr.id as help_request_id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id as user_id, u.first_name, u.last_name, u.username, u.email, u.city,
              c.id as country_id, c.name as country_name
            from help_request hr
            inner join user u on u.id = hr.requester_id
            inner join country c on c.id = u.country_id
            where hr.title like """" + processedSearchQuery + """"
            or hr.description like """" + processedSearchQuery + """"
            or u.first_name like """" + processedSearchQuery + """"
            or u.last_name like """" + processedSearchQuery + """"
            or u.username like """" + processedSearchQuery + """"
            or u.city like """" + processedSearchQuery + """"
            or c.name like """" + processedSearchQuery + """"
            order by expiry_date, creation_date
            limit 50;"""

          case None => """
            select hr.id as help_request_id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id as user_id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id
            from help_request hr
            inner join user u on u.id = hr.requester_id
            order by expiry_date, creation_date
            limit 50;"""
        }

        Logger.info("HelpRequestDto.searchGeneric():" + query)

        SQL(query)().map(row =>
          (
            HelpRequest(
              id = Some(row[Long]("help_request.id")),
              requesterId = Some(row[Long]("user.id")),
              title = row[String]("title"),
              description = row[String]("description"),
              creationDatetime = Some(row[util.Date]("creation_date")),
              expiryDate = row[util.Date]("expiry_date")
            ),
            User(
              id = Some(row[Long]("user.id")),
              firstName = Some(row[String]("first_name")),
              lastName = Some(row[String]("last_name")),
              username = Some(row[String]("username")),
              email = Some(row[String]("email")),
              city = Some(row[String]("city")),
              countryId = Some(row[Long]("country_id"))
            )
            )
        ).toList
    }
  }

  def create(helpRequest: HelpRequest): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into help_request(requester_id, title, description, creation_date, expiry_date)
      values(""" + helpRequest.requesterId.get + """, """" +
          DbUtil.backslashQuotes(helpRequest.title) + """", """" +
          DbUtil.backslashQuotes(helpRequest.description) + """", """" +
          DbUtil.datetimeToString(new util.Date()) + """", """" +
          DbUtil.dateToString(helpRequest.expiryDate) + """");"""

        Logger.info("HelpRequestDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }
}
