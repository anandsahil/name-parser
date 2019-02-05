package dictionary

import domain.{Austria, Culture, English}
import scalaz._, Scalaz._

final case class Conjungation(list: List[String])

object Conjungation {
  def apply(culture: Culture): Conjungation =
    Conjungation((values.get(culture) |@| values.get(English))(_ ::: _) | List.empty)

  private val values: Map[Culture, List[String]] = Map(
    English -> List("&","and","et","e","of","the","y"),
    Austria -> List("und")
  )
}
