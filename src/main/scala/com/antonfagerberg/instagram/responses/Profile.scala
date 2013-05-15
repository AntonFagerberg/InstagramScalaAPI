package com.antonfagerberg.instagram.responses

case class Profile(
  id: String,
  username: String,
  full_name: String,
  profile_picture: String,
  bio: String,
  website: String,
  counts: ProfileStats
) {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}

