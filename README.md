
[![Build Status](https://travis-ci.com/anandsahil/name-parser.svg?token=pZ1VuAvYBcjuYS7wzWWs&branch=master)](https://travis-ci.com/anandsahil/name-parser)

# Description

Name parser is a small library initiated to parse full names in multiple formats. The parser accepts a free form string containing full name 
in formats. The algo analyses and detects the format of the name in (if possible) parses the name with return type `Either[ParsingFailure, ParsedName]`

The parser tries to divide names into following categories.
 
 1. title (string): title(s) (e.g. "Ms." or "Dr.")
 2. first (string): first name or initial
 3. middle (string): middle name(s) or initial(s)
 4. last (string): last name or initial
 5. nick (string): nickname(s)
 5. suffix (string): suffix(es) (e.g. "Jr.", "II", or "Esq.")
 
 During the parser implementation i stumbled upon different cultures having specific name pattern's so currently the parser has two culture
 
 1. English (Default)
 2. Austria
 
 English is always taken into consideration and other cultures currently Austria can be passed as parameter.
 
 # Getting Started
 
 # Usage
 
 ```scala
import domain.English
import parser.NameParser

val fullName: String = "Dr. John P. Doe-Ray, Jr."
val parsedName: ParsingResult = NameParser.parse(fullName, English)
// As second parameter to funtion is default to English we can skip to pass it explicitly

val parsedName: ParsingResult = NameParser.parse(fullName)
// Right(ParsedName(Some(Dr.),Some(John),Some(P.),Some(Doe-Ray),None,Some(Jr.)))

val fullNameEmpty: String = ""
val parsedEmptyName: ParsingResult = NameParser.parse(fullName, English)
// Left(parser.ParsingFailure: name to be parsed cannot be empty)
```

# Limitations

1. Cultures and the dictionary are limited but still covers majority of names.
2. Fixing the case of names in not implemented yet.

# Reporting Bugs
If you find a name this function does not parse correctly, or any other bug, please report it here: https://github.com/anandsahil/name-parser/issues

# Credits and precursors
1. Based on the https://github.com/dschnelldavis/parse-full-name by David Schnell-Davis.
2. Ported to Scala by Sahil Anand.
3. Also took inspiration from Josh Fraser's PHP-Name-Parser: https://github.com/joshfraser/PHP-Name-Parser
4. Also took inspiration from https://nameparser.readthedocs.io/en/latest/index.html

Thanks to the developers for sharing the work.œ

More references can be looked up on 
1. https://en.wikipedia.org/wiki/Title
2. https://en.wikipedia.org/wiki/Wikipedia:Naming_conventions_(people)

Released under MIT License             
