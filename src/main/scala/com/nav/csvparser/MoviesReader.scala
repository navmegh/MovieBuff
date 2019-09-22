package com.nav.csvparser

trait MoviesReader {

  /**
   * @param movieName
   * @return movie id
   */
  def matchMovieByName(movieName: String) : String

  /**
   * @param idList - list of movie ids
   * @return Map of movie titles keyed by id
   */
  def matchMovieByIds(idList: List[String]) : Object

  /**
   * @param movieId
   * @return Map with list of cast & crew names keyed by type (cast/crew, male/female)
   */
  def readCreditData(movieId: String) : Object

  /**
   *
   * @param movieName
   * @return Map of movie ids keyed by matched keyword count
   */
  def readKeywords(movieName: String) : scala.collection.immutable.ListMap[String, Int]

}
