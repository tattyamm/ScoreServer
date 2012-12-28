package controllers


import play.api.data._
import play.api.data.Forms._

import play.api.mvc._
import models._
import models.User.UserModelFormat

import play.api.libs.json._


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

  /** ユーザー全件をjsonで返す
    * @return
    */
  def alljson = Action {

    //TODO 日本語が文字化け。Unicodeエスケープが必要。半角しか扱わないという手もある。
    Ok(
      "[" +
        User.all().map {
        u =>
          UserModelFormat.writes(u).toString
        }.mkString(",")
      + "]"
    ).as("application/json")

    /*
    //Modelをなるべくそのまま扱うとこうなる。
    // Seq(String,JsValue)しか受け付けてくれないが、list[jsobject]をそのままjsonのlistにして欲しい。
    // イメージ
    // { "array" : [
    // { "name1" : "value1" },  //user1
    // { "name2" : "value2" }   //user2
    // ] }
    Ok(
      JsObject(
        User.all().map {
          u =>
            UserModelFormat.writes(u)
        }.map {
          user =>
            ((user \ "id").as[String], user)
        }
      )
    )
    */
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
