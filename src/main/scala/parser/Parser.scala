package parser


import scalaz._
import Scalaz._
import dictionary._
import domain.{Culture, English}
import utils.ops._

object Parse {
  type ParsingResult = Either[ParsingFailure, ParsedName]
  val commaSymbol = ','

  def apply(fullName: String, culture: Culture = English): ParsingResult = {
    val parsePrefixes = joinNamePrefixes(Prefix(culture))
    val parseSuffixes = splitMatchingSuffixes(Suffix(culture))
    val parseTitles = splitMatchingTitles(Titles(culture))

    (for {
      fn <- fullName.isEmpty.fold(ParsingFailure("name to be parsed cannot be empty").left, fullName.right)
      nickName <- fetchNickName(fullName)
      remainingParts <- removeNickNameFromFullName(nickName, fn).split(" ").toList.right
      (np, suffixes, nc1) = parseSuffixes tupled splitNamesAndCommas(remainingParts)
      (np2, titles, nc2) = parseTitles(np, nc1)
      (np3, nc3) = if (np2.size > 1) parsePrefixes(np2, nc2) else (np2, nc2)
      (np4, nc4) = if (np3.size > 2) joinConjugationToSurroundingNames(Conjungation(culture))(np3, nc3) else (np3, nc3)
      nc5 = nc4.take(nc4.size - 1)
      firstComma = nc5.indexOf(Some(commaSymbol))
      remainingCommas = nc5.count(_.isDefined)
      (np6, nc6, extraSuffixes) = if (firstComma > 1 || remainingCommas > 1) gatherSuffixLeft(np4, nc5) else (np4, nc5, List.empty)
      (fn, mn, ln) = extractFirstNameLastNameMiddleName(np6, nc6)
      finalSuffixes = suffixes ::: extraSuffixes
      parsedName <- ParsedName(
        titles.toOptionA(_.mkString(" ")),
        fn,
        mn,
        ln,
        nickName.map(removeNickNameSymbols),
        finalSuffixes.toOptionA(_.mkString(" "))
      ).right[ParsingFailure]
    } yield parsedName).toEither
  }

  def removeNickNameFromFullName(nickName: Option[String], fullName: String): String =
    nickName.map(r => (" " + fullName + " ").replace(r, " ").trim).getOrElse(fullName)

  def removeNickNameSymbols(nickName: String): String = {

    val shredExtraChars = nickName.substring(2, nickName.length - 2)
    (shredExtraChars.last === ',').fold(shredExtraChars.slice(0, shredExtraChars.length - 1), shredExtraChars)
  }

  def fetchNickName(fullName: String): \/[ParsingFailure, Option[String]] = {
    val nickName = Matchers.nicknameRegex
      .findAllIn(s" $fullName ")
      .toList

    (nickName.size > 1).fold(
      // Error out if found two nick names
      ParsingFailure(s"More than one nick name found '$nickName', expected 1 nick name").left,
      nickName.headOption.right
    )
  }

  def adjustComma(commas: List[Option[Char]], index: Int): List[Option[Char]] = {
    if (commas.lift(index).join[Char].map(_ === ',').exists(identity)) commas.patch(index + 1, Nil, 1) else commas.patch(index, Nil, 1)
  }

  def splitNamesAndCommas(nameParts: List[String]): (List[String], List[Option[Char]]) = {
    def nameSplitResult(np: List[String], parts: (List[String], List[Option[Char]])): (List[String], List[Option[Char]]) = np match {
      case head :: tail => {
        head.lastOption.cata(r => {
          (r === ',').fold(
            // return all characters until comma
            nameSplitResult(tail, (parts._1 :+ head.slice(0, head.length - 1), parts._2 :+ Some(','))),
            nameSplitResult(tail, (parts._1 :+ head, parts._2 :+ None))
          )
        }, parts)
      }
      case _ => parts
    }

    nameSplitResult(nameParts, (List(), List(None)))
  }

  def splitMatchingSuffixes(suffixes: Suffix): (List[String], List[Option[Char]]) => (List[String], List[String], List[Option[Char]]) =
    (nameParts: List[String], nameCommas: List[Option[Char]]) => {

      def nameSplitResult(np: List[String], parts: (List[String], List[String], List[Option[Char]])): (List[String], List[String], List[Option[Char]]) = {
        np match {
          case head :: tail => {
            val lastChar = head.slice(head.length - 1, head.length)
            val headIndex = nameParts.indexOf(head)
            val potentialSuffix = (lastChar === ".").fold(
              head.slice(0, head.length - 1).toLowerCase,
              head.toLowerCase
            )

            (suffixes.list.exists(_ === potentialSuffix) || suffixes.list.exists(_ === potentialSuffix + ".")).fold(
              nameSplitResult(tail, (parts._1, head :: parts._2, adjustComma(parts._3, headIndex))),
              nameSplitResult(tail, (head :: parts._1, parts._2, parts._3))
            )
          }
          case _ => parts
        }
      }

      nameSplitResult(nameParts.reverse, (List.empty, List.empty, nameCommas))
    }

  def splitMatchingTitles(titles: Titles): (List[String], List[Option[Char]]) => (List[String], List[String], List[Option[Char]]) =
    (nameParts: List[String], nameCommas: List[Option[Char]]) => {

      def nameSplitResult(np: List[String], parts: (List[String], List[String], List[Option[Char]])): (List[String], List[String], List[Option[Char]]) = {
        np match {
          case head :: tail => {
            val lastChar = head.slice(head.length - 1, head.length)
            val headIndex = nameParts.indexOf(head)

            val potentialTitle = (lastChar === ".").fold(
              head.slice(0, head.length - 1).toLowerCase,
              head.toLowerCase
            )

            (titles.list.exists(_ === potentialTitle) || titles.list.exists(_ === potentialTitle + ".")).fold(
              nameSplitResult(tail, (parts._1, head :: parts._2, adjustComma(parts._3, headIndex))),
              nameSplitResult(tail, (head :: parts._1, parts._2, parts._3))
            )
          }
          case _ => parts
        }
      }

      nameSplitResult(nameParts.reverse, (List.empty, List.empty, nameCommas))
    }

  def joinNamePrefixes(prefixes: Prefix): (List[String], List[Option[Char]]) => (List[String], List[Option[Char]]) = (nameParts: List[String], nameCommas: List[Option[Char]]) => {
    val npRev = nameParts.reverse

    def prefixNameParts(np: List[String], accum: List[String], nc: List[Option[Char]]): (List[String], List[Option[Char]]) = np match {
      case head :: secondElem :: tail => {
        val headIndex = (accum ::: np).indexOf(secondElem)
        if (prefixes.list.exists(_ === secondElem.toLowerCase)) {
          prefixNameParts(s"$secondElem $head" :: tail, accum, nc.patch(headIndex + 1, Nil, 1))
        } else {
          prefixNameParts(secondElem :: tail, head :: accum, nc)
        }
      }
      case head :: tail => prefixNameParts(tail, head :: accum, nc)
      case _ => (accum, nc.reverse)
    }

    prefixNameParts(npRev, List(), nameCommas.reverse)
  }

  def joinConjugationToSurroundingNames(conjugations: Conjungation): (List[String], List[Option[Char]]) => (List[String], List[Option[Char]]) =
    (nameParts: List[String], nameCommas: List[Option[Char]]) => {
      val npRev = nameParts.reverse

      def prefixNameParts(np: List[String], resultAccum: List[String], nc: List[Option[Char]]): (List[String], List[Option[Char]]) = np match {
        case head :: secondElem :: thirdElem :: tail => {
          val headIndex = (resultAccum ::: np).indexOf(secondElem)
          if (conjugations.list.exists(_ === secondElem.toLowerCase)) {
            prefixNameParts(s"$thirdElem $secondElem $head" :: tail, resultAccum, nc.patch(headIndex + 1, Nil, 2))
          } else {
            prefixNameParts(secondElem :: thirdElem :: tail, head :: resultAccum, nc)
          }
        }
        case head :: tail => prefixNameParts(tail, head :: resultAccum, nc)
        case _ => (resultAccum, nc.reverse)
      }

      prefixNameParts(npRev, List(), nameCommas.reverse)
    }

  def gatherSuffixLeft(nameParts: List[String], nameCommas: List[Option[Char]]): (List[String], List[Option[Char]], List[String]) = {
    def gpl(np: List[String], accumList: List[String], nc: List[Option[Char]], suffix: List[String]): (List[String], List[Option[Char]], List[String]) = {
      if (np.size >= 2) {
        np match {
          case head :: tail => {
            val headIndex = nameParts.indexOf(head)
            if (nc.lift(headIndex).join[Char].map(_ === commaSymbol).exists(identity)) {
              gpl(tail, accumList, nc.patch(headIndex, Nil, 1), head :: suffix)
            } else {
              ((head :: tail ::: accumList).reverse, nc, suffix)
            }
          }
          case _ => (accumList.reverse, nc, suffix)
        }
      } else {
        ((np ::: accumList).reverse, nc, suffix)
      }
    }

    gpl(nameParts.reverse, List.empty, nameCommas, List.empty)
  }

  def extractFirstNameLastNameMiddleName(nameParts: List[String], nameCommas: List[Option[Char]]): (Option[String], Option[String], Option[String]) = {
    def names(np: List[String]): (Option[String], Option[String], Option[String]) = {
      if (np.size === 1) {
        (None, None, np.headOption)
      } else if (np.size === 2) {
        (np.headOption, None, np.tail.headOption)
      } else if (np.size > 2) {
        (np.take(1).headOption, Some(np.slice(1, nameParts.size - 1).mkString(" ")), np.lastOption)
      } else {
        (None, None, None)
      }
    }

    val remainingCommas = nameCommas.collect({
      case Some(a) => a
    }).size

    if (remainingCommas > 0) {
      // Remove and store all parts before first comma as last name
      if (nameCommas.exists(_ === Some(commaSymbol))) {
        val commaIndex = nameCommas.indexOf(Some(commaSymbol))
        nameParts.splitAt(commaIndex) match {
          case (l, head :: Nil) => (Some(head), None, Some(l.mkString(" ")))
          case (l, head :: tail) => (Some(head), Some(tail.mkString(" ")), Some(l.mkString(" ")))
          case (l, Nil) => (None, None, Some(l.mkString(" ")))
        }
      } else {
        names(nameParts)
      }
    } else {
      names(nameParts)
    }
  }
}
