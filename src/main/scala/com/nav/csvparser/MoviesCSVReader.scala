package com.nav.csvparser

import scala.io.Source

/**
 * Implementation of [[MoviesReader]] responsible for reading movies data from different CSV files.
 *
 * @param fileName The name of the CSV file to be read.
 * @author Nav Megh
 */
class MoviesCSVReader(val fileName: String) extends MoviesReader {

  /** *
   * Iterates through all rows on movies_metadata.csv until a row with movie name is found.
   * It then parses the row to obtain various relevant fields and checks to see if the matched movie name corresponds to the actual title.
   * Once found, it returns the id field for that row, which represents the movie id
   *
   * @param movieName
   * @return
   */
  override def matchMovieByName(movieName: String): String = {
    val MovieDataRegex = """(.*,)\s*("*?.*"*?,)\s*(\d*,)\s*("*?.*,)\s*(.*,)(.*\d*),(.*\d*,)\s*([a-z]{2},)\s*(.*)(,"*.*),.*""".r
    if(movieName.isEmpty) return ""

    for (line <- Source.fromFile(fileName).getLines().drop(1).toVector) {
      //println(">>>: " + line)
      if (line.toLowerCase() contains movieName + ",") {
        line match {
          case MovieDataRegex(adult, collection, budget, genres, homepage, id, imdb_id, language, title, overview) => {
            //println(s"id: $id - $title \n");
            val titleString = s"$title".split(",").map(_.trim)
            if ((titleString.length > 0) && (movieName.equalsIgnoreCase(titleString(0)))) {
              //println("Stripped Title: " + titleString(0) + "\n");
              return s"$id";
            }
          }
          case _ => return ("NoMatch")
        }
      }
    }
    "NoMatch"
  }

  /** *
   * Iterates through all rows on movies_metadata.csv and matches all movie ids provided in the list.
   * It then parses each matching row to obtain movie title and returns a map of titles keyed by id.
   *
   * @param ids
   * @return
   */
  override def matchMovieByIds(ids: List[String]): scala.collection.mutable.Map[String, String] = {
    val MovieDataRegex = """(.*,)\s*("*?.*"*?,)\s*(\d*,)\s*("*?.*,)\s*(.*,)(.*\d*),(.*\d*,)\s*([a-z]{2},)\s*(.*)(,"*.*),.*""".r
    val movieMap = scala.collection.mutable.Map[String, String]()

    for (line <- Source.fromFile(fileName).getLines().drop(1).toVector) {
      if (matchesAny(line, ids)) {
        line match {
          case MovieDataRegex(adult, collection, budget, genres, homepage, id, imdb_id, language, title, overview) => {
            val titleString = s"$title".split(",").map(_.trim)
            if (titleString.length > 0) {
              //val movieObj = MovieData(s"$adult", s"$collection", s"$budget", s"$genres", s"$homepage", s"$id".trim(), s"$imdb_id", s"$language", titleString(0))
              movieMap(s"$id") = titleString(0)
            }
          }
          case _ => return movieMap
        }
      }
    }
    movieMap
  }

  /** *
   * Obtains the movie credits (Cast & crew) from the given movie Id. Looks into credits.csv file.
   * Parses the two json objects corresponding to the cast & crew, stripping out all the male/female cast/crew
   * and returns a map with keys indicating the data type (e.g. male crew, female cast. etc) and names as values.
   *
   * @param movieId
   * @return
   */
  override def readCreditData(movieId: String): Map[String, scala.collection.mutable.MutableList[String]] = {
    val MovieCreditRegex = """(.*\[.*\].*,)\s*(.*\[.*\].*,)\s*(\d*)""".r

    for (line <- Source.fromFile(fileName).getLines().drop(1).toVector) {
      //println(">>>: " + line)
      if (line contains movieId) {
        line match {
          case MovieCreditRegex(cast, crew, id) => {
            if (s"$id".trim().equalsIgnoreCase(movieId)) {
              return processJSON(s"$cast", "Cast") ++ processJSON(s"$crew", "Crew")
            }
          }
          case _ => return null
        }
      }
    }
    null
  }

  /** *
   * On no exact match for an entered movie name, it tries to matches with keywords from keywords.csv
   * If one of more matching keywords are found, it returns a map with keys are matched keywords and
   * their count as the value.
   *
   * @param movieName
   * @return
   */
  override def readKeywords(movieName: String): scala.collection.immutable.ListMap[String, Int] = {
    val keywordRegex = """(\d+),(.*)""".r
    val idMap = scala.collection.mutable.Map[String, Int]()

    for (line <- Source.fromFile(fileName).getLines().drop(1).toVector) {
      val matchCount = movieName.r.findAllMatchIn(line).length
      if (matchCount > 0) {
        line match {
          case keywordRegex(id, rest) => {
            //println(s"$id" + " - " + count)
            idMap(s"$id".trim()) = matchCount
          }
          //case _ =>
        }
      }
    }
    collection.immutable.ListMap(idMap.toList.sortWith(_._2 > _._2): _*).take(10)
  }

  /** *
   * Function responsible for parsing cast/crew json objects
   *
   * @param json
   * @param typ
   * @return
   */
  def processJSON(json: String, typ: String): Map[String, scala.collection.mutable.MutableList[String]] = {
    val femaleList = scala.collection.mutable.MutableList[String]()
    val maleList = scala.collection.mutable.MutableList[String]()

    val items = json.split("},")
    if (items.length > 0) {
      for (item <- items) {
        if (item contains "{") {
          val keyValPair = item.split(",").map(_.trim)
          val name = getName(keyValPair(5))
          if (!"none".equalsIgnoreCase(name)) {
            val genderIndex = if (typ.equalsIgnoreCase("Cast")) 3 else 2
            val gender = getGender(keyValPair(genderIndex))
            if ("F".equalsIgnoreCase(gender)) {
              femaleList += name
            } else if ("M".equalsIgnoreCase(gender)) {
              maleList += name
            }
          }
        }
      }
    }
    return Map("Male " + typ -> maleList, "Female " + typ -> femaleList)
  }

  /** *
   * Helper function to get the gender from corresponding key value pair
   *
   * @param keyValPair
   * @return
   */
  def getGender(keyValPair: String): String = {
    val a = keyValPair.split(":")
    if (a.length == 2 && a(0).trim().equalsIgnoreCase("'gender'") && a(1).trim().equalsIgnoreCase("2")) {
      "M"
    } else if (a.length == 2 && a(0).trim().equalsIgnoreCase("'gender'") && a(1).trim().equalsIgnoreCase("1")) {
      "F"
    } else {
      "E"
    }
  }

  /** *
   * Helper function to get the name from corresponding key value pair
   *
   * @param keyValPair
   * @return
   */
  def getName(keyValPair: String): String = {
    val a = keyValPair.split(":")
    if (a.length == 2 && a(0).trim().equalsIgnoreCase("'name'")) {
      a(1).trim().replaceAll("'", "")
    } else {
      "none"
    }
  }

  /** *
   * Checks to see if any of the string in the supplied list is present in the given line
   *
   * @param line
   * @param ids
   * @return
   */
  def matchesAny(line: String, ids: List[String]): Boolean = {
    ids.foreach((id) => {
      if (line contains "," + id + ",") return true
    })
    return false
  }

}

