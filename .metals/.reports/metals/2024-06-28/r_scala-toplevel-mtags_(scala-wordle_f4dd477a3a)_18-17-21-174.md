error id: file://<WORKSPACE>/main.scala:[1263..1263) in Input.VirtualFile("file://<WORKSPACE>/main.scala", "import scala.io.StdIn.readLine
import scala.io.Source
import scala.util.Random
import scala.util.matching.Regex

@main def wordle() = 
    val answerList: List[String] = Source.fromFile("src/wordle-nyt-words-14855.txt").getLines().toList
    val answer: String = answerList(Random.between(0, answerList.length-1))
    println(answer)
    println("Welcome to Wordle made in Scala\n" +
            "-------------------------------")
    var triesLeft: Int = 6
    while 
        triesLeft > 0
    do
        println(f"You have: $triesLeft tries left")
        println("Take a guess")
        val guess: String = readLine().toLowerCase()
        try
            isValidGuess(guess)
        catch
            case e: Exception => println(e)
        finally
            if guess == answer then
                println("You win")
                triesLeft = 0 else //to break out of the while loop b/c scala doesn't have breaks without packages
                //check guess and
                triesLeft -= 1

def isValidGuess(guess: String): Unit =
    val numberPattern: Regex = "[0-9]".r
    if guess.length != 5 then throw Exception("Needs to be 5 characters long")
    if numberPattern.findFirstIn(guess).isDefined then throw Exception("No numbers allowed")

def ")
file://<WORKSPACE>/main.scala
file://<WORKSPACE>/main.scala:35: error: expected identifier; obtained eof
def 
    ^
#### Short summary: 

expected identifier; obtained eof