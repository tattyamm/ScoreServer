package controllers

import play.api._
import play.api.mvc._

/**
 * User controller
 */
object UserController extends Controller{

  def register = Action {
    Ok(views.html.index("UserController register"))
  }

}
