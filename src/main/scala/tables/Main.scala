package tables

import slick.jdbc.H2Profile.api._
import slick.jdbc.MySQLProfile

import scala.concurrent._
import scala.concurrent.duration._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  val program: DBIO[List[Album]] =
    music.recreateTable
      .andThen(music.insertAlbums)
      .andThen(music.selectAlbums)

  def main(args: Array[String]): Unit = {
    val albums: List[Album] =
      Await.result(database.run(program), 5.seconds)

    albums.foreach(println)
  }
}
