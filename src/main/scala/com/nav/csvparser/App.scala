package com.nav.csvparser

import scala.collection.mutable

/**
 * Application to search on movie titles to get stats like cast, crew, or recommendations in case exact match is not found
 *
 */
object App {
  def main(args: Array[String]) {
    val MOVIES_METADATA = "src/main/resources/movies_metadata.csv"
    val MOVIES_CREDITS = "src/main/resources/credits.csv"
    val MOVIES_KEYWORDS = "src/main/resources/keywords.csv"

    // Get user input
    println("\nType Movie Name : ")
    val input = scala.io.StdIn.readLine().trim()

    if (input.trim.isEmpty) {
      println("Please enter some text!")
    } else {

      // Get the movie Id from name
      println("Searching for '" + input + "' ....")
      val id = new MoviesCSVReader(MOVIES_METADATA).matchMovieByName(input.toLowerCase)
      // Get the movie cast/crew from Id
      if (id.equalsIgnoreCase("NoMatch")) {
        println("Ooops, did not find any movie title with '" + input + "' ....")
        println("Searching for keyword match ....")
        val ids = new MoviesCSVReader(MOVIES_KEYWORDS).readKeywords(input.toLowerCase)
        if(ids.size > 0) {
          println("Here are some recommendations ....")
          //ids.foreach(println)
          var top10Ids = ids.keys.toList
          val movieMap = new MoviesCSVReader(MOVIES_METADATA).matchMovieByIds(top10Ids)
          var count = 1
          ids.foreach { case (k, v) => {
            if (movieMap.contains(k)) println("  " + count + ". " + movieMap(k) + " (" + v + " match/es)");
            count = count + 1
          }}
        } else {
          println("Sorry, nothing found.")
        }
      } else {
        println("Found the title [id=" + id + "] ....")
        println("Now looking for the cast/crew of " + input + " ....")
        // Get the movie cast/crew from Id
        val creditData = new MoviesCSVReader(MOVIES_CREDITS).readCreditData(id)
        //println("Id for " + input + " is: " + id)
        if (creditData == null) {
          println("Unable to find the credits for " + input)
        } else {
          print("Here is the cast & crew details >>>>>\n\n")
          for ((k, v) <- creditData) {
            println(k + "(" + v.length + ") -> " + v.mkString(", "))
          }
        }
      }
    }
  }
}
