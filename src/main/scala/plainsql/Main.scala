package plainsql

import slick.jdbc.MySQLProfile.api._
import slick.jdbc.MySQLProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  val program: DBIO[Vector[(Artist, Album)]] =
    music.recreateTables
      .andThen(music.insertAll)
      .andThen(music.selectAll)

  def main(args: Array[String]): Unit = {
    val results: Vector[(Artist, Album)] =
      Await.result(database.run(program.transactionally), 2.seconds)

    results.foreach(println)
  }
}
