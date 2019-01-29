package parser

object Matchers {
  val nicknameRegex = "\\s(?:[‘’']([^‘’']+)[‘’']|[“”\"]([^“”\"]+)[“”\"]|\\[([^\\]]+)\\]|\\(([^\\)]+)\\)),?\\s".r
}
