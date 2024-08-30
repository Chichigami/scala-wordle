package wordle
import scala.io.StdIn.readLine
import scala.util.matching.Regex
import scala.util.Random
import ErrorHandle.Error.ErrorInterface
import GameState.Game.Game

/* 
Wordle is a web app that was made by some guy for their friend
There is an answer
You have 6 attempts at guessing the answer
There is a keyboard shown of what letters you guessed
 */
object wordle {
    def gameStart(seed: Int = Random.nextInt()): Unit =
        println("Welcome to Wordle made in Scala\n" +
                "-------------------------------")

        val currentGame = new Game(seed)
        println(currentGame.answer)
        gameCore(currentGame)

    def gameCore(thisGame: Game): Unit =
        var attemptsRemaining: Int = 6
        while attemptsRemaining != 0 //if using for loop then need to use boundaries lib to break
        do
            var currentGuess = readLine("Take a guess: ").toUpperCase().replaceAll(" ","")
            val checkedGuess = validateGuess(currentGuess)

            if checkedGuess.isValid then
                thisGame.updateGuess(currentGuess)

                if thisGame.currentGuess == thisGame.answer then
                    thisGame.printGameState()
                    println(f"${Console.CYAN_B}Congratulations, you won${Console.RESET}")
                    attemptsRemaining = 0
                else
                    
                    thisGame.printGameState()
                    attemptsRemaining -= 1
                    println(f"You have: $attemptsRemaining tries left")
                    if attemptsRemaining == 0 then
                        println(f"You lost. The answer is ${thisGame.answer}")
            else
                println(f"${Console.RED}${checkedGuess.error}${Console.RESET}")

        gameEnd(thisGame)

    def gameEnd(thisGame: Game): Unit =
        //ask if they want the defition to the word. if yes then give def, else don't do anything
        val alias = readLine("What is your alias? ")
        thisGame.name = alias
        // val response: String = readLine("Do you want the definition of the word [Y/N]? ")
        // if response == "Y" then
        //     println(thisGame.answerDefinition) //will implement in future
        thisGame.save()

    // //val guessTrie: TrieMap(char or boolean -> char or boolean) = fileParse.getGuess()
    def validateGuess(guess: String): ErrorInterface =
        var isValid: Boolean = true
        var errorString: String = ""
        val numberPattern: Regex = "[0-9]".r
        val specialPattern: Regex = "[\\^&*()!@#$%`/?.>,<\\{}\\[\\]\\-_+=|]".r //all special characters
        if guess.length != 5 then
            isValid = false
            errorString += "Need to be 5 letters. "
        if numberPattern.findFirstIn(guess).isDefined then 
            isValid = false
            errorString += "No numbers allowed. "
        if specialPattern.findFirstIn(guess).isDefined then 
            isValid = false
            errorString += "No special characters either."
        //was supposed to check if guess is inside guess list, but guess file sort of broken? 
        ErrorInterface(isValid, errorString)
}