package com.antonfagerberg.instagram.responses

case class Likes(
  count: Int,
  data: List[User]
) {
  lazy val users = data
}