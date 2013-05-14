package responses

case class ProfileStats(
  media: Int,
  follows: Int,
  followed_by: Int
) {
  lazy val followedBy = followed_by
}