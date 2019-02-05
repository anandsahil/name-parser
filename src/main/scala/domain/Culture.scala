package domain

sealed abstract class Culture extends Product with Serializable

case object Austria extends Culture
case object English extends Culture
