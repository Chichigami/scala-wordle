package fileParse
import scala.io.Source
import scala.util.Random

//import scala.collection.concurrent.TrieMap


def normalizeRand(seed: Int, maxInt: Int): Int =
    seed.abs % maxInt

def getAnswer(seed: Int): String =
    val answerFile = Source.fromFile("src/main/resources/wordle-nyt-words-14855.txt")
    val answerList: List[String] = answerFile.getLines().toList
    answerFile.close()
    val answer: String = answerList(normalizeRand(seed, answerList.length-1)).toUpperCase()
    return answer

//parse guess x answer file one day to a trie?