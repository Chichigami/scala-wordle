package GameState
import fileParse.getAnswer
import upickle.default.* 
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer

object Game {
    class Game(val seed: Int):
        val answer: String = getAnswer(seed)
        //lazy val answerDefinition: String = f"$answer: some def here"   //lazy val wordDefinition: String = getDefinition(answer)
        var name: String = ""

        var currentAttempt: Int = 0
        var currentGuess: String = ""
        var allGuesses: List[String] = List() //if i did map, it might insert into dupes into same keyvalue and i dont want that

        var prettiedGuessCache: String = ""
        var keyboardCache: Map[Char, String] = Map(
            'Q'->"white", 'W'->"white", 'E'->"white", 'R'->"white", 'T'->"white", 'Y'->"white", 'U'->"white", 'I'->"white", 'O'->"white",'P'->"white",
                    'A'->"white", 'S'->"white", 'D'->"white", 'F'->"white", 'G'->"white", 'H'->"white", 'J'->"white", 'K'->"white", 'L'->"white",
                                'Z'->"white", 'X'->"white", 'C'->"white", 'V'->"white", 'B'->"white", 'N'->"white", 'M'->"white"
            )

        def updateGuess(validatedGuess: String): Unit = 
            currentGuess = validatedGuess
            allGuesses = allGuesses :+ validatedGuess
            currentAttempt += 1
            
        def compareGuessToAnswer(): ListBuffer[String] =
            var tempPretty: ListBuffer[String] = ListBuffer()
            for i <- 0 to 4 //for each letter in answer/guess
            do
                if answer.contains(currentGuess(i)) then
                    if currentGuess(i) == answer(i) then
                        tempPretty += "green"
                        keyboardCache(currentGuess(i)) = "green"
                    else
                        tempPretty += "yellow"
                        keyboardCache(currentGuess(i)) = "yellow"
                else
                    tempPretty += "white"
                    keyboardCache(currentGuess(i)) = "blank"
            tempPretty
            
        def guessesToString(temp: ListBuffer[String]): String =
            prettiedGuessCache += "            "
            for i <- 0 to 4
            do
                temp(i) match
                    case "green" => prettiedGuessCache += f"${Console.GREEN_B}${currentGuess(i)}${Console.RESET}"
                    case "yellow" => prettiedGuessCache += f"${Console.YELLOW_B}${currentGuess(i)}${Console.RESET}"
                    case "white" => prettiedGuessCache += f"${Console.WHITE_B}${currentGuess(i)}${Console.RESET}"
                    case _ => throw Error("Guess cannot be pretty")
            prettiedGuessCache += "\n"
            prettiedGuessCache
        
        def keyboardCacheToString(): String =
            var coquetteKeyboard: String = "         "
            for letter <- "QWERTYUIOPASDFGHJKLZXCVBNM"
            do
                keyboardCache(letter) match
                    case "green" => coquetteKeyboard += f"${Console.GREEN_B}$letter${Console.RESET}" //what is correct
                    case "yellow" => coquetteKeyboard += f"${Console.YELLOW_B}$letter${Console.RESET}" //what is almost correct
                    case "white" => coquetteKeyboard +=  f"${Console.WHITE_B}$letter${Console.RESET}" //what you haven't used
                    case _ => coquetteKeyboard += Console.RESET + letter //what you tried and is wrong
                letter match //formatting the print string when it reach the end of the row
                    case 'P' => coquetteKeyboard = f"$coquetteKeyboard\n         "
                    case 'L' => coquetteKeyboard = f"$coquetteKeyboard\n          "
                    case _ => 
            coquetteKeyboard

        def printGameState(): Unit = 
            print("\u001b[2J\u001b[H" + //clears terminal
              f"${guessesToString(compareGuessToAnswer())}" +
              "------KEYBOARD STATUS-------\n" +
              f"${keyboardCacheToString()}" +
              "\n----------------------------\n")


        def save(): ujson.Value =
            val gameSave: ujson.Value = ujson.Obj(
                "seed" -> upickle.default.write(seed),
                "answer" -> upickle.default.write(answer),
                "attempts taken" -> upickle.default.write(currentAttempt),
                "all guesses" -> upickle.default.write(allGuesses),
                "keyboard state" -> upickle.default.write(keyboardCache)
            )
            gameSave

        override def toString(): String =
            save().toString
        //lazy val definition
    end Game
}