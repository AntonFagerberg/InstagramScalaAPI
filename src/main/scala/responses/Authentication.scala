package responses

case class Authentication(
  access_token: String,
  user: User
) {
  lazy val accessToken = access_token
}
