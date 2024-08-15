import org.scalatest.funsuite.AnyFunSuite
import wordle.wordle.gameStart
import fileParse.getAnswer
import wordle.wordle.validateGuess

class WordleTest extends AnyFunSuite:

    test ("first word is aahed"){
        val result = getAnswer(1)
        val expected = "AAHED"
        assert(result == expected)
    }

    test("gameStart(69)"){
        val result = getAnswer(69)
        val expected = "ABURN"
        assert(result == "ABURN")
    }

    test("last word is possible"){
        val result = getAnswer(14855)
        assert(result == "ZYMIC")   
    }

    test("wrap around to first word"){
        val result = getAnswer(14856)
        assert(result == "AAHED")
    }

    test("error number"){
        val input = "abcd3"
        val expected = "No numbers allowed. "
        val actual = validateGuess(input).error
        assert(actual == expected)
    }

    test("error length"){
        val input = "abc"
        val expected = "Need to be 5 letters. "
        val actual = validateGuess(input).error
        assert(actual == expected)
    }

    test("error special"){
        val input = "abcd!"
        val expected = "No special characters either."
        val actual = validateGuess(input).error
        assert(actual == expected)
    }

    test("error all 3"){
        val input = "ac3@"
        val expected = "Need to be 5 letters. No numbers allowed. No special characters either."
        val actual = validateGuess(input).error
        assert(actual == expected)
    }

end WordleTest