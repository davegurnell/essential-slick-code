package joins

import slick.jdbc.H2Profile.api._
import slick.jdbc.MySQLProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

object Main {
  val music = new MusicDB(MySQLProfile)
  val database = Database.forConfig("musicdb.mysql")

  def main(args: Array[String]): Unit = {
    val program: DBIO[(List[(Artist, Album)], List[(Artist, Album)])] =
      for {
        _        <- music.createTables
        _        <- music.insertTestData
        results1 <- music.implicitInnerJoin
        results2 <- music.explicitInnerJoin
      } yield (results1, results2)

    val (results1, results2) = Await.result(database.run(program), 5.seconds)

    println("results1")
    results1.foreach(println)

    println("results2")
    results2.foreach(println)
  }

}
