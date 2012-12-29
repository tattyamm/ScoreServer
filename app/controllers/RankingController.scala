package controllers

import play.api._
import play.api.mvc._
import models.Score

/**
 * Ranking controller
 */
object RankingController extends Controller{

  def getRankingWeekley = Action {
    Ok("RankingController getRankingWeekley")
  }

  def getRankingToday = Action {
    Ok("RankingController getRankingToday")
  }

  def getRankingList = Action {
    Ok(views.html.gameList(Score.games()))
  }

  def getRankingTotal(gameId:String) = Action {
    Ok(views.html.score(Score.ranking(gameId)))
  }
}
