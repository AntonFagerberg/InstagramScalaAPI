package com.antonfagerberg.instagram.responses

case class Location(
  id: Option[Int],
  latitude: Float,
  longitude: Float,
  name: Option[String]
)