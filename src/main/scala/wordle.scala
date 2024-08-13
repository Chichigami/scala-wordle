package wordle
import scala.io.StdIn.readLine
import scala.util.matching.Regex
import scala.collection.mutable.Map
import fileParse.getAnswer
import scala.compiletime.ops.double

/* 
Wordle is a web app that was made by some guy for their friend
There is an answer
You have 6 attempts at guessing the answer
There is a keyboard shown of what letters you guessed
 */
var coquetteCache: List[String] = List()
var keyboardState: Map[Char, String] = Map(
    'Q'->"white", 'W'->"white", 'E'->"white", 'R'->"white", 'T'->"white", 'Y'->"white", 'U'->"white", 'I'->"white", 'O'->"white",'P'->"white",
            'A'->"white", 'S'->"white", 'D'->"white", 'F'->"white", 'G'->"white", 'H'->"white", 'J'->"white", 'K'->"white", 'L'->"white",
                        'Z'->"white", 'X'->"white", 'C'->"white", 'V'->"white", 'B'->"white", 'N'->"white", 'M'->"white"
    )

def gameStart(): Unit = 
    println("Welcome to Wordle made in Scala\n" +
            "-------------------------------")
    val answer = getAnswer()
    //println(answer)
    var attemptsLeft: Int = 6
    var guessList: Map[Int, List[String]] = (6 to 1 by -1).map(_ -> List.empty[String]).to(Map)
    //{K: 6 to 1: V: [guess, _, _, _, _, _]} should be faster to mutate vs add mutating
    while attemptsLeft != 0 //if for loop then need to use boundaries lib to break
    do
        println(f"You have: $attemptsLeft tries left")
        val currentGuess = getGuess()
        guessList(attemptsLeft) = saveGameState(currentGuess, answer)

        if currentGuess == answer then //faster than calling the checker function
            printGameState(guessList, attemptsLeft)
            println("Congratulations, you won")
            attemptsLeft = 0
        else
            printGameState(guessList, attemptsLeft)
            printKeyboard()
            attemptsLeft -= 1
            if attemptsLeft == 0 then
                println(f"You lost. The answer is $answer")


def getGuess(): String = 
    var valid = false
    var guess = ""
    while !valid //error handling otherwise program will throw
    do
        guess = readLine("Take a guess: ").toUpperCase().replaceAll(" ","")
        try 
            valid = validateGuess(guess) //break out of loop if the guess is valid
        catch
            case e: Exception => println(e)
    guess

//val guessTrie: TrieMap(char or boolean -> char or boolean) = fileParse.getGuess()
def validateGuess(guess: String): Boolean =
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
    isValid

def saveGameState(guess: String, answer: String): List[String] = 
    var gameState: List[String] = List(guess)
    for i <- 0 to 4 //for each letter in answer/guess
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
    gameState


def printGameState(gameState: Map[Int, List[String]], currentTry: Int): Unit =
    /*
    firstGuess
    secondGuess
    ..
    sixthGuess  
    */
    var coquetteGuess: String = "            " 
    //format: (color + letter1 + color + letter2 + color + letter3 + color + letter4 + color + letter5 + reset)
    val currentGuess: List[String] = gameState(currentTry)
    for i <- 1 to 5 //getting the rest of the list besides the guess
    do
        currentGuess(i) match
            case "green" => coquetteGuess += Console.GREEN_B + currentGuess(0)(i-1) + Console.RESET
            case "yellow" => coquetteGuess += Console.YELLOW_B + currentGuess(0)(i-1) + Console.RESET
            case "white" => coquetteGuess += Console.WHITE_B + currentGuess(0)(i-1) + Console.RESET
    coquetteCache = coquetteCache :+ coquetteGuess
    for guess <- coquetteCache do println(guess)

def printKeyboard(): Unit =
    val row1: String = "QWERTYUIOP"
    val row2: String =  "ASDFGHJKL"
    val row3: String =   "ZXCVBNM"
    var coquetteKeyboard: String = "         "

    for letter <- row1 + row2 + row3
    do
        keyboardState(letter) match
            case "green" => coquetteKeyboard += Console.GREEN_B + letter + Console.RESET //what is correct
            case "yellow" => coquetteKeyboard += Console.YELLOW_B + letter + Console.RESET //what is almost correct
            case "white" => coquetteKeyboard +=  Console.WHITE_B + letter + Console.RESET //what you haven't used
            case _ => coquetteKeyboard = coquetteKeyboard + Console.RESET + letter //what you tried and is wrong

        letter match //formatting the print string when it reach the end of the row
            case 'P' => coquetteKeyboard = f"$coquetteKeyboard\n         "
            case 'L' => coquetteKeyboard = f"$coquetteKeyboard\n          "
            case _ =>

    println(f"------KEYBOARD-STATUS-------\n$coquetteKeyboard\n----------------------------")
