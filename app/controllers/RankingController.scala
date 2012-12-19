package controllers

import play.api._
import play.api.mvc._

/**
 * Ranking controller
 */
object RankingController extends Controller{

  def getRankingWeekley = Action {
    Ok(views.html.index("RankingController getRankingWeekley"))
  }

  def getRankingToday = Action {
    Ok(views.html.index("RankingController getRankingToday"))
  }

  def getRankingTotal = Action {
    Ok(views.html.index("RankingController getRankingTotal"))
  }
}
