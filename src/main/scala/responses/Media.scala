package responses

case class Media(
  users_in_photo: List[UsersInPhoto],
  filter: String,
  tags: List[String],
  comments: Comment,
  caption: String,
  likes: Likes,
  link: String,
  user: UserSearch,
  created_time: String,
  images: Images,
  id: String,
  location: String
) {
  lazy val usersInPhoto = users_in_photo
  lazy val createdTime = created_time
}

case class Likes(
  count: Int,
  data: List[MediaUser]
)

case class UsersInPhoto(
  user: MediaUser,
  position: Position
)

case class MediaUser(
  username: String,
  full_name: String,
  id: String,
  profile_picture: String
) {
  lazy val fullName = full_name
}

case class Position(
  x: String,
  y: String
)

case class Comment(
  data: List[CommentData],
  count: Int
)

case class CommentData(
  created_time: String,
  text: String,
  from: MediaUser
) {
  lazy val createdTime = created_time
}

case class Images(
  low_resolution: Image,
  thumbnail: Image,
  standard_resolution: Image
) {
  lazy val lowResolution = low_resolution
  lazy val standardResolution = standard_resolution
}

case class Image(
  url: String,
  width: Int,
  height: Int
)