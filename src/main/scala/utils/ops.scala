package utils

object ops {
  implicit class ListOps[B](v: List[B]) {
    def toOptionA[A](onNel: List[B] => A): Option[A] = v match {
      case Nil => None
      case nel => Some(onNel(nel))
    }
  }
}
