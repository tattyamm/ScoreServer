package controllers


import play.api.data._
import play.api.data.Forms._

import play.api.mvc._
import models._
import models.User.UserModelFormat


/**
 * User controller
 */
object UserController extends Controller {

  /**
   * ユーザー全件を返す
   * @return
   */
  def all = Action {
    Ok(views.html.user(User.all()))
  }

  /**
   * ユーザー登録
   * @return
   */
  def register = Action {
    implicit request =>

    //パラメーター受け取り
      val form = Form(
        "screenName" -> nonEmptyText
      )
      val screenName = form.bindFromRequest.fold(
        errors => throw new IllegalArgumentException("パラメータが正しく無いようです"),
        anyData => {
          anyData
        }
      )

      //User新規登録
      val newUser = User.create(screenName)

      Ok(UserModelFormat.writes(newUser))

  }
}
