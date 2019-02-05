package dictionary

import domain.{Culture, English}
import scalaz._, Scalaz._

final case class Prefix(list: List[String])

object Prefix {
  def apply(culture: Culture): Prefix =
    Prefix((values.get(culture) |@| values.get(English)) (_ ::: _) | List.empty)

  private val values: Map[Culture, List[String]] = Map(
    English -> List(
      "ab", "bar", "bin", "da", "dal", "de", "de la", "del", "della", "der",
      "di", "du", "ibn", "l'", "la", "le", "san", "st", "st.", "ste", "ter", "van",
      "van de", "van der", "van den", "vel", "ver", "vere", "von"
    )
  )
}