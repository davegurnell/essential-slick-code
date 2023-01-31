package queries

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

  val insertTestData: DBIO[Option[Int]] =
    AlbumTable ++= Seq(
      Album("Keyboard Cat", "Keyboard Cat's Greatest Hits", 2009, Rating.Awesome),
      Album("Spice Girls", "Spice", 1996, Rating.Good),
      Album("Rick Astley", "Whenever You Need Somebody", 1987, Rating.NotBad),
      Album("Manowar", "The Triumph of Steel", 1992, Rating.Meh),
      Album("Justin Bieber", "Believe", 2013, Rating.Aaargh)
    )

  val selectAllQuery: Query[AlbumTable, Album, Seq] =
    AlbumTable

  val selectWhereQuery: Query[AlbumTable, Album, List] =
    AlbumTable
      .filter(_.rating === (Rating.Awesome: Rating))
      .to[List]

  val selectSortedQuery1: Query[AlbumTable, Album, List] =
    AlbumTable
      .sortBy(_.year.asc)
      .to[List]

  val selectSortedQuery2: Query[AlbumTable, Album, List] =
    AlbumTable
      .sortBy(a => (a.year.asc, a.rating.asc))
      .to[List]

  val selectPagedQuery: Query[AlbumTable, Album, List] =
    AlbumTable
      .drop(2)
      .take(1)
      .to[List]

  val selectColumnsQuery1: Query[Rep[String], String, List] =
    AlbumTable
      .map(_.title)
      .to[List]

  val selectColumnsQuery2: Query[(Rep[String], Rep[String]), (String, String), List] =
    AlbumTable
      .map(a => (a.artist, a.title))
      .to[List]

  val selectCombinedQuery: Query[Rep[String], String, List] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(_.title)
      .to[List]

  val selectPagedAction1: DBIO[List[Album]] =
    selectPagedQuery.result

  val selectPagedAction2: DBIO[Option[Album]] =
    selectPagedQuery.result.headOption

  val selectPagedAction3: DBIO[Album] =
    selectPagedQuery.result.head
}
