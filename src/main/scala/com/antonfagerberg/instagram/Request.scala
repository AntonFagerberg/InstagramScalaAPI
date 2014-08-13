package com.antonfagerberg.instagram

import com.antonfagerberg.instagram.responses.{Meta, Pagination, Response}
import net.liftweb.json._
import scala.util.Try
import scalaj.http.{Http, HttpException, HttpOptions}

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
    val response =
      Try {
        val jsonResponse = jsonRequest

        Response(
          extractor(jsonResponse \ "data"),
          (jsonResponse \ "pagination").extract[Option[Pagination]],
          (jsonResponse \ "meta").extract[Meta]
        )
      } recoverWith {
        case e: HttpException => Try(
          Response(
            None,
            None,
            (parse(e.body) \ "meta").extract[Meta]
          ): Response[T]
        )
      } recover {
        case e: Exception => Response(
          None,
          None,
          Meta(Some(e.toString),
            -1,
            Some(e.getStackTrace.map(_.toString).mkString("\n"))
          )
        ): Response[T]
      }

    response.get
  }
}