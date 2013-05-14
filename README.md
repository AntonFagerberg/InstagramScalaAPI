# Instagram API written in Scala.
I'm currently implementing everything in the Instagram API. To see which features I've implemented so far, see the list below.

This project uses scalaj-http to send HTTP-requests and lift-json to parse the JSON response as case classes.

Until everything is implemented, the case classes are a unorganized and their names might change when I get a better grip of the relation between the results and how I might reuse them.

All responses returns Either the parsed JSON wrapped in a neat Case Class or an Error-object which represents the meta response from Instagram (or a parsing error).

## Installation
You can use SBT to download dependencies and run the Demo-file:

```bash
sbt run
```

### Dependencies
The current dependencies I use are:
```bash
"net.liftweb" %% "lift-json" % "2.5-M4"
"org.scalaj" %% "scalaj-http" % "0.3.7"
```

## Implementation status

### Implemented
 * Get basic information about a user.
 * Search for a user by name.
 * Get information about a media object.

### Not yet implemented
 * See the authenticated user's feed.
 * Get the most recent media published by a user.
 * See the authenticated user's list of liked media.
 * Get the list of users this user follows.
 * Get the list of users this user is followed by.
 * List the users who have requested to follow.
 * Get information about a relationship to another user.
 * Modify the relationship with target user.
 * Search for media in a given area. The default time span is set to 5 days. The time span must not exceed 7 days. Defaults time stamps cover the last 5 days.
 * Get a list of currently popular media.
 * Get a full list of comments on a media.
 * Create a comment on a media. Please email apidevelopers[at]instagram.com for access.
 * Remove a comment.
 * Get a list of users who have liked this media.
 * Set a like on this media by the current user.
 * Remove a like on this media by the current user.
 * Get information about a tag object.
 * Get a list of recently tagged media.
 * Search for tags by name.
 * Get information about a location.
 * Get a list of media objects from a given location.
 * Search for a location by geographic coordinate.
 * Get recent media from a geography subscription.

 ## OAuth
 Note that Instagram uses OAuth 2.0 and this library does not implement it yet. I might decide not to implement it at all in to this code since it is pretty straight forward and since scalaj-http already implements OAuth.

 To create an instance of the Instagram-object, you need the OAuth access token.

 You can read more about the authentication at: http://instagram.com/developer/authentication/