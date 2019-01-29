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
      "mr", "mrs", "ms", "miss", "dr", "herr", "monsieur", "hr", "frau",
      "a v m", "admiraal", "admiral", "air cdre", "air commodore", "air marshal",
      "air vice marshal", "alderman", "alhaji", "ambassador", "baron", "barones",
      "brig", "brig gen", "brig general", "brigadier", "brigadier general",
      "brother", "canon", "capt", "captain", "cardinal", "cdr", "chief", "cik", "cmdr",
      "coach", "col", "col dr", "colonel", "commandant", "commander", "commissioner",
      "commodore", "comte", "comtessa", "congressman", "conseiller", "consul",
      "conte", "contessa", "corporal", "councillor", "count", "countess",
      "crown prince", "crown princess", "dame", "datin", "dato", "datuk",
      "datuk seri", "deacon", "deaconess", "dean", "dhr", "dipl", "dipl ing", "doctor",
      "dott", "dott sa", "dr", "dr ing", "dra", "drs", "embajador", "embajadora", "en",
      "encik", "eng", "eur ing", "exma sra", "exmo sr", "f o", "father",
      "first lieutient", "first officer", "flt lieut", "flying officer", "fr",
      "frau", "fraulein", "fru", "gen", "generaal", "general", "governor", "graaf",
      "gravin", "group captain", "grp capt", "h e dr", "h h", "h m", "h r h", "hajah",
      "haji", "hajim", "her highness", "her majesty", "herr", "high chief",
      "his highness", "his holiness", "his majesty", "hon", "hr", "hra", "ing", "ir",
      "jonkheer", "judge", "justice", "khun ying", "kolonel", "lady", "lcda", "lic",
      "lieut", "lieut cdr", "lieut col", "lieut gen", "lord", "m", "m l", "m r",
      "madame", "mademoiselle", "maj gen", "major", "master", "mevrouw", "miss",
      "mlle", "mme", "monsieur", "monsignor", "mr", "mrs", "ms", "mstr", "nti", "pastor",
      "president", "prince", "princess", "princesse", "prinses", "prof", "prof dr",
      "prof sir", "professor", "puan", "puan sri", "rabbi", "rear admiral", "rev",
      "rev canon", "rev dr", "rev mother", "reverend", "rva", "senator", "sergeant",
      "sheikh", "sheikha", "sig", "sig na", "sig ra", "sir", "sister", "sqn ldr", "sr",
      "sr d", "sra", "srta", "sultan", "tan sri", "tan sri dato", "tengku", "teuku",
      "than puying", "the hon dr", "the hon justice", "the hon miss", "the hon mr",
      "the hon mrs", "the hon ms", "the hon sir", "the very rev", "toh puan", "tun",
      "vice admiral", "viscount", "viscountess", "wg cdr", "ind", "misc", "mx",
    ),
    Austria -> List("dipl.bw")
  )
}