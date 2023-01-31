package aggregates

import slick.jdbc.MySQLProfile.api._
import slick.jdbc.MySQLProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  val program: DBIO[List[(Long, Int)]] =
    music.recreateTables
      .andThen(music.insertAll)
      .andThen(music.numberOfAlbumsByArtist)

  def main(args: Array[String]): Unit = {
    val results: List[(Long, Int)] =
      Await.result(database.run(program), 5.seconds)

    results.foreach(println)
  }
}
