#ScoreServer

##概要
* ランキングとかスコアを記録するサーバーにしたい

##使い方
### ユーザー登録
* 名前を送るとuidが返る
 * POST

    http://localhost:9000/user/register -X POST -d "screenName=なまえ"

 * 戻り値

    {"id":"2","uid":"8ee37d91-dd5d-4fb7-9e25-91b2e2056190","screenName":"なまえ","createdAt":"2012-12-23 19:45:55.516"}

### スコア登録
* ユーザーごとに最大値が記録される
* あらかじめユーザー登録している必要がある
* 順位が返る
 * POST

    curl http://localhost:9000/score/register -X POST -d "score=1234" -d "uid=8ee37d91-dd5d-4fb7-9e25-91b2e2056190"

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
