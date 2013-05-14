# Instagram API written in Scala.
Is it a Scala implementation of the Instagram API. Not all features are implemented yet, check the list below to see which features are implemented or not.

This project uses scalaj-http to send HTTP-requests and lift-json to parse the JSON response as case classes.

Note that you require a users access token to use this library. The support for use of client id instead is not yet implemented.

A Response object is returned from all methods and it contains:
 * Data - the stuff you actually want. This is the JSON response parsed as a corresponding object.
 * Pagination - if a large result set is retrieved, this object contains what you need to fetch the next "page" with information. You can pass the relevant pieces of this information to the same method as called before as optional parameters.
 * Meta - contains the status from Instagram (like 200 OK, 400 error). Internal errors such as parsing or socket errors will be stored in this meta object as well.

## Installation & Demo
You can use SBT to download the required dependencies and run the Demo-file:

```bash
sbt run
```

The demo-file contains all implemented methods but you need to uncomment some of them to try them (to avoid spamming the Instagram servers).

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
 * See the authenticated user's feed.
 * Get the most recent media published by a user.
 * See the authenticated user's list of liked media.
 * Get the list of users this user follows.
 * Get the list of users this user is followed by.
 * Get a list of currently popular media.
 * Get a full list of comments on a media.
 * Get a list of users who have liked this media.
 * Get information about a tag object.
 * Get a list of recently tagged media.
 * Search for tags by name.
 * Get information about a location.
 * Search for a location by geographic coordinate.
 * Get a list of media objects from a given location.
 * Token support.

### Not yet implemented
 * List the users who have requested to follow.
 * Get information about a relationship to another user.
 * Modify the relationship with target user.
 * Search for media in a given area. The default time span is set to 5 days. The time span must not exceed 7 days. Defaults time stamps cover the last 5 days.
 * Create a comment on a media. Please email apidevelopers[at]instagram.com for access.
 * Remove a comment.
 * Set a like on this media by the current user.
 * Remove a like on this media by the current user.
 * Get recent media from a geography subscription.
 * OAuth 2.0
 * Client ID support (only token is used currently).
 * Pagination

 ## OAuth
 Note that Instagram uses OAuth 2.0 and this library does not implement it yet. The approach to do this is pretty straight forward and scalaj-http has implemented OAuth helper methods.

 To create an instance of the Instagram-object, you need the OAuth access token.

 You can read more about the authentication at: http://instagram.com/developer/authentication/