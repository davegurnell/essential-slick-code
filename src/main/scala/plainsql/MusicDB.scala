package plainsql

import slick.jdbc.{GetResult, JdbcProfile}

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
    def artistId = column[Long]("artist_id")
    def title = column[String]("title")
    def year = column[Int]("year")
    def rating = column[Rating]("rating")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (artistId, title, year, rating, id).mapTo[Album]
  }

  lazy val AlbumTable = TableQuery[AlbumTable]

  implicit val ratingGetResult: GetResult[Rating] =
    GetResult(r => Rating.fromInt(r.nextInt()))

  implicit val artistGetResult: GetResult[Artist] =
    GetResult(result => Artist(name = result.nextString(), id = result.nextLong()))

  implicit val albumGetResult: GetResult[Album] =
    GetResult(result => Album(result.nextInt(), result.nextString(), result.nextInt(), result.<<[Rating], result.nextInt()))

  val recreateTables: DBIO[Unit] =
    AlbumTable.schema.dropIfExists
      .andThen(ArtistTable.schema.dropIfExists)
      .andThen(ArtistTable.schema.create)
      .andThen(AlbumTable.schema.create)

  val insertArtists: DBIO[Int] =
    sqlu"""
    insert into "artists" ("name") values
      ( 'Keyboard Cat'    ),
      ( 'Spice Girls'     ),
      ( 'Rick Astley'     ),
      ( 'My Mate''s Band' );
    """

  def findArtistId(name: String): DBIO[Int] =
    sql"""select "id" from "artists" where "name" = $name""".as[Int].head

  def insertAlbums: DBIO[Unit] =
    for {
      catId   <- findArtistId("Keyboard Cat")
      spiceId <- findArtistId("Spice Girls")
      rickId  <- findArtistId("Rick Astley")
      _       <- sqlu"""
                 insert into "albums" ("artist_id", "title", "year", "rating") values
                   ( $catId   , 'Keyboard Cat''s Greatest Hits' , 2009 , 5 ),
                   ( $spiceId , 'Spice'                         , 1996 , 4 ),
                   ( $spiceId , 'Forever'                       , 2000 , 3 ),
                   ( $rickId  , 'Whenever You Need Somebody'    , 1987 , 5 ),
                   ( $rickId  , 'Hold Me in Your Arms'          , 1988 , 4 ),
                   ( $rickId  , 'Free'                          , 1991 , 3 ),
                   ( $rickId  , 'Body & Soul'                   , 1993 , 3 ),
                   ( $rickId  , 'Keep It Turned On'             , 2001 , 3 ),
                   ( $rickId  , 'Portrait'                      , 2005 , 2 ),
                   ( $rickId  , 'My Red Book'                   , 2013 , 3 );
                 """
    } yield ()

  val insertAll: DBIO[Unit] =
    insertArtists.andThen(insertAlbums)

  val selectAll: DBIO[Vector[(Artist, Album)]] =
    sql"""
    select * from "artists", "albums"
    where "artists"."id" = "albums"."artist_id"
    """.as[(Artist, Album)]

  val selectAlbums: DBIO[Vector[Album]] =
    sql""" select * from "albums" """.as[Album]

  val selectArtists: DBIO[Vector[Artist]] =
    sql""" select * from "artists" """.as[Artist]

}
