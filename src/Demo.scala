object Demo {
  def main(args: Array[String]) {
    val instagram = new Instagram(accessToken = "")

    // User information.
    println(instagram.userInfo("1574083"))

    // Error information
    println(instagram.userInfo("123"))

    // Searching for users.
    instagram.userSearch("anton") match {
      case Right(error) => println
      case Left(results) => results.foreach(println)
    }

    // Media information for media id.
    println(instagram.media("3"))
  }
}
