package wordle
import scala.io.StdIn.readLine
import scala.util.matching.Regex
import scala.collection.mutable.Map
import fileParse.getAnswer

var coquetteCache: List[String] = List()
var keyboardState: scala.collection.mutable.Map[Char, String] = Map(
    'Q'->"white", 'W'->"white", 'E'->"white", 'R'->"white", 'T'->"white", 'Y'->"white", 'U'->"white", 'I'->"white", 'O'->"white",'P'->"white",
            'A'->"white", 'S'->"white", 'D'->"white", 'F'->"white", 'G'->"white", 'H'->"white", 'J'->"white", 'K'->"white", 'L'->"white",
                        'Z'->"white", 'X'->"white", 'C'->"white", 'V'->"white", 'B'->"white", 'N'->"white", 'M'->"white"
    )
//val possibleGuesses: List[String] = parse()

def gameStart() = 
    val answer = getAnswer()
    //println(answer)
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
            guess = readLine("Take a guess: ").toUpperCase().replaceAll(" ","")
            try
                valid = isValidGuess(guess) //break loop if valid
            catch
                case e: Exception => println(e)

        guessList(triesLeft) = saveGameState(guess, answer)
        if guess == answer then //faster than calling the checker function
            printGameState(guessList, triesLeft)
            println("Congratulations, you won")
            triesLeft = 0 //breaks out of while loop
        else
            printGameState(guessList, triesLeft)
            printKeyboard()
            triesLeft -= 1
            if triesLeft == 0 then
                println(f"You lost. The answer is $answer")


def isValidGuess(guess: String): Boolean =
    var isValid: Boolean = true
    var errorString: String = ""
    val numberPattern: Regex = "[0-9]".r
    val specialPattern: Regex = "[\\^&*()!@#$%`/?.>,<\\{}\\[\\]\\-_+=|]".r //all special characters
    if guess.length != 5 then 
        isValid = false
        errorString += "Need 5 letters. "
    if numberPattern.findFirstIn(guess).isDefined then 
        isValid = false
        errorString += "No numbers allowed. "
    if specialPattern.findFirstIn(guess).isDefined then 
        isValid = false
        errorString += "No special characters either."
    //was supposed to check if guess is inside guess list, but guess file sort of broken? 
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
            if keyboardState(guess(i)) != "green" then
                keyboardState(guess(i)) = "yellow"
        else
            gameState = gameState :+ "white"
            keyboardState(guess(i)) = "blank"
    return gameState
    
def printGameState(gameState: Map[Int, List[String]], currentTry: Int): Unit =
    val green: String = Console.GREEN_B
    val yellow: String = Console.YELLOW_B
    val white: String = Console.WHITE_B
    val reset: String = Console.RESET
    var coquetteGuess: String = "            " //format: (color + letter1 + color + letter2 + color + letter3 + color + letter4 + color + letter5 + reset)
    val currentGuess: List[String] = gameState(currentTry)
    for i <- 1 to 5 //getting the rest of the list besides the guess
    do
        currentGuess(i) match
            case "green" => coquetteGuess = coquetteGuess + green + currentGuess(0)(i-1) + reset
            case "yellow" => coquetteGuess = coquetteGuess + yellow + currentGuess(0)(i-1) + reset
            case "white" => coquetteGuess = coquetteGuess + white + currentGuess(0)(i-1) + reset
    coquetteCache = coquetteCache :+ coquetteGuess
    for guess <- coquetteCache do println(guess)

def printKeyboard(): Unit =
    val row1: String = "QWERTYUIOP"
    val row2: String =  "ASDFGHJKL"
    val row3: String =   "ZXCVBNM"
    val green: String = Console.GREEN_B
    val yellow: String = Console.YELLOW_B
    val white: String = Console.WHITE_B
    val reset: String = Console.RESET
    var coquetteKeyboard: String = "         "

    for letter <- row1 + row2 + row3
    do
        keyboardState(letter) match
            case "green" => coquetteKeyboard = coquetteKeyboard + green + letter + reset //what is correct
            case "yellow" => coquetteKeyboard = coquetteKeyboard + yellow + letter + reset //what is almost correct
            case "white" => coquetteKeyboard = coquetteKeyboard + white + letter + reset //what you haven't used
            case _ => coquetteKeyboard = coquetteKeyboard + reset + letter //what you tried and is wrong

        letter match
            case 'P' => coquetteKeyboard = f"$coquetteKeyboard\n         "
            case 'L' => coquetteKeyboard = f"$coquetteKeyboard\n          "
            case _ =>
    println(f"------KEYBOARD-STATUS-------\n$coquetteKeyboard\n----------------------------")
