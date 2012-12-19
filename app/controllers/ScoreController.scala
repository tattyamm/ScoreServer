package controllers

import play.api._
import play.api.mvc._

/**
 * Score Controller
 */
object ScoreController extends Controller{

  def register = Action {
    Ok(views.html.index("ScoreController register"))
  }

}
