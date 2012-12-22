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
 */
case class User(
                 id: Long,
                 //uid: String,
                 screenName: String
                 //createdAt: String
                 )

object User {

  def all(): List[User] = DB.withConnection { implicit c =>
    SQL("select * from user").as(user *)
  }

//  def create(uid:String , screenName:String , createdAt:String){
def create(screenName:String){
    DB.withConnection { implicit c =>
      SQL("insert into user (screenName) values ({screenName})").on(
        'screenName -> screenName
      ).executeUpdate()
    }

  }

  val user = {
    get[Long]("id") ~
      get[String]("screenName") map {
      case id~screenName => User(id, screenName)
    }
  }

}
