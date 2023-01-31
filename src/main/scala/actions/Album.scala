package actions

case class Album(
  artist: String,
  title: String,
  year: Int,
  rating: Rating,
  id: Long = 0L,
)
