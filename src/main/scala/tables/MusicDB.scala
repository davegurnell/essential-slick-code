package tables

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

  val recreateTable: DBIO[Unit] =
    AlbumTable.schema.dropIfExists
      .andThen(AlbumTable.schema.create)

  val insertAlbums: DBIO[Option[Int]] =
    AlbumTable ++= Seq(
      Album("Keyboard Cat", "Keyboard Cat's Greatest Hits"), // released in 2009
      Album("Spice Girls", "Spice"), // released in 1996
      Album("Rick Astley", "Whenever You Need Somebody"), // released in 1987
      Album("Manowar", "The Triumph of Steel"), // released in 1992
      Album("Justin Bieber", "Believe")
    ) // released in 2013

  val selectAlbums: DBIO[List[Album]] =
    AlbumTable.to[List].result
}
