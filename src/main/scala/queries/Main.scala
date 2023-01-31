package queries

import slick.jdbc.H2Profile.api._
import slick.jdbc.MySQLProfile

import scala.concurrent._
import scala.concurrent.duration._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  val program =
    music.recreateTable
      .andThen(music.insertTestData)
      .andThen(music.selectCombinedQuery.result)

  def main(args: Array[String]): Unit = {
    val albums: List[String] =
      Await.result(database.run(program), 5.seconds)

    albums.foreach(println)
  }
}
