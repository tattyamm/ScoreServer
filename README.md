#ScoreServer

##概要
* ランキングとかスコアを記録するサーバーにしたい

##使い方
### ユーザー登録
* 名前を送るとuidが返る
* POST

        curl http://localhost:9000/user/register -X POST -d "screenName=YourName"

* 戻り値

        {"id":"2","uid":"7a17d693-a1e8-48f0-b9e4-01fe8a02f986","screenName":"YoutName","createdAt":"2012-12-29 17:21:51.771"}

### スコア登録
* ゲームIDへの対応は不完全で、常に同じIDを使わないと、バグる可能性がある（！）
* ユーザーごとに最大値が記録される
* あらかじめユーザー登録している必要がある
* 順位が返る


        curl http://localhost:9000/score/register -X POST -d "score=1234" -d "uid=7a17d693-a1e8-48f0-b9e4-01fe8a02f986" -d "gameId=game01"
* 戻り値

        {"rank":"1"}


### URL
* ランキング
 * http://localhost:9000/ranking/total
* ユーザー全員
 * http://localhost:9000/user/all
* スコア全部
 * http://localhost:9000/score/all

##今後
* リファクタリング
* エラー時にplayの画面ではなくjsonを返す
* 情報の削除、修正をするAPIの作成
* uid再発行機構
* http://scoreserver.herokuapp.com/ で動かす

##作者
* [@tattyamm](https://twitter.com/tattyamm)
