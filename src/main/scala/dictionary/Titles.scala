package dictionary

import dictionary.Suffix.values
import domain.{Austria, Culture, English}
import scalaz._
import Scalaz._

final case class Titles(list: List[String])

object Titles {

  def apply(culture: Culture): Titles = Titles(
    (values.get(culture) |@| values.get(English))(_ ::: _) | List.empty
  )

  private val values: Map[Culture, List[String]] = Map(
    English -> List(
      // Default Titles
      "mr", "mrs", "ms", "miss", "mx", "master", "maid", "madam", "madame", "aunt", "auntie", "uncle", "m", "m l", "m r",
      // Legislative and executive titles
      "hon", "the hon dr", "the hon justice", "the hon miss", "the hon mr","the hon mrs", "the hon ms", "the hon sir",
      "rt", "mp", "senator", "speaker", "president", "councillor", "alderman", "admiral", "delegate", "mayor",
      "governor", "ambassador", "secretary", "cardinal secretary of state", "foreign secretary", "general secretary", "secretary of state",
      "her highness", "her majesty", "high chief", "his highness", "his holiness", "his majesty", "sir",
      // Judicial titles
      "advocate", "advocate general", "ag", "attorney", "judge", "justice", "magistrate",
      // Priests titles
      "bishop", "pope", "pastor",
      //Academic titles
      "dr", "monsieur", "hr", "prof", "prof dr", "prof sir", "professor", "diphe", "certhe",
      // Other
      "a v m", "admiraal", "air cdre", "air commodore", "air marshal",
      "air vice marshal", "alhaji", "ambassador", "baron", "barones",
      "brig", "brig gen", "brig general", "brigadier", "brigadier general",
      "brother", "canon", "capt", "captain", "cardinal", "cdr", "chief", "cik", "cmdr",
      "coach", "col", "col dr", "colonel", "commandant", "commander", "commissioner",
      "commodore", "comte", "comtessa", "congressman", "conseiller", "consul",
      "conte", "contessa", "corporal", "count", "countess",
      "crown prince", "crown princess", "dame", "datin", "dato", "datuk",
      "datuk seri", "deacon", "deaconess", "dean", "dhr", "dipl", "dipl ing", "doctor",
      "dott", "dott sa", "dr ing", "dra", "drs", "embajador", "embajadora", "en",
      "encik", "eng", "eur ing", "exma sra", "exmo sr", "f o", "father",
      "first lieutient", "first officer", "flt lieut", "flying officer", "fr",
      "frau", "fraulein", "fru", "gen", "generaal", "general", "governor", "graaf",
      "gravin", "group captain", "grp capt", "h e dr", "h h", "h m", "h r h", "hajah",
      "haji", "hajim" , "hr", "hra", "ing", "ir",
      "jonkheer", "khun ying", "kolonel", "lady", "lcda", "lic",
      "lieut", "lieut cdr", "lieut col", "lieut gen" ,
      "mademoiselle", "maj gen", "major", "master", "mevrouw", "miss",
      "mlle", "mme", "monsieur", "monsignor", "mr", "mrs", "ms", "mstr", "nti", "pastor",
      "prince", "princess", "princesse", "prinses", "puan", "puan sri", "rabbi", "rear admiral", "rev",
      "rev canon", "rev dr", "rev mother", "reverend", "rva" , "sergeant",
      "sheikh", "sheikha", "sig", "sig na", "sig ra", "sister", "sqn ldr", "sr",
      "sr d", "sra", "srta", "sultan", "tan sri", "tan sri dato", "tengku", "teuku",
      "than puying", "the very rev", "toh puan", "tun",
      "vice admiral", "viscount", "viscountess", "wg cdr", "ind", "misc", "mx","leutnant"
    ),
    Austria -> List(
      // Default Titles
      "herr", "frau", "fräulein", "dame", "herren", "frauen", "fräulein", "damen", "majestät",
      //Academics Titles
      "professor", "pd", "doktor", "magister", "mag",
      //Professions
      "meister", "ing", "dipl", "dipl.-ing.", "diplom-ingenieur", "bachelor", "med", "dent"

    )
  )
}
