package parser

import org.scalatest._
import org.scalatest.prop._

class FullNameParserTest extends PropSpec with PropertyChecks with Matchers with EitherValues {
  property("parse names test") {
    val names = Table(
      ("nameToParse", "expectedName"),
      // First & Last Names
      ("sahil anand", ParsedName(None, Some("sahil"), None, Some("anand"), None, None)),
      ("Gerald Böck", ParsedName(None, Some("Gerald"), None, Some("Böck"), None, None)),

      // Middle Names
      ("David William Davis", ParsedName(None, Some("David"), Some("William"), Some("Davis"), None, None)),
      ("Davis, David William", ParsedName(None, Some("David"), Some("William"), Some("Davis"), None, None)),

      // Last names with known prefixes
      ("Vincent Van Gogh", ParsedName(None, Some("Vincent"), None, Some("Van Gogh"), None, None)),
      ("Van Gogh, Vincent", ParsedName(None, Some("Vincent"), None, Some("Van Gogh"), None, None)),
      ("Lorenzo de Médici", ParsedName(None, Some("Lorenzo"), None, Some("de Médici"), None, None)),
      ("de Médici, Lorenzo", ParsedName(None, Some("Lorenzo"), None, Some("de Médici"), None, None)),
      ("Jüan de la Véña", ParsedName(None, Some("Jüan"), None, Some("de la Véña"), None, None)),
      ("de la Véña, Jüan", ParsedName(None, Some("Jüan"), None, Some("de la Véña"), None, None)),

      // Compound last names
      ("Jüan Martinez de Lorenzo y Gutierez", ParsedName(None, Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), None, None)),
      ("de Lorenzo y Gutierez, Jüan Martinez", ParsedName(None, Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), None, None)),

      // Names with known suffixes
      ("Sammy Davis, Jr.", ParsedName(None, Some("Sammy"), None, Some("Davis"), None, Some("Jr."))),
      ("Davis, Sammy, Jr.", ParsedName(None, Some("Sammy"), None, Some("Davis"), None, Some("Jr."))),
      ("John P. Doe-Ray, Jr., CLU, CFP", ParsedName(None, Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr. CLU CFP"))),
      ("Doe-Ray, John P., Jr., CLU, CFP", ParsedName(None, Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr. CLU CFP"))),

      // Names with unknown suffixes
      ("John P. Doe-Ray, Jr., CL, CF, LUTC", ParsedName(None, Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr. CL CF LUTC"))),
      ("Doe-Ray, John P., Jr., CL, CF, LUTC", ParsedName(None, Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr. CL CF LUTC"))),

      // Names with titles
      ("Dr. John P. Doe-Ray, Jr.", ParsedName(Some("Dr."), Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr."))),
      ("Dr. Doe-Ray, John P., Jr.", ParsedName(Some("Dr."), Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr."))),
      ("Doe-Ray, Dr. John P., Jr.", ParsedName(Some("Dr."), Some("John"), Some("P."), Some("Doe-Ray"), None, Some("Jr."))),
      ("Dipl.Bw. (GG) Sahil Anand", ParsedName(Some("Dipl.Bw."), Some("Sahil"), None, Some("Anand"), Some("GG"), None)),

      // Names with nick names
      ("Orenthal James \"O. J.\" Simpson", ParsedName(None, Some("Orenthal"), Some("James"), Some("Simpson"), Some("O. J."), None)),
      ("Orenthal 'O. J.' James Simpson", ParsedName(None, Some("Orenthal"), Some("James"), Some("Simpson"), Some("O. J."), None)),
      //("(O. J.) Orenthal James Simpson", ParsedName(None, Some("Orenthal"), Some("James"), Some("Simpson"), Some("O. J."), None)),
      //("Simpson, Orenthal James “O. J.”", ParsedName(None, Some("Orenthal"), Some("James"), Some("Simpson"), Some("O. J."), None)),
      ("Simpson, Orenthal ‘O. J.’ James", ParsedName(None, Some("Orenthal"), Some("James"), Some("Simpson"), Some("O. J."), None)),
      ("Simpson, [O. J.] Orenthal James", ParsedName(None, Some("Orenthal"), Some("James"), Some("Simpson"), Some("O. J."), None)),

      // Misc name orders
      ("Mr. Jüan Martinez (Martin) de Lorenzo y Gutierez Jr.", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      ("de Lorenzo y Gutierez, Mr. Jüan Martinez (Martin) Jr.", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      ("de Lorenzo y Gutierez, Mr. Jüan (Martin) Martinez Jr.", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      ("Mr. de Lorenzo y Gutierez, Jüan Martinez (Martin) Jr.", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      //("Mr. de Lorenzo y Gutierez, Jüan (Martin) Martinez Jr.", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      //("Mr. de Lorenzo y Gutierez Jr., Jüan Martinez (Martin)", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      ("Mr. de Lorenzo y Gutierez Jr., Jüan (Martin) Martinez", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      //("Mr. de Lorenzo y Gutierez, Jr. Jüan Martinez (Martin)", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      ("Mr. de Lorenzo y Gutierez, Jr. Jüan (Martin) Martinez", ParsedName(Some("Mr."), Some("Jüan"), Some("Martinez"), Some("de Lorenzo y Gutierez"), Some("Martin"), Some("Jr."))),
      ("Dale Edward Senior II, PhD", ParsedName(None, Some("Dale"), Some("Edward"), Some("Senior"), None, Some("II PhD"))),
      ("Jonathan Smith IV, PhD", ParsedName(None, Some("Jonathan"), None, Some("Smith"), None, Some("IV PhD"))),

    )

    forAll(names) { (name: String, expected: ParsedName) => {
      Parse(name).fold(t => fail(t.message), _ should be (expected))
    }}
  }

  property("should give error when empty string in provided") {
    Parse("").fold(
      _ => succeed,
      _ => fail()
    )
  }

}
