package plainsql

case class Album(
  artistId: Long,
  title: String,
  year: Int,
  rating: Rating,
  id: Long = 0L
)
