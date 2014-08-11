package com.antonfagerberg.instagram.responses

case class Media(
  `type`: String,
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
  videos: Option[Videos],
  id: String,
  location: Option[Location]
) {
  lazy val usersInPhoto = users_in_photo
  lazy val createdTime = created_time
}
