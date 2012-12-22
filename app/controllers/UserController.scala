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


/**
 * User controller
 */
object UserController extends Controller {

  def all = Action {
    Ok(views.html.user(User.all()))
  }



  def register = Action {
    implicit request =>

      //パラメーター受け取り
      val form = Form(
          "screenName" -> nonEmptyText
      )
      val screenName = form.bindFromRequest.fold(
        errors => throw new IllegalArgumentException("投稿メッセージが受け取れませんでした"),
        anyData => {
          anyData
        }
      )

      val uid = UUID.randomUUID().toString

      User.create(uid , screenName )
      Ok("UserController register : screenName = " + screenName)

  }
}
