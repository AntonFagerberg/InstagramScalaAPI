import net.liftweb.json.JsonParser.ParseException
import responses._
import net.liftweb.json._
import scalaj.http._

class Instagram(accessToken: String) {
  implicit val formats = DefaultFormats
  val timeOut = 5000

  private def jsonResponse[T](url: String, extractor: (JsonAST.JValue => T)): Either[T, Error] = {
    try {
      try {
        Left(extractor((parse(Http(url).options(HttpOptions.connTimeout(timeOut), HttpOptions.readTimeout(timeOut)).asString)) \ "data"))
      } catch {
        case e: HttpException => Right((parse(e.body) \ "meta").extract[Error])
      }
    } catch {
      case e: ParseException => Right(Error(Some("ParseException"), -1, Some(s"Unable to parse JSON response from Instagram for url: $url.")))
    }
  }

  def userInfo(userId: String): Either[UserInfo, Error] = {
    jsonResponse(s"https://api.instagram.com/v1/users/$userId/?access_token=$accessToken", _.extract[UserInfo])
  }

  def userSearch(query: String): Either[List[UserSearch], Error] = {
    jsonResponse(s"https://api.instagram.com/v1/users/search?q=$query&access_token=$accessToken", _.extract[List[UserSearch]])
  }

  def media(mediaId: String): Either[Media, Error] = {
    jsonResponse(s"https://api.instagram.com/v1/media/$mediaId?access_token=$accessToken", _.extract[Media])
  }
}
