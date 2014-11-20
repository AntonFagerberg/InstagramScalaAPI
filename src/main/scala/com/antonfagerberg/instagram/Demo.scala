package com.antonfagerberg.instagram

import scalaj.http.{HttpOptions, Http}

object Demo {
  def main(args: Array[String]) {
    // You need to request these variables from Instagram: www.instagram.com/developer
    val clientId = "client-id"
    val clientSecret = "client-secret"
    val redirectURI = "redirect-URI"

    // Client-Side (Implicit) Authentication
    // Get a URL to call. This URL will return the TOKEN in the URI after the #-symbol (and you're done)..
    println(Authenticator.tokenURL(clientId, redirectURI))
    // You can append scopes by doing println(Authenticator.tokenURL(clientId, redirectURI, comments = true, relationships = true, likes = true))

    // Server-side (Explicit) Flow
    // Step 1: Get a URL to call. This URL will return the CODE to use in step 2 in the URI as a parameter code.
    println(Authenticator.codeURL(clientId, redirectURI))
    // You can append scopes by doing println(Authenticator.codeURL(clientId, redirectURI, comments = true, relationships = true, likes = true))    
    // Step 2: Request a token for the code requested in step 1 (the code is valid one time only).
    // This will return Either a Authentication object with access token and user information or a Meta object on failure.
    println(Authenticator.requestToken(clientId, clientSecret, redirectURI, code = "the-code-from-step-1"))

    // Use either Left("The access-token you requested") or Right("your client ID").
    // Note that not all features are available without an access token.
    val instagram = new Instagram(accessTokenOrClientId = Left("Put-access-token-here"))

    // println("Here is some stuff you can do (when you have a valid Access Token or Client Id):")

    // println(s"Get basic information about a name: ${instagram.userInfo("12895238")}")

    // println(s"Search for a name by name: ${instagram.userSearch("AntonFagerberg")}")

    // println(s"Get the list of users this user follows: ${instagram.follows("12895238")}")

    // println(s"Get the list of users this user is followed by: ${instagram.followedBy("12895238")}")

    // println(s"See the authenticated user's feed: ${instagram.feed()}") // Token

    // println(s"Get the most recent media published by a user: ${instagram.mediaRecent("12895238")}")

    // println(s"See the authenticated user's list of liked media: ${instagram.liked()}") // Token

    // println(s"Get information about a media object: ${instagram.media("452194471682227494_12895238")}")

    // println(s"Get a list of currently popular media: ${instagram.popular}")

    // println(s"Get a full list of comments on a media: ${instagram.comments("452194471682227494_12895238")}")

    // println(s"Get a list of users who have liked this media: ${instagram.likes("452194471682227494_12895238")}")

    // println(s"Get information about a tag object: ${instagram.tagInformation("hipster")}")

    // println(s"Get information about a tag object: ${instagram.tagSearch("snowy")}")

    // println(s"Get a list of recently tagged media: ${instagram.tagRecent("beer")}")

    // println(s"Get information about a location: ${instagram.location("1")}")

    // println(s"Get a list of media objects from a given location: ${instagram.locationMedia("1")}")

    // println(s"Search for a location by geographic coordinate: ${instagram.locationSearch(Some("48.858844" -> "2.294351"))}")

    // println(s"Like an image: ${instagram.like("457326401520123505_12895238")}")

    // println(s"Unlike an image: ${instagram.unlike("457326401520123505_12895238")}")

    // println(s"Relationship to a user: ${instagram.relationship("12895238")}")

    // println(s"Follow a user: ${instagram.relationshipFollow("12895238")}")

    // println(s"Unfollow a user: ${instagram.relationshipUnfollow("12895238")}")

    // println(s"Block a user: ${instagram.relationshipBlock("12895238")}")

    // println(s"Unblock a user: ${instagram.relationshipUnblock("12895238")}")

    // println(s"Approve a user: ${instagram.relationshipApprove("12895238")}")

    // println(s"Ignore a user: ${instagram.relationshipIgnore("12895238")}")

    // println(s"Relationship requests: ${instagram.relationshipRequests}")

    // Whitelisting of client is needed for comment access.
    // Please visit http://bit.ly/instacomments for commenting access
    // println(instagram.comment("457326401520123505_12895238", "test! :D"))
    // println(instagram.commentDelete("media-id", "comment-id"))

    // println(s"Search for images near a coordinate: ${instagram.mediaSearch("48.858844" -> "2.294351")}")

    //println(s"Generic query: ${instagram.request[List[Media]]("https://api.instagram.com/v1/tags/beer/media/recent?access_token=<SOME SECRET TOKEN>&max_tag_id=1416518266674445")}")
  }
}