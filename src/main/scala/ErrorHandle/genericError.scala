package ErrorHandle

object Error {
    case class ErrorInterface[A, B] (output: A, error: B)
}