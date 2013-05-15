import responses.{Meta, Pagination}

case class Response[T](
  data: Option[T],
  pagination: Option[Pagination],
  meta: Meta
)