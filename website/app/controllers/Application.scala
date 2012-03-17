package controllers

import play.api.mvc._
import database.{CountryDto, UserDto}
import common.JsonUtil

object Application extends Controller {

  def index = Action {
    val user = UserDto.getUserOfUsername("blackbird")

    Ok(views.html.index(user))
  }

  def register = Action {

    // TODO: remove
    val countries = CountryDto.getAll
    val json = JsonUtil.serialize(countries)

    Ok(views.html.register(CountryDto.getAll))
  }

}