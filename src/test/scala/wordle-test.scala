import org.scalatest.funsuite.AnyFunSuite
import wordle.wordle.gameStart
import fileParse.getAnswer

class WordleTest extends AnyFunSuite:

    test ("first word is aahed"){
        val result = getAnswer(1)
        assert(result == "AAHED")
    }

    test("gameStart(69)"){
        val result = getAnswer(69)
        assert(result == "ABURN")
    }

    test("last word is possible"){
        val result = getAnswer(14855)
        assert(result == "ZYMIC")   
    }
end WordleTest