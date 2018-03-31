# BlockBreaker
部活で制作したPC用Javaゲームです。  
これをビルドするには別途[YDKのゲームエンジン](https://github.com/ydk-nk/SimpleGameFramework) (エンジンと言えるかわからない)と
[JavaZoom JLayer](http://www.javazoom.net/javalayer/javalayer.html)の  
2つのライブラリが必要です。  
  
**遊ぶだけなら[GoogleDrive - BlockBreaker](https://drive.google.com/drive/folders/1AJAGSFdX3n3XmBeBeH5ZCQFZIw4WAhf1?usp=sharing)
の方をダウンロードしてください。**

![Screenshot](https://user-images.githubusercontent.com/33191176/38160887-69fb2e30-34ff-11e8-8a68-d6b9a2239be4.png)

* バージョン -- 1.0  
* 作者名 -- YDK_NK  
* ライセンス -- LGPL-2.1  
* 動作環境 -- Java SE7以降JRE  
* 最終更新日 -- 2018/3/31  

## 概要
  Javaで動作する,ボールが増えるブロック崩しゲーム。  
  上から侵攻してくるブロックにボールを当ててブロックを壊します。  
  ブロックが地面についたらGameOverとなります。  
  このゲームにクリアはなく,どれだけ生き延びられるかがポイントとなります。  

## 実行方法
  同梱されている"blockBreaker.jar"がゲーム本体となります。  
  ファイルマネージャでそれをダブルクリックするか, ターミナルで  
          java -jar blockBreaker.jar  
  と入力すると実行できます。  
  実行にはjava SE7以降のJREが必要です。  


## ゲームの流れ
  ゲームを始めるとタイトル画面が出てきます。クリックするとゲーム開始です。  
  ゲームは「ターン」という一つのまとまりで動いていきます。  
  ボールを飛ばし,ボールが地面に戻り,ブロックが1つ分下がって1ターンが終了します。  
  そのターンの流れを繰り返します。  
  ブロックが地面につくとGameOverです。クリックしてタイトル画面に戻ります。  

## 各オブジェクトの説明
#### ボール
  ボールは最初地面についています。  
  マウスでクリックしたところにボールが飛んでいきます。  
  スペースキーを押すとボールを早送りすることができます。  
  ボールはブロックに当たると反射します。  

#### ブロック
  ボールに当たるとブロックのHPが1つ減ります。  
  ブロックに書いてある数字がHPです。  
  1ターンごとに1つ分下へ侵攻してきます。  
  
#### スター
  星のマーク(スター) を取るとボールが一つ増えます。  
  増えた分は次のターンに反映されます。  
  なお,スターは地面にあたってもGameOverとならないので安心してください。  

#### ステータスパネル
  画面右側の黒い部分です。ゲームの進捗状況が表示されます。  
  表示される情報は以下のとおりです。  
  1. WAVE:  現在のターン数です。  
  1. BALL:  現在のボールの数です。  
  1. SCORE: 現在のスコアです。得点は以下のとおりです。  
      * ブロックの破壊: +100  
      * ボールが増える: +200  
      * 1ターン経過:    +50  
      * 5ターンごとに:  +100  
      * 10ターンごとに: +300  
    得点の配分は適当です。簡単に得点が万単位になります。  

## 注意事項
  本ソフトウェアを使用したことあるいは使用できなかったことによって  
  生じた損害について,作者は責任を負いません。  
	
  本ソフトウェアの仕様、名称、公開形態及び本テキストファイルの内容は  
  予告無く変更されることがあります。  

## お礼
  MP3ファイルの再生にJavaZoom様のJLayerライブラリを使用させていただきました。  
  音楽素材は「魔王魂」様,「Music-Note.jp」からお借りしています。  
  画像素材の一部は「写真AC」様, 「GATAG」様からお借りしています。  
  この場を借りて感謝申し上げます。
   * JavaZoom JLayer ----  http://www.javazoom.net/javalayer/javalayer.html
   * 魔王魂          ----  https://maoudamashii.jokersounds.com/ 
   * Music-Note.jp   ----  http://www.music-note.jp/  
     (Music-Note.jpの運営:株式会社ピクセル - http://pixel-co.com/)  
   * 写真AC          ----  https://www.photo-ac.com/  
   * GATAG フリーイラスト素材集 ---- http://free-illustrations.gatag.net/  

## 既知の不具合
  * スペースキーで早送りするとボールの軌道がずれることがある。  
  * ボールがブロックの角に当たったと判定されて反射が不自然なことがある。  
  * ボールがブロックを貫通することがある。  
  * まれにGameOver画面でクリックしてもタイトル画面に戻らないことがある。  
