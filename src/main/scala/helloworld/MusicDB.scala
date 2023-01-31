package helloworld

import slick.jdbc.JdbcProfile

class MusicDB(val profile: JdbcProfile) {
  import profile.api._

  class AlbumTable(tag: Tag) extends Table[Album](tag, "albums") {
    def artist = column[String]("artist")
    def title = column[String]("title")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (artist, title, id).mapTo[Album]
  }

  lazy val AlbumTable = TableQuery[AlbumTable]

  val createTable: DBIO[Unit] =
    AlbumTable.schema.createIfNotExists

  val dropTable: DBIO[Unit] =
    AlbumTable.schema.dropIfExists

  val insertTestData: DBIO[Option[Int]] =
    AlbumTable ++= Seq(
      Album("Keyboard Cat", "Keyboard Cat's Greatest Hits"),
      Album("Spice Girls", "Spice"),
      Album("Rick Astley", "Whenever You Need Somebody"),
      Album("Manowar", "The Triumph of Steel"),
      Album("Justin Bieber", "Believe")
    )

  val selectTestData: DBIO[List[Album]] =
    AlbumTable.to[List].result
}
