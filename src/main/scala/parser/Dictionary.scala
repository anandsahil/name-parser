package parser

import scala.util.matching.Regex

object Dictionary {
  val nicknameRegex = "\\s(?:[‘’']([^‘’']+)[‘’']|[\"\"\"]([^“”\"]+)[“”\"]|\\[([^\\]]+)\\]|\\(([^\\)]+)\\)),?\\s".r

  val suffixList = List("esq", "esquire", "jr", "jnr", "sr", "snr", "2", "i", "ii", "iii", "iv",
    "v", "clu", "chfc", "cfp", "md", "ll.m.", "m.d.", "d.o.", "d.c.",
    "p.c.", "1st", "2nd", "3rd", "4th", "5th",

    // Professional suffixes from https://github.com/joshfraser/PHP-Name-Parser/blob/master/parser.php
    "ao", "b.a.", "m.sc", "bcompt", "phd","apr","rph","pe", "ma","dmd","cme", "bsc", "bsc", "bsc(hons)", "ph.d.", "beng", "m.b.a.", "mba", "faicd", "cm", "obc", "m.b.", "chb", "frcp", "frsc",
    "freng", "meng", "msc", "j.d.", "jd", "bgdipbus", "dip", "dipl.phys","m.h.sc.", "mpa", "b.comm", "b.eng", "b.acc", "fsa", "pgdm", "fcpa", "rn", "r.n.", "msn",
    "pca", "pccrm","pcfp","pcgd","pchr","pcm","pcps","pcpm","pcscm","pcsm","pcmm","pctc","aca", "fca","acma", "fcma","aaia", "faia","ccc","mipa","fipa","cia","cfe","cisa","cfap",
    "qc", "q.c.", "m.tech", "cta", "c.i.m.a.", "b.ec",
    "cfia","iccp","cps","cap-om","capta","tnaoap","afa","ava","asa","caia","cba","cva","icvs","ciia","cmu","pfm","prm","cfp","cwm","ccp","ea","ccmt","cgap","cdfm","cfo","cgfm","cgat","cgfo","cmfo","cpfo","cpfa",
    "bmd","biet","p.eng","pe", "mbbs", "mb", "bch", "bao", "bmbs", "mbbchir", "mbchba","mphil","ll.d", "lld", "d.lit","dea", "dess", "dclinpsy", "dsc", "mres", "m.res", "psy.d", "pharm.d",
    "ba(admin)", "bacc","bacom","badmin","bae","baecon","ba(ed)","ba(fs)","bagr","bah","bai","bai(elect)","bai(mech)","balaw","bao","bappsc","barch","barchsc","barelst","basc","basoc", "dds", "d.d.s.",
    "bass","batheol","bba","bbls", "bbs","bbus","bchem","bcj","bcl","bcld(socsc)","bclinsci","bcom","bcombst","bcommedcommdev","bcomp","bcomsc","bcoun","bd","bdes","be","becon","becon&fin", "m.p.p.m.", "mppm",
    "beconsci", "bed","beng","bes","beng(tech)","bfa","bfin","bfls","bfst","bh","bhealthsc","bhsc","bhy","bjur","bl","ble","blegsc","blib","bling","blitt","blittcelt","bls","bmedsc","bmet",
    "bmid", "bmin","bms","bmsc","bmsc","bms","bmus","bmused","bmusperf","bn", "bns","bnurs","boptom","bpa","bpharm", "bphil", "ttc", "dip", "tchg", "bed", "med","acib", "fcim", "fcis", "fcs", "fcs",
    "bachelor", "o.c.", "jp", "c.eng", "c.p.a.", "b.b.s.", "mbe", "gbe", "kbe", "dbe", "cbe", "obe", "mrics",  "d.p.s.k.", "d.p.p.j.", "dpsk", "dppj", "b.b.a.", "gbs", "migem", "m.i.g.e.m.", "fcis",
    "bphil(ed)", "bphys","bphysio","bpl","bradiog","bsc", "b.sc", "bscagr","bsc(dairy)","bsc(domsc)","bscec","bscecon","bsc(econ)","bsc(eng)","bscfor","bsc(healthsc)","bsc(hort)", "bba", "b.b.a.",
    "bsc(mcrm)", "bsc(med)","bsc(mid)","bsc(min)","bsc(psych)", "bsc(tech)","bsd", "bsocsc","bss","bstsu","btchg","btcp","btech","bteched","bth","btheol","bts","edb","littb","llb","ma","musb","scbtech",
    "ceng", "fca", "cfa", "cfa", "c.f.a.", "llb", "ll.b", "llm", "ll.m", "ca(sa)", "c.a.", "ca","cpa",  "solicitor",  "dms", "fiwo", "cenv", "mice", "miwem", "b.com", "bcom", "bacc", "ba", "bec", "mec", "hdip", "b.bus.", "e.s.c.p."
  )

  val prefixList = List(
    "a", "ab", "antune", "ap", "abu", "al", "alm", "alt", "bab", "bäck",
    "bar", "bath", "bat", "beau", "beck", "ben", "berg", "bet", "bin", "bint", "birch",
    "björk", "björn", "bjur", "da", "dahl", "dal", "de", "degli", "dele", "del",
    "della", "der", "di", "dos", "du", "e", "ek", "el", "escob", "esch", "fleisch",
    "fitz", "fors", "gott", "griff", "haj", "haug", "holm", "ibn", "kauf", "kil",
    "koop", "kvarn", "la", "le", "lind", "lönn", "lund", "mac", "mhic", "mic", "mir",
    "na", "naka", "neder", "nic", "ni", "nin", "nord", "norr", "ny", "o", "ua", "ui\"",
    "öfver", "ost", "över", "öz", "papa", "pour", "quarn", "skog", "skoog", "sten",
    "stor", "ström", "söder", "ter", "ter", "tre", "türk", "van", "väst", "väster",
    "vest", "von"
  )

  val conjunctionList = List("&","and","et","e","of","the","und","y")
}
