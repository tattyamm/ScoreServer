package controllers

import play.api._
import libs.json.{JsString, JsObject}
import play.api.data._
import play.api.data.Forms._

import play.api.mvc._
import models.{User, Score}
import models.Score.ScoreModelFormat

/**
 * Score Controller
 */
object ScoreController extends Controller {

  /**
   * スコア全件を返す
   * @return
   */
  def all = Action {
    Ok(views.html.score(Score.all()))
  }

  /**
   * スコアを登録する
   * @return
   */
  def register = Action {
    implicit request =>

    //パラメーター受け取り
      val form = Form(
        tuple(
          "uid" -> nonEmptyText,
          "score" -> nonEmptyText,
          "gameId" -> nonEmptyText
        )
      )
      val (uid, score, gameId) = form.bindFromRequest.fold(
        errors => throw new IllegalArgumentException("パラメータが正しく無いようです"),
        anyData => {
          anyData
        }
      )

      //TODO Scoreが前より低くないかとか、連続アクセスとか、そういうのを調べる

      //Score登録
      if (User.countByUID(uid) == 1) {
        Score.register(User.selectUserByUID(uid), score.toInt , gameId.toString)
      } else {
        throw new IllegalAccessError("存在しないユーザーのようです")
      }

      //順位確認
      val rank = Score.rankByUID(uid,gameId)

      //現在のランキングをjsonで返す
      Ok(JsObject(List("rank"->JsString(rank.toString))))

  }

}
