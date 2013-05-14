import java.net.SocketTimeoutException
import net.liftweb.json.JsonParser.ParseException
import responses._
import net.liftweb.json._
import scalaj.http._

class Instagram(accessToken: String, timeOut: Int = 10000) {
  implicit val formats = DefaultFormats

  case class Response[T](
    data: Option[T],
    pagination: Option[Pagination],
    meta: Meta
  )

  /** Request JSON from Instagram and parse it to a Response.
    *
    * @param url        URL to call for a response from Instagrams API.
    * @param extractor  Convert the data section to the corresponding case class.
    * @tparam T         Type the data section should be parsed to.
    * @return           Response with data if successful, pagination if it exists and always a meta section
    *                   (which possibly contains error information).
    */
  private def jsonResponse[T](url: String, extractor: (JsonAST.JValue => T)): Response[T] = {
    try {
      try {
        val jsonResponse = parse(Http(url).options(HttpOptions.connTimeout(timeOut), HttpOptions.readTimeout(timeOut)).asString)
        Response(
          Some(extractor((jsonResponse \ "data"))),
          (jsonResponse \ "pagination").extract[Option[Pagination]],
          (jsonResponse \ "meta").extract[Meta]
        )
      } catch {
        case e: HttpException => Response(None, None, (parse(e.body) \ "meta").extract[Meta])
        case e: SocketTimeoutException => Response(None, None, responses.Meta(Some("SocketTimeoutException"), -2, Some(s"Read timed out for URL: $url.")))
      }
    } catch {
      case e: ParseException => Response(None, None, responses.Meta(Some("ParseException"), -1, Some(s"Unable to parse JSON response from Instagram for URL: $url.")))
    }
  }

  /** Get basic information about a name.
    *
    * @param userId Id-number of the name to get information about.
    * @return       Response.
    */
  def userInfo(userId: String): Response[Profile] = {
    jsonResponse(s"https://api.instagram.com/v1/users/$userId/?access_token=$accessToken", _.extract[Profile])
  }

  /** Search for a name by name.
    *
    * @param name   Name of user.
    * @param count  Max number of results to return.
    * @param minId  Return media later than this.
    * @param maxId  Return media earlier than this.
    * @return       Response.
    */
  def userSearch(name: String, count: Option[Int] = None, minId: Option[String] = None, maxId:  Option[String] = None): Response[List[UserSearch]] = {
    jsonResponse(s"https://api.instagram.com/v1/users/search?q=$name&access_token=$accessToken&count=${count.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", _.extract[List[UserSearch]])
  }

  /** Get the list of users this user follows.
    *
    * @param userId Id-number of user.
    * @return       Response.
    */
  def follows(userId: String): Response[List[User]] = {
    jsonResponse(s"https://api.instagram.com/v1/users/$userId/follows?access_token=$accessToken", _.extract[List[User]])
  }

  /** Get the list of users this user is followed by.
    *
    * @param userId Id-number of user.
    * @return       Response.
    */
  def followedBy(userId: String): Response[List[User]] = {
    jsonResponse(s"https://api.instagram.com/v1/users/$userId/followed-by?access_token=$accessToken", _.extract[List[User]])
  }

  /** See the authenticated user's feed.
    *
    * @param count  Max number of results to return.
    * @param minId  Return media later than this.
    * @param maxId  Return media earlier than this.
    * @return       Response.
    */
  def feed(count: Option[Int] = None, minId: Option[String] = None, maxId:  Option[String] = None): Response[List[Media]] = {
    jsonResponse(s"https://api.instagram.com/v1/users/self/feed?access_token=$accessToken&count=${count.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", _.extract[List[Media]])
  }

  /** Get the most recent media published by a user.
    *
    * @param count        Max number of results to return.
    * @param minTimestamp Return media after this UNIX timestamp.
    * @param maxTimestamp Return media before this UNIX timestamp.
    * @param minId        Return media later than this.
    * @param maxId        Return media earlier than this.
    * @return             Response.
    */
  def mediaRecent(userId: String, count: Option[Int] = None, minTimestamp: Option[String] = None, maxTimestamp: Option[String] = None, minId: Option[String] = None, maxId: Option[String] = None): Response[List[Media]] = {
    jsonResponse(s"https://api.instagram.com/v1/users/${userId}/media/recent/?access_token=$accessToken&max_timestamp=${maxTimestamp.mkString}&min_timestamp=${minTimestamp.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", _.extract[List[Media]])
  }

  /** See the authenticated user's list of liked media.
    *
    * @param count      Max number of results to return.
    * @param maxLikeId  Return media liked before this id.
    * @return           Response.
    */
  def liked(count: Option[Int] = None, maxLikeId: Option[String] = None): Response[List[Media]] = {
    jsonResponse(s"https://api.instagram.com/v1/users/self/media/liked?access_token=$accessToken&count=${count.mkString}&max_like_id=${maxLikeId.mkString}", _.extract[List[Media]])
  }

  /** Get information about a media object.
    *
    * @param mediaId  Id-number of media object.
    * @return         Response.
    */
  def media(mediaId: String): Response[Media] = {
    jsonResponse(s"https://api.instagram.com/v1/media/$mediaId?access_token=$accessToken", _.extract[Media])
  }

  /** Get a list of currently popular media.
    *
    * @return Response.
    */
  def popular: Response[List[Media]] = {
    jsonResponse(s"https://api.instagram.com/v1/media/popular?access_token=$accessToken", _.extract[List[Media]])
  }

  /** Get a full list of comments on a media.
    *
    * @param mediaId  Id-number of media object.
    * @return         Response.
    */
  def comments(mediaId: String): Response[List[Comment]] = {
    jsonResponse(s"https://api.instagram.com/v1/media/$mediaId/comments?access_token=$accessToken", _.extract[List[Comment]])
  }

  /** Get a list of users who have liked this media.
    *
    * @param mediaId  Id-number of media object.
    * @return         Response.
    */
  def likes(mediaId: String): Response[List[User]] = {
    jsonResponse(s"https://api.instagram.com/v1/media/$mediaId/likes?access_token=$accessToken", _.extract[List[User]])
  }

  /** Get information about a tag object.
    *
    * @param tag  Name of tag to search for.
    * @return     Response.
    */
  def tagInformation(tag: String): Response[Tag] = {
    jsonResponse(s"https://api.instagram.com/v1/tags/$tag?access_token=$accessToken", _.extract[Tag])
  }

  /** Get information about a tag object.
    *
    * @param tag  Name of tag to search for.
    * @return     Response.
    */
  def tagSearch(tag: String): Response[List[Tag]] = {
    jsonResponse(s"https://api.instagram.com/v1/tags/search?q=$tag&access_token=$accessToken", _.extract[List[Tag]])
  }

  /** Get a list of recently tagged media.
    *
    * @param tag      Name of tag to search for.
    * @param minTagId Return media later than this.
    * @param maxTagId Return media earlier than this.
    * @return         Response.
    */
  def tagRecent(tag: String, minTagId: Option[String] = None, maxTagId: Option[String] = None): Response[List[Media]] = {
    jsonResponse(s"https://api.instagram.com/v1/tags/$tag/media/recent?access_token=$accessToken&min_tag_id=${minTagId.mkString}&max_tag_id=${maxTagId.mkString}", _.extract[List[Media]])
  }

  /** Get information about a location.
    *
    * @param locationId Id-number of location.
    * @return           Response.
    */
  def location(locationId: String): Response[Location] = {
    jsonResponse(s"https://api.instagram.com/v1/locations/$locationId?access_token=$accessToken", _.extract[Location])
  }

  /** Get a list of media objects from a given location.
    *
    * @param locationId   Id-number of location.
    * @param minTimestamp Return media after this UNIX timestamp.
    * @param maxTimestamp Return media before this UNIX timestamp.
    * @param minId        Return media later than this.
    * @param maxId        Return media earlier than this.
    * @return             Response.
    */
  def locationMedia(locationId: String, minTimestamp: Option[String] = None, maxTimestamp: Option[String] = None, minId: Option[String] = None, maxId: Option[String] = None): Response[List[Media]] = {
    jsonResponse(s"https://api.instagram.com/v1/locations/$locationId/media/recent?access_token=$accessToken&min_timestamp=${minTimestamp.mkString}&max_timestamp=${maxTimestamp.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", _.extract[List[Media]])
  }

  /** Search for a location by geographic coordinate.
    *
    * @param coordinates    Latitude & Longitude coordinates.
    * @param distance       Default is 1000m (distance=1000), max distance is 5000.
    * @param foursquareV2Id Returns a location mapped off of a foursquare v2 api location id. If used, you are not required to use lat and lng.
    * @return
    */
  def locationSearch(coordinates: Option[(String, String)], distance: Option[Int] = None, foursquareV2Id: Option[String] = None): Response[List[Location]] = {
    val latitudeLongitude =
      if (coordinates.isDefined) s"&lat=${coordinates.get._1}&lng=${coordinates.get._2}"
      else ""

    jsonResponse(s"https://api.instagram.com/v1/locations/search?access_token=$accessToken&foursquare_v2_id=${foursquareV2Id.mkString}${latitudeLongitude}", _.extract[List[Location]])
  }

}
