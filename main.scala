import scala.io.StdIn.readLine
import scala.io.Source
import scala.util.Random
import scala.util.matching.Regex
import scala.collection.mutable.Map

var coquetteCache: List[String] = List()

@main def wordle() = 
    val answerList: List[String] = Source.fromFile("src/wordle-nyt-words-14855.txt").getLines().toList
    val answer: String = answerList(Random.between(0, answerList.length-1))
    println(f"ANSWER: $answer")
    println("Welcome to Wordle made in Scala\n" +
            "-------------------------------")
    var triesLeft: Int = 6
    var guessList: Map[Int, List[String]] = Map() //{triesleft: [guess, _, _, _, _, _]} _ = if respective letter is correct
    while 
        triesLeft != 0
    do
        println(f"You have: $triesLeft tries left")
        println("Take a guess")
        val guess: String = readLine().toLowerCase()
        try
            isValidGuess(guess)
        catch
            case e: Exception => triesLeft += 1; println(e)
        finally
            guessList(triesLeft) = saveGameState(guess, answer)
            if guess == answer then //faster than calling the checker function
                printGameState(guessList, triesLeft)
                println("You win")
                triesLeft = 0 //breaks out of while loop
            else
                printGameState(guessList, triesLeft)
                triesLeft -= 1

    

def isValidGuess(guess: String): Unit =
    val numberPattern: Regex = "[^a-z]".r
    if guess.length != 5 then throw Exception("Needs to be 5 characters long")
    if numberPattern.findFirstIn(guess).isDefined then throw Exception("Only alphabet allowed. No numbers or special characters")
    //check if guess is in valid guess List

def saveGameState(guess: String, answer: String): List[String] = 
    var gameState: List[String] = List(guess)
    for i <- 0 to 4
    do
        if guess(i) == answer(i) then gameState = gameState :+ "green"
        else if answer.contains(guess(i)) then gameState = gameState :+ "yellow"
        else gameState = gameState :+ "white"
    return gameState
    
def printGameState(gameState: Map[Int, List[String]], currentTry: Int): Unit =
    val green: String = Console.GREEN_B
    val yellow: String = Console.YELLOW_B
    val white: String = Console.WHITE_B
    val reset: String = Console.RESET
    var coquetteGuess: String = "" //format: (color + letter1 + color + letter2 + color + letter3 + color + letter4 + color + letter5 + reset)
    val currentGuess: List[String] = gameState(currentTry)
    for i <- 1 to 5 //getting the rest of the list besides the guess
    do
        currentGuess(i).toString match
            case "green" => coquetteGuess = coquetteGuess + green + currentGuess(0)(i-1) + reset
            case "yellow" => coquetteGuess = coquetteGuess + yellow + currentGuess(0)(i-1) + reset
            case "white" => coquetteGuess = coquetteGuess + white + currentGuess(0)(i-1) + reset
    coquetteCache = coquetteCache :+ coquetteGuess
    for guess <- coquetteCache do println(guess)