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
      val (uid , score) = form.bindFromRequest.fold(
        errors => throw new IllegalArgumentException("パラメータが正しく無いようです"),
        anyData => {
          anyData
        }
      )

      //Score新規登録
      //TODo uidの存在確認
      val newScore = if (User.countByUID(uid)==1){
        Score.create(User.selectUserByUID(uid), score.toInt)
      }else{
        throw new IllegalAccessError("存在しないユーザーのようです")
      }

      Ok(ScoreModelFormat.writes(newScore))

  }

}
