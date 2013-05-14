package responses

case class User(
  username: String,
  full_name: String,
  id: String,
  profile_picture: String
) {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}