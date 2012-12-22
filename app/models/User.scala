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
                 uid: String,
                 screenName: String,
                 createdAt: String
                 )

object User {

  def all(): List[User] = DB.withConnection { implicit c =>
    SQL("select * from user").as(user *)
  }

//  def create(uid:String , screenName:String , createdAt:String){
def create(uid:String , screenName:String , createdAt:String){
    DB.withConnection { implicit c =>
      SQL("insert into user (uid , screenName , createdAt) values ({uid} , {screenName} , {createdAt} )").on(
        'uid -> uid,
        'screenName -> screenName,
        'createdAt -> createdAt
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
