package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import annotation.tailrec
import java.util.UUID

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString


/**
 * User Model
 *
 * DB操作方法 https://github.com/playframework-ja/Play20/wiki/ScalaAnorm
 */
case class User(
                 id: Long,
                 uid: String,
                 screenName: String,
                 createdAt: String
                 )

object User {

  def all(): List[User] = DB.withConnection {
    implicit c =>
      SQL("select * from user").as(user *)
  }

  /**
   * Userの新規登録をし、その結果のModelを返す
   * TODO 失敗時は考慮していない
   * @param screenName
   * @return
   */
  def create(screenName: String): User = {
    val uid = User.createUid()
    DB.withConnection {
      implicit c =>
        SQL("insert into user (uid , screenName ) values ({uid} , {screenName}  )").on(
          'uid -> uid,
          'screenName -> screenName
        ).executeUpdate()
    }

    //今回登録したユーザーを返す
    selectUserByUID(uid)
  }

  /**
   * uidからUserを返す。
   * TODO 存在しないユーザーだった場合、エラーだった場合を考慮していない
   * @param uid
   * @return
   */
  def selectUserByUID(uid: String): User = DB.withConnection {
    implicit c =>
      SQL("select * from user where uid = {uid}").on(
        'uid -> uid
      ).as(user *).head //uidはuniqなので1人しか返ってこないはず
  }

  val user = {
    get[Long]("id") ~
      get[String]("uid") ~
      get[String]("screenName") ~
      get[String]("createdAt") map {
      case id ~ uid ~ screenName ~ createdAt => User(id, uid, screenName, createdAt)
    }
  }

  def countByUID(uid: String): Int = DB.withConnection {
    implicit c =>
      SQL("select count(*) from user where uid = {uid}").on(
        'uid -> uid
      ).as(scalar[Long].single).toInt
  }

  /**
   * UID発行（重複チェック付き）
   * @return
   */
  @tailrec //これ末尾再帰とかじゃなく、普通の無限ループでは？
  def createUid(): String = {
    val uid = UUID.randomUUID().toString
    if (User.countByUID(uid) > 0) {
      createUid
    } else {
      uid
    }
  }


  /**
   * jsonとmodelの変換
   * https://github.com/playframework-ja/Play20/wiki/ScalaJson
   */
  implicit object UserModelFormat extends Format[User] {
    /*
     * JsonからModelに変換
     */
    def reads(json: JsValue): User = User(
      (json \ "id").as[Long],
      (json \ "uid").as[String],
      (json \ "screenName").as[String],
      (json \ "createdAt").as[String]
    )

    /*
     * ModelからJsonに変換
     */
    def writes(u: User): JsValue = JsObject(
      List(
        "id" -> JsString(u.id.toString),
        "uid" -> JsString(u.uid),
        "screenName" -> JsString(u.screenName),
        "createdAt" -> JsString(u.createdAt)
      )
    )

  }

}
