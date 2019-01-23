name := "name-parser"

version := "0.1"

organization in ThisBuild := "org.anandsahil.net"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.11.8", "2.12.8")


val scalaTestVersion = "3.0.5"
val scalacheckVersion = "1.14.0"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.27"  withSources(),
  "org.scalatest" %% "scalatest" % scalaTestVersion,
  "org.scalacheck" %% "scalacheck" % scalacheckVersion
)

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-language:_")

cancelable := true

fork := true