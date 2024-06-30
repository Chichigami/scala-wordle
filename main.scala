import scala.io.StdIn.readLine
import scala.io.Source
import scala.util.Random
import scala.util.matching.Regex
import scala.collection.mutable.Map

var coquetteCache: List[String] = List()
var keyboardState: Map[Char, String] = Map(
    'Q'->"White", 'W'->"White", 'E'->"White", 'R'->"White", 'T'->"White", 'Y'->"White", 'U'->"White", 'I'->"White", 'O'->"White",'P'->"White",
            'A'->"White", 'S'->"White", 'D'->"White", 'F'->"White", 'G'->"White", 'H'->"White", 'J'->"White", 'K'->"White", 'L'->"White",
                        'Z'->"White", 'X'->"White", 'C'->"White", 'V'->"White", 'B'->"White", 'N'->"White", 'M'->"White"
    )

@main def wordle() = 
    val answerList: List[String] = Source.fromFile("src/wordle-nyt-words-14855.txt").getLines().toList
    val answer: String = answerList(Random.between(0, answerList.length-1)).toUpperCase()
    println(f"ANSWER: $answer")
    println("Welcome to Wordle made in Scala\n" +
            "-------------------------------")
    var triesLeft: Int = 6
    var guessList: Map[Int, List[String]] = Map() //{triesleft: [guess, _, _, _, _, _]} _ = if respective letter is correct
    while 
        triesLeft != 0 
    do
        println(f"You have: $triesLeft tries left")
        var valid: Boolean = false
        var guess: String = ""
        while
            valid == false
        do
            guess = readLine("Take a guess\n").toUpperCase().replaceAll(" ","")
            printKeyboard()
            try
                valid = isValidGuess(guess) //break loop if valid
            catch
                case e: Exception => println(e)

        guessList(triesLeft) = saveGameState(guess, answer)
        if guess == answer then //faster than calling the checker functio
            printGameState(guessList, triesLeft)
            println("You win")
            triesLeft = 0 //breaks out of while loop
        else
            printGameState(guessList, triesLeft)
            triesLeft -= 1


def isValidGuess(guess: String): Boolean =
    var isValid: Boolean = true
    var errorString: String = ""
    val numberPattern: Regex = "[0-9]".r
    val specialPattern: Regex = "[\\^&*()!@#$%`/?.>,<\\{}\\[\\]\\-_+=|]".r
    if guess.length != 5 then 
        isValid = false
        errorString += "Need 5 letters. "
    if numberPattern.findFirstIn(guess).isDefined then 
        isValid = false
        errorString += "No numbers allowed. "
    if specialPattern.findFirstIn(guess).isDefined then 
        isValid = false
        errorString += "No special characters either."
    //if guess is in valid guess list
    if isValid == false then throw Exception(errorString) 
    return isValid

def saveGameState(guess: String, answer: String): List[String] = 
    var gameState: List[String] = List(guess)
    for i <- 0 to 4
    do
        if guess(i) == answer(i) then 
            gameState = gameState :+ "green"
            keyboardState(guess(i)) = "green"
        else if answer.contains(guess(i)) then 
            gameState = gameState :+ "yellow"
            keyboardState(guess(i)) = "yellow"
        else
            keyboardState(guess(i)) = "reset"
            gameState = gameState :+ "white"
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

def printKeyboard(): Unit =
    val row1: String = "qwertyuiop"
    val row2: String =  "asdfghjkl"
    val row3: String =   "zxcvbnm"
    println(keyboardState)