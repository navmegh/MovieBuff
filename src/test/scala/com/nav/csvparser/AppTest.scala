package com.nav.csvparser

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class AppTest extends FunSuite {

    test("Test blank input") {
        val movies = new MoviesCSVReader("src/main/resources/movies_metadata.csv").matchMovieByName("")
        movies shouldBe empty
    }

    test("Test Movie match by Title") {
        val movies = new MoviesCSVReader("src/main/resources/movies_metadata.csv").matchMovieByName("toy story")
        movies shouldBe "862"
    }

    test("Test Movie Credits by Id") {
        val movies = new MoviesCSVReader("src/main/resources/credits.csv").readCreditData("862")

        movies.size shouldBe 4
        movies("Male Cast").length shouldBe 9
        movies("Female Cast").length shouldBe 3
        movies("Male Crew").length shouldBe 29
        movies("Female Crew").length shouldBe 4

        movies("Male Cast").toSeq should equal (List("Tom Hanks","Tim Allen","Don Rickles","Jim Varney","Wallace Shawn","John Ratzenberger","Erik von Detten","R. Lee Ermey","Penn Jillette").toSeq)
        //movies("Female Cast") should contain theSameElementsAs List("Annie Potts","Laurie Metcalf","Sarah Freeman")
        movies("Female Cast").toSeq should equal (List("Annie Potts","Laurie Metcalf","Sarah Freeman").toSeq)
        movies("Male Crew").toSeq should equal (List("John Lasseter","Joss Whedon","Andrew Stanton","Joel Cohen","Ralph Guggenheim","Steve Jobs","Lee Unkrich","Ralph Eggleston","Robert Gordon","Randy Newman", "John Lasseter","Pete Docter","Ash Brannon","Randy Newman","Don Davis","Randy Newman","Andrew Stanton","Pete Docter","Gary Rydstrom","Jimmy Hayward","Bud Luckey","Glenn McQueen","Jeff Pidgeon","Bob Pauley","Bud Luckey","Andrew Stanton","Gary Rydstrom","Tim Holland","Tom Myers").toSeq)
        movies("Female Crew").toSeq should equal (List("Bonnie Arnold","Sharon Calahan","Susan Sanford","Ruth Lambert").toSeq)
    }

    test("Test Blank Movie Credits by Id") {
        val movies = new MoviesCSVReader("src/main/resources/credits.csv").readCreditData("461257")

        movies.size shouldBe 4
        movies("Male Cast").length shouldBe 0
        movies("Female Cast").length shouldBe 0
        movies("Male Crew").length shouldBe 0
        movies("Female Crew").length shouldBe 0
    }

    test("Test keyword match by title") {
        val movies = new MoviesCSVReader("src/main/resources/keywords.csv").readKeywords("romanc")

        movies.size shouldBe 10
        movies("86889") shouldBe 2
        movies("53879") shouldBe 2
        movies("340485") shouldBe 2
        movies("112973") shouldBe 2
        movies("27099") shouldBe 1
        movies("36785") shouldBe 1
        movies("52778") shouldBe 1
        movies("329865") shouldBe 1
        movies("38702") shouldBe 1
        movies("111708") shouldBe 1
    }

    test("Test keyword match by list of movie Ids") {
        val ids: List[String] = List("324786", "84346", "31589", "13398", "42984", "16061", "127372", "64686", "39256", "11645")
        val movies = new MoviesCSVReader("src/main/resources/movies_metadata.csv").matchMovieByIds(ids)

        movies.size shouldBe 10
        movies("324786") shouldBe "Hacksaw Ridge"
        movies("84346") shouldBe "The Tsunami and the Cherry Blossom"
        movies("31589") shouldBe "悪い奴ほどよく眠る"
        movies("13398") shouldBe "東京ゴッドファーザーズ"
        movies("42984") shouldBe "にっぽん昆虫記"
        movies("16061") shouldBe "東京残酷警察"
        movies("127372") shouldBe "Emperor"
        movies("64686") shouldBe "47 Ronin"
        movies("39256") shouldBe "Godzilla 1985"
        movies("11645") shouldBe "乱"
    }

}


