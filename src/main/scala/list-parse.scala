package fileParse
import scala.io.Source
import scala.util.Random
//import scala.collection.concurrent.TrieMap


def getAnswer(): String =
    val answerFile = Source.fromFile("src/main/resources/wordle-nyt-words-14855.txt")
    val answerList: List[String] = answerFile.getLines().toList
    answerFile.close()
    val answer: String = answerList(Random.between(0, answerList.length-1)).toUpperCase()
    return answer

//parse guess x answer file one day to a trie?