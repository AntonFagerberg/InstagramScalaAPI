object Demo {
  def main(args: Array[String]) {
    val instagram = new Instagram(accessTokenOrClientId = "Put either Left('accessToken') or Right('clientId') from Instagram here.")

    println("Here is some stuff you can do:")

    println(s"Get basic information about a name: ${instagram.userInfo("12895238")}")

    println(s"Search for a name by name: ${instagram.userSearch("AntonFagerberg")}")

//    println(s"Get the list of users this user follows: ${instagram.follows("12895238")}")

//    println(s"Get the list of users this user is followed by: ${instagram.followedBy("12895238")}")

    println(s"See the authenticated user's feed: ${instagram.feed()}")

//    println(s"Get the most recent media published by a user: ${instagram.mediaRecent("12895238")}")

//    println(s"See the authenticated user's list of liked media: ${instagram.liked()}")

    println(s"Get information about a media object: ${instagram.media("452194471682227494_12895238")}")

    println(s"Get a list of currently popular media: ${instagram.popular}")

//    println(s"Get a full list of comments on a media: ${instagram.comments("452194471682227494_12895238")}")

//    println(s"Get a list of users who have liked this media: ${instagram.likes("452194471682227494_12895238")}")

//    println(s"Get information about a tag object: ${instagram.tagInformation("hipster")}")

//    println(s"Get information about a tag object: ${instagram.tagSearch("snowy")}")

//    println(s"Get a list of recently tagged media: ${instagram.tagRecent("beer")}")

//    println(s"Get information about a location: ${instagram.location("1")}")

//    println(s"Get a list of media objects from a given location: ${instagram.locationMedia("1")}")

//    println(s"Search for a location by geographic coordinate: ${instagram.locationSearch(Some("48.858844" -> "2.294351"))}")
  }
}
