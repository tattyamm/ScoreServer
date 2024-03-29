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
 * userが1つだけスコアを持つので、今の仕様ならuserテーブルのみで良い気もする（最大値以外も記録するのなら、scoreテーブルは必須）
 */
case class Score(
                  id: Long,
                  uid: String,
                  screenName: String,
                  score: Int,
                  gameId: String ,
                  createdAt: String
                  )

object Score {

  val score = {
    get[Long]("id") ~
      get[String]("uid") ~
      get[String]("screenName") ~
      get[Int]("score") ~
      get[String]("gameId") ~
      get[String]("createdAt") map {
      case id ~ uid ~ screenName ~ score ~ gameId ~createdAt => Score(id, uid, screenName, score, gameId, createdAt)
    }
  }

  def all(): List[Score] = DB.withConnection {
    implicit c =>
      SQL("select * from score").as(score *)
  }

  def ranking(gameId:String , limit:Int = 50): List[Score] = DB.withConnection {
    implicit c =>
      SQL("select * from score where gameId = {gameId} order by score desc limit {limit}").on(
        'limit -> limit,
        'gameId -> gameId
      ).as(score *)
  }

  /**
   * ゲームIDユニークなList[Score]を返す
   * TODO ゲームIDが2つ以上あると動かない...
   * @return
   */
  def games(): List[Score] = DB.withConnection {
    implicit c =>
      SQL("select * from score where gameId =  (select distinct gameId from score)")
        .as(score *)
  }

  def rankByUID(uid:String , gameId:String): Int = DB.withConnection {
    implicit c =>
      SQL(
        """
        select
          (select count(id)
          from score t2
          where t2.score > t1.score) + 1 as ranking
        from score  t1
        where uid = {uid} and gameId = {gameId}
        """
      ).on(
        'uid -> uid,
        'gameId -> gameId
      ).as(scalar[Long].single).toInt
  }

  /**
   * Scoreの新規登録または更新
   * TODO 失敗時は考慮していない
   * @param user
   * @param score
   * @return
   */
  def register(user:User , score:Int , gameId:String) = {
    if (countByUID(user.uid) == 1){
      //更新
      update(user,score,gameId)
    }else{
      //新規
      create(user,score,gameId)
    }
  }


  def create(user: User, score: Int, gameId:String) = {
    DB.withConnection {
      implicit c =>
        SQL("insert into score (uid , score , gameId , screenName) values ({uid} , {score} , {gameId} , {screenName} )").on(
          'uid -> user.uid,
          'score -> score,
          'gameId -> gameId,
          'screenName -> user.screenName
        ).executeUpdate()
    }
  }

  def update(user: User, score: Int, gameId:String) = {
    DB.withConnection {
      implicit c =>
        SQL("update score set score = {score} , gameId = {gameId} where uid = {uid}").on(
          'uid -> user.uid,
          'gameId -> gameId,
          'score -> score
        ).executeUpdate()
    }
  }

  /**
   * uidからScoreを返す。
   * TODO 存在しないuidだった場合、エラーだった場合を考慮していない
   * @param uid
   * @return
   */
  def selectScoreByUID(uid: String): Score = DB.withConnection {
    implicit c =>
      SQL("select * from score where uid = {uid}").on(
        'uid -> uid
      ).as(score *).head //idはuniqなので1件のはず
  }

  def countByUID(uid: String): Int = DB.withConnection {
    implicit c =>
      SQL("select count(*) from score where uid = {uid}").on(
        'uid -> uid
      ).as(scalar[Long].single).toInt
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
      (json \ "gameId").as[String],
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
        "gameId" -> JsString(s.gameId.toString),
        "createdAt" -> JsString(s.createdAt)
      )
    )

  }

}
