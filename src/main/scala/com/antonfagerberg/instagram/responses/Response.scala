package com.antonfagerberg.instagram.responses

case class Response[T](
  data: Option[T],
  pagination: Option[Pagination],
  meta: Meta
)