package controllers

import play.api._
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
   * TODO 100件程度に制限する（sqlでlimit 100する）
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
          "score" -> nonEmptyText
        )
      )
      val (uid, score) = form.bindFromRequest.fold(
        errors => throw new IllegalArgumentException("パラメータが正しく無いようです"),
        anyData => {
          anyData
        }
      )

      //TODO Scoreが前より低くないかとか、連続アクセスとか、そういうのを調べる

      //Score登録
      if (User.countByUID(uid) == 1) {
        Score.register(User.selectUserByUID(uid), score.toInt)
      } else {
        throw new IllegalAccessError("存在しないユーザーのようです")
      }

      //順位確認
      val rank = Score.rankByUID(uid)

      //TODO 現在のランキングをjsonで返す
      Ok("スコア更新できたはず 順位："+rank)

  }

}
