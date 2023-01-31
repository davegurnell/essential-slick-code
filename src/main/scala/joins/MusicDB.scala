package joins

import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class MusicDB(val profile: JdbcProfile)(implicit executionContext: ExecutionContext) {
  import profile.api._

  class ArtistTable(tag: Tag) extends Table[Artist](tag, "artists") {
    def name = column[String]("name")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (name, id).mapTo[Artist]
  }

  lazy val ArtistTable = TableQuery[ArtistTable]

  class AlbumTable(tag: Tag) extends Table[Album](tag, "albums") {
    def artistId = column[Long]("artistId")
    def title = column[String]("title")
    def year = column[Int]("year")
    def rating = column[Rating]("rating")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (artistId, title, year, rating, id).mapTo[Album]
  }

  lazy val AlbumTable = TableQuery[AlbumTable]

  val createTables: DBIO[Unit] =
    ArtistTable.schema.create.andThen(AlbumTable.schema.create)

  val dropTables: DBIO[Unit] =
    AlbumTable.schema.drop.andThen(ArtistTable.schema.drop)

  val insertTestData: DBIO[Unit] =
    for {
      keyboardCatId <- ArtistTable.returning(ArtistTable.map(_.id)) += Artist("Keyboard Cat")
      spiceGirlsId  <- ArtistTable.returning(ArtistTable.map(_.id)) += Artist("Spice Girls")
      rickAstleyId  <- ArtistTable.returning(ArtistTable.map(_.id)) += Artist("Rick Astley")
      _             <- ArtistTable.returning(ArtistTable.map(_.id)) += Artist("My Mate's Band")
      _ <- AlbumTable ++= Seq(
             Album(keyboardCatId, "Keyboard Cat's Greatest Hits", 2009, Rating.Awesome),
             Album(spiceGirlsId, "Spice", 1996, Rating.Good),
             Album(spiceGirlsId, "Forever", 2000, Rating.Meh),
             Album(rickAstleyId, "Whenever You Need Somebody", 1987, Rating.Awesome),
             Album(rickAstleyId, "Hold Me in Your Arms", 1988, Rating.Good),
             Album(rickAstleyId, "Free", 1991, Rating.Meh),
             Album(rickAstleyId, "Body & Soul", 1993, Rating.Meh),
             Album(rickAstleyId, "Keep It Turned On", 2001, Rating.Meh),
             Album(rickAstleyId, "Portrait", 2005, Rating.NotBad),
             Album(rickAstleyId, "My Red Book", 2013, Rating.Meh)
           )
    } yield ()

  val implicitInnerJoin: DBIO[List[(Artist, Album)]] = {
    val query = for {
      artist <- ArtistTable
      album  <- AlbumTable if artist.id === album.artistId
    } yield (artist, album)

    query.to[List].result
  }

  val explicitInnerJoin: DBIO[List[(Artist, Album)]] =
    ArtistTable
      .join(AlbumTable)
      .on { case (artist, album) => artist.id === album.artistId }
      .to[List]
      .result
}
