package responses

case class UserInfo(
  id: String,
  username: String,
  full_name: String,
  profile_picture: String,
  bio: String,
  website: String,
  counts: UserInfoCounts
) {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}

case class UserInfoCounts(
  media: Int,
  follows: Int,
  followed_by: Int
) {
  lazy val followedBy = followed_by
}