package parser

import scala.util.control.NoStackTrace
import scalaz._, Scalaz._


final case class ParsedName(
                             title: Option[String],
                             firstName: Option[String],
                             middleName: Option[String],
                             lastName: Option[String],
                             nickName: Option[String],
                             suffix: Option[String]
                           ) {
  def toFullNameString: String = {
    List(
      title,
      firstName,
      middleName,
      lastName,
      nickName,
      suffix
    ).collect({
      case Some(a) => a
    }).mkString(" ")
  }
}






final case class ParsingFailure(message: String) extends Exception(message) with NoStackTrace
