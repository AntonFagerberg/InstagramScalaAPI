package responses

case class Comment(
  created_time: String,
  text: String,
  from: User,
  id: String
) {
  lazy val createdTime = created_time
}