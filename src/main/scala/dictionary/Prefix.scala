package dictionary

import domain.{Culture, English}
import scalaz._, Scalaz._

final case class Prefix(list: List[String])

object Prefix {
  def apply(culture: Culture): Prefix =
    Prefix((values.get(culture) |@| values.get(English))(_ ::: _) | List.empty)

  private val values: Map[Culture, List[String]] = Map(
    English -> List(
      "a", "ab", "antune", "ap", "abu", "al", "alm", "alt", "bab",
      "bar", "bath", "bat", "beau", "beck", "ben", "berg", "bet", "bin", "bint", "birch"
      ,"bjur", "da", "dahl", "dal", "de", "degli", "dele", "del",
      "della", "der", "di", "dos", "du", "e", "ek", "el", "escob", "esch", "fleisch",
      "fitz", "fors", "gott", "griff", "haj", "haug", "holm", "ibn", "kauf", "kil",
      "koop", "kvarn", "la", "le", "lind", "lönn", "lund", "mac", "mhic", "mic", "mir",
      "na", "naka", "neder", "nic", "ni", "nin", "nord", "norr", "ny", "o", "ua", "ui\"",
      "öfver", "ost", "över", "öz", "papa", "pour", "quarn", "skog", "skoog", "sten",
      "stor", "ström", "söder", "ter", "ter", "tre", "türk", "van", "väst", "väster",
      "vest", "von"
    )
  )
}