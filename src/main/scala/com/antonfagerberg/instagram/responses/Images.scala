package com.antonfagerberg.instagram.responses

case class Images(
  low_resolution: Image,
  thumbnail: Image,
  standard_resolution: Image
) {
  lazy val lowResolution = low_resolution
  lazy val standardResolution = standard_resolution
}