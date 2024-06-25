import scala.io.StdIn.readLine

@main def wordle() = 
    println("Welcome to Wordle made in Scala")
    val answer: String = "abcde"
    val guess: String = readLine()