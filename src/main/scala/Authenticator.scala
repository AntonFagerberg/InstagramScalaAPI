import java.net.SocketTimeoutException
import responses.{Meta, Authentication}
import net.liftweb.json._
import net.liftweb.json.JsonParser.ParseException
import scalaj.http.{HttpException, HttpOptions, Http}

object Authenticator {
  implicit val formats = DefaultFormats

  /** Create the URL to call when retrieving an authentication code.
    *
    * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
    * @param redirectURI  URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @return             URL to call.
    */
  def codeURL(clientId: String, redirectURI: String): String =
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI&response_type=code"

  /** Create the URL to call when retrieving an access token.
    *
    * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
    * @param redirectURI  URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @return             URL to call.
    */
  def tokenURL(clientId: String, redirectURI: String): String =
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI&response_type=token"

  /** Post request to exchange a authentication code against an access token.
    * Note that an authentication code is valid one time only.
    *
    * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
    * @param clientSecret Client secret. (You need to register this on instagram.com/developer)
    * @param redirectURI  URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param code         Authentication code. You can retrieve it via codeURL.
    * @param timeOut      Timeout value for request.
    * @return             Authentication with access token and user information on success or Meta on failure.
    */
  def requestToken(clientId: String, clientSecret: String, redirectURI: String, code: String, timeOut: Int = 10000): Either[Authentication, Meta] = {
    val url = "https://api.instagram.com/oauth/access_token"

    try {
      try {
        val jsonResponse = parse(Http.post(url).params(List("client_id" -> clientId, "client_secret" -> clientSecret, "redirect_uri" -> redirectURI, "code" -> code, "grant_type" -> "authorization_code")).options(HttpOptions.connTimeout(timeOut), HttpOptions.readTimeout(timeOut)).asString)
        Left(jsonResponse.extract[Authentication])
      } catch {
        case e: HttpException => Right(parse(e.body).extract[Meta])
        case e: SocketTimeoutException => Right(Meta(Some("SocketTimeoutException"), -2, Some(s"Read timed out for URL: $url.")))
      }
    } catch {
      case e: ParseException => Right(Meta(Some("ParseException"), -1, Some(s"Unable to parse JSON response from Instagram for URL: $url.")))
      case e: MappingException => Right(Meta(Some("MappingException"), -3, Some(s"Unable to map JSON response from Instagram for URL: $url.")))
    }
  }
}
