# MovieBuff
Scala application enabling user to search for cast and crew of a movie by entering title.<br/>
The data is supplied as some comma separated (csv) files that stores all necessary details for over 45000 movies.<br/>
If a movie is found by the supplied title, application returns details such as male/female cast & crew of the movie.<br/>
In case an exact match is not found, a list of up to 10 recommendations is provided in the order of most keywords matched.

#### Title Found:
<table>
    <thead>
        <tr><th rowspan=2>Input</th><th colspan=3>Output</th></tr>
        <tr><th>Type</th><th>Count</th><th>Names</th></tr>
    </thead>
    <tbody>
        <tr><td rowspan=5>Toy Story</td></tr>
        <tr><td>Male Cast</td><td>9</td><td>Tom Hanks, Tim Allen, Don Rickles, Jim Varney, Wallace Shawn....</td></tr>
        <tr><td>Female Cast</td><td>3</td><td>Annie Potts, Laurie Metcalf, Sarah Freeman</td></tr>
        <tr><td>Male Crew</td><td>29</td><td>John Lasseter, Joss Whedon, Andrew Stanton, Joel Cohen....</td></tr>
        <tr><td>Female Crew</td><td>4</td><td>Bonnie Arnold, Sharon Calahan, Susan Sanford, Ruth Lambert</td></tr>
   </tbody>
</table>

#### Title Not Found:
<table>
    <thead>
        <tr><th rowspan=3>Input</th><th colspan=3>Output</th></tr>
        <tr><th colspan=3>Recommendations</th></tr>
        <tr><th>#</th><th>Names</th><th>Keyword matches</th></tr>
    </thead>
    <tbody>
        <tr><td rowspan=5>Boxer</td><td>1</td><td>Like It Is</td><td>2</td></tr>
        <tr></td><td>2</td><td>The Big Timer</td><td>1</td><</tr>
        <tr></td><td>3</td><td>Fat City</td><td>1</td></tr>
        <tr></td><td>...</td><td>...</td><td>...</td></tr>
        <tr></td><td>10</td>><td>Les misérables</td><td>1</td</tr>
   </tbody>
</table>

# Scala Modules
1. **App** - Entry point class for the application. Asks for user input and returns prompts with final results.
1. **MoviesReader** - Trait for defining the behavior of the application. Declares various csv parsing methods.
1. **MoviesCSVReader** - Implements the functionality of reading/parsing cvs files and performing functional logic.

# Unit Tests
Scalatest library is used which provided "Funsuite" framework for easy unit testing Scala application.
_**AppTest**_ class provides tests to prove basic underlying functionality thereby testing each single unit that reads/and parses various cvs files.
> Test cases are limited for now and more should be added to perform thorough test coverage.

# Dependencies
1. sbt: org.scala-lang.modules:scala-xml_2.11:1.0.2.jar
1. sbt: org.scala-lang:scala-library:2.11.7.jar
1. sbt: org.scala-lang:scala-reflect:2.11:7.jar
1. sbt: org.scalatest:scalatest_2.11:2.2.6.jar
1. sbt: sbt-and-plugins

# CSV Data Files
1. **movies_metadata.csv** - Provides metadata for over 450000 movies. Used to obtain movie id by looking up movie title.
1. **credits.csv** - Provides cast and crew information for all movies. Movie Id is used for lookup.
2. **keywords.csv** - Provides various associated keywords for each of the movie. All keywords associated with entered title are matched and the corresponding movie id's are obtained.
> These files are not attached in the repository due to their large size.

# Cosole Output


