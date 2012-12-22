package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import mvc._

import play.api._
import play.api.data._
import play.api.data.Forms._
import libs.oauth.ConsumerKey
import libs.oauth.OAuth
import libs.oauth.OAuthCalculator
import libs.oauth.RequestToken
import libs.oauth.ServiceInfo

import models.User
import java.util.UUID
import annotation.tailrec


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
      val uid = User.createUid()

      User.create(uid, screenName)

      //TODO 最終的にUserModelのJSONを返したい
      Ok("UserController register : screenName = " + screenName)

  }
}
