package responses

case class Comments(
  data: List[Comment],
  count: Int
)