package com.antonfagerberg.instagram

import net.liftweb.json.DefaultFormats
import com.antonfagerberg.instagram.responses._

class Instagram(accessTokenOrClientId: Either[String, String], timeOut: Int = 10000) {
  implicit val formats = DefaultFormats

  val authentication = accessTokenOrClientId match {
    case Left(accessToken) => s"access_token=$accessToken"
    case Right(clientId) => s"client_id=$clientId"
  }

  /** Get basic information about a name.
    *
    * @param userId Id-number of the name to get information about.
    * @return       Response.
    */
  def userInfo(userId: String): Response[Profile] = {
    Request.getJson(s"https://api.instagram.com/v1/users/$userId/?$authentication", json => Some(json.extract[Profile]), timeOut)
  }

  /** Search for a user by name.
    *
    * @param name   Name of user.
    * @param count  Max number of results to return.
    * @param minId  Return media later than this.
    * @param maxId  Return media earlier than this.
    * @return       Response.
    */
  def userSearch(name: String, count: Option[Int] = None, minId: Option[String] = None, maxId:  Option[String] = None): Response[List[UserSearch]] = {
    Request.getJson(s"https://api.instagram.com/v1/users/search?$authentication&q=$name&count=${count.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", (json => Some(json.extract[List[UserSearch]])), timeOut)
  }

  /** Get the list of users this user follows.
    *
    * @param userId Id-number of user.
    * @return       Response.
    */
  def follows(userId: String): Response[List[User]] = {
    Request.getJson(s"https://api.instagram.com/v1/users/$userId/follows?$authentication", json => Some(json.extract[List[User]]), timeOut)
  }

  /** Get the list of users this user is followed by.
    *
    * @param userId Id-number of user.
    * @return       Response.
    */
  def followedBy(userId: String): Response[List[User]] = {
    Request.getJson(s"https://api.instagram.com/v1/users/$userId/followed-by?$authentication", json => Some(json.extract[List[User]]), timeOut)
  }

  /** See the authenticated user's feed.
    *
    * @param count  Max number of results to return.
    * @param minId  Return media later than this.
    * @param maxId  Return media earlier than this.
    * @return       Response.
    */
  def feed(count: Option[Int] = None, minId: Option[String] = None, maxId:  Option[String] = None): Response[List[Media]] = {
    Request.getJson(s"https://api.instagram.com/v1/users/self/feed?$authentication&count=${count.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", json => Some(json.extract[List[Media]]), timeOut)
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
    Request.getJson(s"https://api.instagram.com/v1/users/$userId/media/recent/?$authentication&max_timestamp=${maxTimestamp.mkString}&min_timestamp=${minTimestamp.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", json => Some(json.extract[List[Media]]), timeOut)
  }

  /** Search for media in a given area.
    * The default time span is set to 5 days. The time span must not exceed 7 days. Defaults time stamps cover the last 5 days.
    *
    * @param coordinates  Latitude & Longitude coordinates.
    * @param minTimestamp Return media after this UNIX timestamp.
    * @param maxTimestamp Return media before this UNIX timestamp.
    * @param distance     Default is 1000m (distance=1000), max distance is 5000.
    * @return             Response.
    */
  def mediaSearch(coordinates: (String, String), minTimestamp: Option[String] = None, maxTimestamp: Option[String] = None, distance: Option[Int] = None): Response[List[Media]] = {
    Request.getJson(s"https://api.instagram.com/v1/media/search?$authentication&lat=${coordinates._1}&lng=${coordinates._2}&min_timestamp=${minTimestamp.getOrElse("")}&max_timestamp=${maxTimestamp.getOrElse("")}&distance=${distance.getOrElse("")}", json => Some(json.extract[List[Media]]), timeOut)
  }

  /** See the authenticated user's list of liked media.
    *
    * @param count      Max number of results to return.
    * @param maxLikeId  Return media liked before this id.
    * @return           Response.
    */
  def liked(count: Option[Int] = None, maxLikeId: Option[String] = None): Response[List[Media]] = {
    Request.getJson(s"https://api.instagram.com/v1/users/self/media/liked?$authentication&count=${count.mkString}&max_like_id=${maxLikeId.mkString}", json => Some(json.extract[List[Media]]), timeOut)
  }

  /** Get information about a media object.
    *
    * @param mediaId  Id-number of media object.
    * @return         Response.
    */
  def media(mediaId: String): Response[Media] = {
    Request.getJson(s"https://api.instagram.com/v1/media/$mediaId?$authentication", json => Some(json.extract[Media]), timeOut)
  }

  /** Get a list of currently popular media.
    *
    * @return Response.
    */
  def popular: Response[List[Media]] = {
    Request.getJson(s"https://api.instagram.com/v1/media/popular?$authentication", json => Some(json.extract[List[Media]]), timeOut)
  }

  /** Get a full list of comments on a media.
    *
    * @param mediaId  Id-number of media object.
    * @return         Response.
    */
  def comments(mediaId: String): Response[List[Comment]] = {
    Request.getJson(s"https://api.instagram.com/v1/media/$mediaId/comments?$authentication", json => Some(json.extract[List[Comment]]), timeOut)
  }

  /** Get a list of users who have liked this media.
    *
    * @param mediaId  Id-number of media object.
    * @return         Response.
    */
  def likes(mediaId: String): Response[List[User]] = {
    Request.getJson(s"https://api.instagram.com/v1/media/$mediaId/likes?$authentication", json => Some(json.extract[List[User]]), timeOut)
  }

  /** Get information about a tag object.
    *
    * @param tag  Name of tag to search for.
    * @return     Response.
    */
  def tagInformation(tag: String): Response[Tag] = {
    Request.getJson(s"https://api.instagram.com/v1/tags/$tag?$authentication", json => Some(json.extract[Tag]), timeOut)
  }

  /** Get information about a tag object.
    *
    * @param tag  Name of tag to search for.
    * @return     Response.
    */
  def tagSearch(tag: String): Response[List[Tag]] = {
    Request.getJson(s"https://api.instagram.com/v1/tags/search?q=$tag&$authentication", json => Some(json.extract[List[Tag]]), timeOut)
  }

  /** Get a list of recently tagged media.
    *
    * @param tag      Name of tag to search for.
    * @param minTagId Return media later than this.
    * @param maxTagId Return media earlier than this.
    * @return         Response.
    */
  def tagRecent(tag: String, minTagId: Option[String] = None, maxTagId: Option[String] = None): Response[List[Media]] = {
    Request.getJson(s"https://api.instagram.com/v1/tags/$tag/media/recent?$authentication&min_tag_id=${minTagId.mkString}&max_tag_id=${maxTagId.mkString}", json => Some(json.extract[List[Media]]), timeOut)
  }

  /** Get information about a location.
    *
    * @param locationId Id-number of location.
    * @return           Response.
    */
  def location(locationId: String): Response[Location] = {
    Request.getJson(s"https://api.instagram.com/v1/locations/$locationId?$authentication", json => Some(json.extract[Location]), timeOut)
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
    Request.getJson(s"https://api.instagram.com/v1/locations/$locationId/media/recent?$authentication&min_timestamp=${minTimestamp.mkString}&max_timestamp=${maxTimestamp.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}", json => Some(json.extract[List[Media]]), timeOut)
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

    Request.getJson(s"https://api.instagram.com/v1/locations/search?$authentication&foursquare_v2_id=${foursquareV2Id.mkString}$latitudeLongitude", json => Some(json.extract[List[Location]]), timeOut)
  }

  /** Get information about a relationship to another user.
    *
    * @param userId Id-number of user.
    * @return       Response.
    */
  def relationship(userId: String): Response[Relationship] = Request.getJson(s"https://api.instagram.com/v1/users/$userId/relationship?$authentication", (json => Some(json.extract[Relationship])), timeOut)

  /** Send the request to update the relationship status.
    * This method is called from the methods named relationshipXXX.
    *
    * @param userId Id of user to follow.
    * @param action Action (follow/unfollow/block/unblock/approve/deny).
    * @return       Response.
    */
  private def updateRelationship(userId: String, action: String): Response[NoData] = {
    Request.postJson(s"https://api.instagram.com/v1/users/$userId/relationship?$authentication", json => None, List("action" -> action), timeOut)
  }

  /** Follow a user.
    *
    * @param userId Id of user to follow.
    * @return       Response.
    */
  def relationshipFollow(userId: String): Response[NoData] = {
    updateRelationship(userId, "follow")
  }

  /** Unfollow a user.
    *
    * @param userId Id of user to unfollow.
    * @return       Response.
    */
  def relationshipUnfollow(userId: String): Response[NoData] = {
    updateRelationship(userId, "unfollow")
  }

  /** Block a user.
    *
    * @param userId Id of user to block.
    * @return       Response.
    */
  def relationshipBlock(userId: String): Response[NoData] = {
    updateRelationship(userId, "block")
  }

  /** Unblock a user.
    *
    * @param userId Id of user to follow.
    * @return       Response.
    */
  def relationshipUnblock(userId: String): Response[NoData] = {
    updateRelationship(userId, "unblock")
  }

  /** Approve a follow request from a user.
    *
    * @param userId Id of user to follow.
    * @return       Response.
    */
  def relationshipApprove(userId: String): Response[NoData] = {
    updateRelationship(userId, "approve")
  }

  /** Deny a follow request from a user.
    * Note: this methid is deprectated - use relationshipIgnore instead.
    *
    * @param userId Id of user to follow.
    * @return       Response.
    */
  def relationshipDeny(userId: String): Response[NoData] = {
    relationshipIgnore(userId)
  }

  /** Ignore a follow request from a user.
    *
    * @param userId Id of user to follow.
    * @return       Response.
    */
  def relationshipIgnore(userId: String): Response[NoData] = {
    updateRelationship(userId, "ignore")
  }

  /** View users who has sent a follow request.
    *
    * @return       Response.
    */
  def relationshipRequests: Response[List[User]] = {
    Request.getJson(s"https://api.instagram.com/v1/users/self/requested-by?$authentication", json => Some(json.extract[List[User]]), timeOut)
  }

  /** Set a like on this media by the currently authenticated user.
    *
    * @param mediaId  Id of media object.
    * @return         Response.
    */
  def like(mediaId: String): Response[NoData] = {
    Request.postJson(s"https://api.instagram.com/v1/media/$mediaId/likes?$authentication", json => None, Nil, timeOut)
  }

  /** Remove a like on this media by the currently authenticated user.
    *
    * @param mediaId  Id of media object.
    * @return         Response.
    */
  def unlike(mediaId: String): Response[NoData] = {
    Request.postJson(s"https://api.instagram.com/v1/media/$mediaId/likes?$authentication", json => None, List("_method" -> "DELETE"), timeOut)
  }

  /**  Remove a comment either on the authenticated user's media or authored by the authenticated user.
    *
    * @param mediaId    Id of media object.
    * @param commentId  Id of comment.
    * @return           Response.
    */
  def commentDelete(mediaId: String, commentId: String): Response[NoData] = {
    Request.postJson(s"https://api.instagram.com/v1/media/$mediaId/comments/$commentId/?$authentication", json => None, List("_method" -> "DELETE"), timeOut)
  }

  /** Create a comment on a media.
    * Please email apidevelopers[at]instagram.com or visit http://bit.ly/instacomments for access.
    *
    * @param mediaId  Id of media object.
    * @param comment  Comment text.
    * @return         Response.
    */
  def comment(mediaId: String, comment: String): Response[NoData] = {
    Request.postJson(s"https://api.instagram.com/v1/media/$mediaId/comments?$authentication", json => None, List("text" -> comment), timeOut)
  }

  /** Generic query for a given URL.
    *
    * @param url  Instagram API URL.
    * @param m    Manifest.
    * @tparam T   Return type (use the cases classes from responses - or create your own)
    * @return     T wrapped in a Response.
    */
  def request[T](url: String)(implicit m: Manifest[T]): Response[T] = {
    Request.getJson(url, json => Some(json.extract[T]), timeOut)
  }
}
