package actions

import slick.jdbc.JdbcProfile

class MusicDB(val profile: JdbcProfile) {
  import profile.api._

  class AlbumTable(tag: Tag) extends Table[Album](tag, "albums") {
    def artist = column[String]("artist")
    def title = column[String]("title")
    def year = column[Int]("year")
    def rating = column[Rating]("rating")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (artist, title, year, rating, id).mapTo[Album]
  }

  lazy val AlbumTable = TableQuery[AlbumTable]

  val recreateTable: DBIO[Unit] =
    AlbumTable.schema.dropIfExists
      .andThen(AlbumTable.schema.create)

  val selectAlbums: DBIO[List[String]] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(_.title)
      .to[List]
      .result

  val updateKeyboardCat1: DBIO[Int] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(_.title)
      .update("Even Greater Hits")

  val updateKeyboardCat2: DBIO[Int] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(a => (a.title, a.year))
      .update(("Even Greater Hits", 2010))

  val deleteJustinBieber: DBIO[Int] =
    AlbumTable
      .filter(_.artist === "Justin Bieber")
      .delete

  val insertPinkFloyd: DBIO[Int] =
    AlbumTable += Album("Pink Floyd", "Dark Side of the Moon", 1978, Rating.Awesome)

  val insertAlbums: DBIO[Option[Int]] =
    AlbumTable ++= Seq(
      Album("Keyboard Cat", "Keyboard Cat's Greatest Hits", 2009, Rating.Awesome),
      Album("Spice Girls", "Spice", 1996, Rating.Good),
      Album("Rick Astley", "Whenever You Need Somebody", 1987, Rating.NotBad),
      Album("Manowar", "The Triumph of Steel", 1992, Rating.Meh),
      Album("Justin Bieber", "Believe", 2013, Rating.Aaargh)
    )
}
