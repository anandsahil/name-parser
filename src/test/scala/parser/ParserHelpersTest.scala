package parser
import org.scalatest._

class ParserHelpersTest extends FunSuite with Matchers {

  test("split name and comma helper test") {
    val name = "sahil anand"
    val nameWithComma = "Sammy Davis, Jr."

    Parse.splitNamesAndCommas(name.split(" ").toList) should be(
      (List("sahil", "anand"), List(None, None, None)))

    Parse.splitNamesAndCommas(nameWithComma.split(" ").toList) should be(
      (List("Sammy", "Davis", "Jr."), List(None, None, Some(','), None))
    )
  }

  test("split matching suffixes helper test") {
    val nameWithComma = "Sammy Davis, Jr."
    val namesParsed = Parse.splitNamesAndCommas(nameWithComma.split(" ").toList)
    namesParsed should be(
      (List("Sammy", "Davis", "Jr."), List(None, None, Some(','), None))
    )


    val (np, suffixes, nc) = Parse.splitMatchingSuffixes _ tupled namesParsed

    np should be(List("Sammy", "Davis"))
    suffixes should be (List("Jr."))
    nc should be (List(None, None, Some(',')))
  }

  test("split name and comma parts with suffix helper test") {
    val nameWithComma = "Sammy Davis, Jr."

    Parse.splitMatchingSuffixes _ tupled Parse.splitNamesAndCommas(nameWithComma.split(" ").toList) should be(
      (List("Sammy", "Davis"), List("Jr."), List(None, None, Some(',')))
    )
  }

  test ("conjugation names parsing helper test") {
    val namesParsed = Parse.splitNamesAndCommas("Jüan Martinez de Lorenzo y Gutierez".split(" ").toList)
    val prefixedName = Parse.joinNamePrefixes _ tupled namesParsed

    prefixedName should be (
      List("Jüan", "Martinez", "de Lorenzo", "y", "Gutierez"), List.fill(6)(None)
    )

    Parse.joinConjugationToSurroundingNames _ tupled prefixedName should be (List("Jüan", "Martinez", "de Lorenzo y Gutierez"), List.fill(4)(None))

    Parse.joinConjugationToSurroundingNames _ tupled (
      (List("Jüan", "Martinez", "de Lorenzo", "y", "Gutierez", "y", "Gutierez"), List.fill(8)(None))
      ) should be (List("Jüan", "Martinez", "de Lorenzo y Gutierez y Gutierez"), List.fill(4)(None))

  }

  test ("conjugation names reverse parsing helper test") {
    val namesParsed = Parse.splitNamesAndCommas("de Lorenzo y Lorenzo Gutierez, Jüan Martinez".split(" ").toList)

    val prefixedName = Parse.joinNamePrefixes _ tupled namesParsed

    prefixedName should be (
      List("de Lorenzo", "y", "Lorenzo", "Gutierez", "Jüan", "Martinez"), List(None, None, None, None, Some(','), None, None)
    )

    Parse.joinConjugationToSurroundingNames _ tupled prefixedName should be (List("de Lorenzo y Lorenzo", "Gutierez", "Jüan", "Martinez"), List.fill(5)(None))
  }

  test ("unknown suffix with wong commas placement helper test") {
    val nameWithComma = "John P. Doe-Ray, Jr., CLU, CFP LUTC"

    val (np, suffixes, nc) = Parse.splitMatchingSuffixes _ tupled Parse.splitNamesAndCommas(nameWithComma.split(" ").toList)
    (np, suffixes, nc) should be(
      (List("John", "P.", "Doe-Ray", "CLU", "CFP", "LUTC"), List("Jr."), List(None, None, None, Some(','), Some(','), None, None))
    )

    val (np1, nc1, sl) =  Parse.gatherSuffixLeft(np, nc)

    np1 should be(List("John", "P.", "Doe-Ray", "CLU", "CFP", "LUTC"))
    nc1 should be(List(None, None, None, Some(','), Some(','), None, None))
    sl should be (List.empty)

  }

  test ("unknown suffix with right commas placement helper test") {
    val nameWithComma = "John P. Doe-Ray, Jr., CLU, CFP, LUTC"

    val (np, suffixes, nc) = Parse.splitMatchingSuffixes _ tupled Parse.splitNamesAndCommas(nameWithComma.split(" ").toList)
    (np, suffixes, nc) should be(
      (List("John", "P.", "Doe-Ray", "CLU", "CFP", "LUTC"), List("Jr."), List(None, None, None, Some(','), Some(','), Some(','), None))
    )

    val (np1, nc1, sl) =  Parse.gatherSuffixLeft(np, nc)

    np1 should be(List("John", "P.", "Doe-Ray"))
    nc1 should be(List(None, None, None, None))
    sl should be (List("CLU", "CFP", "LUTC"))
  }
}
