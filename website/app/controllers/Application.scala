package controllers

import play.api.mvc._
import database.CountryDto

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def register = Action {
    Ok(views.html.register(CountryDto.getAll))
  }

}