package fileParse
import scala.io.Source
import scala.collection.concurrent.TrieMap
import scala.util.Random


def getAnswer(): String =
    val answerFile = Source.fromFile("src/wordle-nyt-words-14855.txt")
    val answerList: List[String] = answerFile.getLines().toList
    answerFile.close()
    val answer: String = answerList(Random.between(0, answerList.length-1)).toUpperCase()
    return answer