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
 * Score Model
 */
case class Score(
                  id: Long,
                  uid: String,
                  screenName: String,
                  score: Int,
                  createdAt: String
                  )

object Score {

  val score = {
    get[Long]("id") ~
      get[String]("uid") ~
      get[String]("screenName") ~
      get[Int]("score") ~
      get[String]("createdAt") map {
      case id ~ uid ~ screenName ~ score ~ createdAt => Score(id, uid, screenName, score, createdAt)
    }
  }

  def all(): List[Score] = DB.withConnection {
    implicit c =>
      SQL("select * from score").as(score *)
  }

  /**
   * Scoreの新規登録をし、その結果のModelを返す
   * TODO 失敗時は考慮していない
   * @param user
   * @param score
   * @return
   */
  def create(user: User, score: Int): Score = {
    DB.withConnection {
      implicit c =>
        SQL("insert into score (uid , score , screenName) values ({uid} , {score} ,{screenName} )").on(
          'uid -> user.uid,
          'score -> score,
          'screenName -> user.screenName
        ).executeUpdate()
    }

    //今回登録したユーザーを返す
    //TODO どうやって？というかいらない？
    //selectUserByUID(uid)
    Score(1L, "debug uid!", "screenname!", 2, "createdad")
  }

  /**
   * jsonとmodelの変換
   * https://github.com/playframework-ja/Play20/wiki/ScalaJson
   */
  implicit object ScoreModelFormat extends Format[Score] {
    /*
     * JsonからModelに変換
     */
    def reads(json: JsValue): Score = Score(
      (json \ "id").as[Long],
      (json \ "uid").as[String],
      (json \ "screenName").as[String],
      (json \ "score").as[Int],
      (json \ "createdAt").as[String]
    )

    /*
     * ModelからJsonに変換
     */
    def writes(s: Score): JsValue = JsObject(
      List(
        "id" -> JsString(s.id.toString),
        "uid" -> JsString(s.uid),
        "screenName" -> JsString(s.screenName),
        "score" -> JsString(s.score.toString),
        "createdAt" -> JsString(s.createdAt)
      )
    )

  }

}
