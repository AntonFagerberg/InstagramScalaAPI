package com.antonfagerberg.instagram.responses

case class UserSearch (
  username: String,
  profile_picture: String,
  full_name: String,
  id: String
) {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}
