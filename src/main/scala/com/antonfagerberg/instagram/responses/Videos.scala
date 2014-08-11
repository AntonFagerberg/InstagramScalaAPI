package com.antonfagerberg.instagram.responses

case class Videos(
  low_resolution: Video,
  standard_resolution: Video
) {
  lazy val lowResolution = low_resolution
  lazy val standardResolution = standard_resolution
}
