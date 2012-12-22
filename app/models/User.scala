package models

import play.api._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import annotation.tailrec
import java.util.UUID

/**
 * User Model
 *
 * DB操作方法 https://github.com/playframework-ja/Play20/wiki/ScalaAnorm
 *
 * iOSの場合、[[UIDevice c u r r e n t D e v i c e ] i d e n t i f i e r F o r V e n d o r ] を と り あ え ず 想 定
 * （もちろん適当な値が投げられたら、適当な値が入る　cf http://akisute.com/2011/08/udiduiid.html ）
 * サーバーサイドでid発行の方が普通かな。（register叩くとユニークなuid(java.util.uidとか)を返し、以降はそれを使う。）
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

  def create(uid: String, screenName: String) {
    DB.withConnection {
      implicit c =>
        SQL("insert into user (uid , screenName ) values ({uid} , {screenName}  )").on(
          'uid -> uid,
          'screenName -> screenName
        ).executeUpdate()
    }

  }

  val user = {
    get[Long]("id") ~
      get[String]("uid") ~
      get[String]("screenName") ~
      get[String]("createdAt") map {
      case id ~ uid ~ screenName ~ createdAt => User(id, uid, screenName, createdAt)
    }
  }

  def countByUID(uid:String): Int = DB.withConnection {
    implicit c =>
      val firstRow = SQL("select count(*) as c from user where uid = {uid}").on(
        'uid -> uid
      ).apply().head
      firstRow[Long]("c").toInt
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
}
