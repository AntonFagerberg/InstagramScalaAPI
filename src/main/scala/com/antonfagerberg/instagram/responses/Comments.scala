package com.antonfagerberg.instagram.responses

case class Comments(
  data: List[Comment],
  count: Int
)