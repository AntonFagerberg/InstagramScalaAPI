package com.antonfagerberg.instagram.responses

case class Tag(
  media_count: Long,
  name: String
) {
  lazy val mediaCount = media_count
}