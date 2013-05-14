package responses

case class Media(
  users_in_photo: List[UserInPhoto],
  filter: String,
  tags: List[String],
  comments: Comments,
  caption: Comment,
  likes: Likes,
  link: String,
  user: UserSearch,
  created_time: String,
  images: Images,
  id: String,
  location: Option[Location]
) {
  lazy val usersInPhoto = users_in_photo
  lazy val createdTime = created_time
}