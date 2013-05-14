package responses

case class Error(
  error_type: Option[String],
  code: Int,
  error_message: Option[String]
)
