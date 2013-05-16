package com.antonfagerberg.instagram

import java.net.SocketTimeoutException
import net.liftweb.json._
import net.liftweb.json.JsonParser.ParseException
import scala.Some
import scalaj.http.{HttpException, HttpOptions, Http}
import com.antonfagerberg.instagram.responses.{Meta, Pagination, Response}

object Request {
  implicit val formats = DefaultFormats

  /** Send GET request to an URL and parse the response to an appropriate case class.
    *
    * @param url        URL to send GET request to.
    * @param extractor  Convert the data section to the corresponding case class.
    * @param timeOut    Request max time before time out.
    * @tparam T         Type the data section should be parsed to.
    * @return
    */
  def getJson[T](url: String, extractor: (JsonAST.JValue => Option[T]), timeOut: Int = 10000): Response[T] = {
    json(parse(Http(url).options(HttpOptions.connTimeout(timeOut), HttpOptions.readTimeout(timeOut)).asString), extractor, url)
  }

  /** Send POST request to an URL and parse the response to an appropriate case class.
    *
    * @param url        URL to send POST request to.
    * @param extractor  Convert the data section to the corresponding case class.
    * @param timeOut    Request max time before time out.
    * @tparam T         Type the data section should be parsed to.
    * @return
    */
  def postJson[T](url: String, extractor: (JsonAST.JValue => Option[T]), parameters: List[(String, String)], timeOut: Int = 10000): Response[T] = {
    json(parse(Http.post(url).params(parameters).options(HttpOptions.connTimeout(timeOut), HttpOptions.readTimeout(timeOut)).asString), extractor, url)
  }

  /** Request JSON from Instagram and parse it to a Response.
    *
    * @param jsonRequest  JSON request to send and parse response from to appropriate case class.
    * @param extractor    Convert the data section to the corresponding case class.
    * @tparam T           Type the data section should be parsed to.
    * @return             com.antonfagerberg.instagram.responses.Response with data if successful, pagination if it exists and always a meta section
    *                     (which possibly contains error information).
    */
  private def json[T](jsonRequest: => JValue, extractor: (JValue => Option[T]), url: String): Response[T] = {
    try {
      try {
        val jsonResponse = jsonRequest

        Response(
          extractor((jsonResponse \ "data")),
          (jsonResponse \ "pagination").extract[Option[Pagination]],
          (jsonResponse \ "meta").extract[Meta]
        )
      } catch {
        case e: HttpException => Response(None, None, (parse(e.body) \ "meta").extract[Meta])
        case e: SocketTimeoutException => Response(None, None, Meta(Some("SocketTimeoutException"), -2, Some(s"Read timed out for URL: $url.")))
      }
    } catch {
      case e: ParseException => Response(None, None, Meta(Some("ParseException"), -1, Some(s"Unable to parse JSON response from Instagram for URL: $url.")))
      case e: MappingException => Response(None, None, Meta(Some("MappingException"), -3, Some(s"Unable to map JSON response from Instagram for URL: $url.")))
    }
  }

}