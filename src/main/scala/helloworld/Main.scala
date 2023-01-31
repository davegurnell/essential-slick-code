package helloworld

import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent._
import scala.concurrent.duration._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  val program: DBIO[List[Album]] =
    music.dropTable
      .andThen(music.createTable)
      .andThen(music.insertTestData)
      .andThen(music.selectTestData)

  def main(args: Array[String]): Unit = {
    val albumsFuture: Future[List[Album]] =
      database.run(program)

    val albums: List[Album] =
      Await.result(albumsFuture, 2.seconds)

    albums.foreach(println)
  }
}
