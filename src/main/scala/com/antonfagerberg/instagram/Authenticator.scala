package com.antonfagerberg.instagram

import com.antonfagerberg.instagram.responses.{Authentication, Meta}
import net.liftweb.json._
import scala.util.Try
import scalaj.http.{Http, HttpException, HttpOptions}

object Authenticator {
  implicit val formats = DefaultFormats

  /** Create the URL to call when retrieving an authentication code.
    *
    * @param clientId       Client identifier. (You need to register this on instagram.com/developer)
    * @param redirectURI    URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param comments       Require comment scope.
    * @param relationships  Require relationships scope.
    * @param likes          Require likes scope.
    * @return               URL to call.
    */
  def codeURL(clientId: String, redirectURI: String, comments: Boolean = false, relationships: Boolean = false, likes: Boolean = false): String = {
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI&response_type=code&${scopes(comments, relationships, likes)}"
  }

  /** Create the URL to call when retrieving an access token.
    *
    * @param clientId       Client identifier. (You need to register this on instagram.com/developer)
    * @param redirectURI    URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param comments       Require comment scope.
    * @param relationships  Require relationships scope.
    * @param likes          Require likes scope.
    * @return               URL to call.
    */
  def tokenURL(clientId: String, redirectURI: String, comments: Boolean = false, relationships: Boolean = false, likes: Boolean = false): String = {
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI&response_type=token&${scopes(comments, relationships, likes)}"
  }

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
    val response =
      Try {
        val jsonResponse = parse(
          Http.post(url).params(
            List(
              "client_id" -> clientId,
              "client_secret" -> clientSecret,
              "redirect_uri" -> redirectURI,
              "code" -> code,
              "grant_type" -> "authorization_code"
            )
          ).options(
            HttpOptions.connTimeout(timeOut),
            HttpOptions.readTimeout(timeOut)).asString
          )

        Left(jsonResponse.extract[Authentication])
      } recoverWith {
        case e: HttpException => Try(
          Right(
            parse(e.body).extract[Meta]
          )
        )
      } recover {
        case e: Exception => Right(
          responses.Meta(
            Some(e.toString),
            -1,
            Some(e.getStackTrace.map(_.toString).mkString("\n"))
          )
        )
      }

    response.get
  }

  /** Scope string which will be append to URL on demand.
    *
    * @param comments       Comments scope.
    * @param relationships  Relationships scope.
    * @param likes          Likes scope.
    * @return
    */
  private def scopes(comments: Boolean = false, relationships: Boolean = false, likes: Boolean = false): String = {
    val scopes = List(
      if (comments) "comments" else "",
      if (relationships) "relationships" else "",
      if (likes) "likes" else ""
    ).filterNot(_ == "")

    if (scopes.headOption.isDefined) scopes.mkString("scope=", "+", "")
    else ""
  }
}
