package models

import play.api._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

/**
 * User Model
 *
 * DB操作方法 https://github.com/playframework-ja/Play20/wiki/ScalaAnorm
 *
 * iOSの場合、[[UIDevice currentDevice] identifierForVendor]をとりあえず想定
 * （もちろん適当な値が投げられたら、適当な値が入る　cf http://akisute.com/2011/08/udiduiid.html ）
 * サーバーサイドでid発行の方が普通かな。（register叩くとユニークなuid(java.util.UUIDとか)を返し、以降はそれを使う。）
 */
case class User(
                 id: Long,
                 uid: String,
                 screenName: String,
                 createdAt: String
                 )

object User {

  def all(): List[User] = DB.withConnection { implicit c =>
    SQL("select * from user").as(user *)
  }

def create(uid:String , screenName:String ){
    DB.withConnection { implicit c =>
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
      case id~uid~screenName~createdAt => User(id, uid , screenName , createdAt)
    }
  }

}
