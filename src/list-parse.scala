package guessParse
import scala.io.Source
import scala.collection.concurrent.TrieMap

def parse(): List[String] =
    val guessFile = Source.fromFile("src/wordle-nyt-allowed-guesses-update-12546.txt") //open file
    var guessList: List[String] = guessFile.getLines().toList
    guessFile.close()
    return guessList
