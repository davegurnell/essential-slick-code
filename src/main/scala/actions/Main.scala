package actions

import scala.concurrent._
import scala.concurrent.duration._
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  val program: DBIO[List[String]] =
    music.recreateTable
      .andThen(music.insertAlbums)
      .andThen(music.insertPinkFloyd)
      .andThen(music.updateKeyboardCat1)
      .andThen(music.deleteJustinBieber)
      .andThen(music.updateKeyboardCat2)
      .andThen(music.selectAlbums)

  def main(args: Array[String]): Unit = {
    val albums: List[String] =
      Await.result(database.run(program), 5.seconds)

    println(albums)
  }
}
