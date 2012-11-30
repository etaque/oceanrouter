name := "Basic Project"

organization := "fr.skiffr"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12" % "test",
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "org.scalaz" %% "scalaz-geo" % "6.0.3",
  "org.scalaj" %% "scalaj-time" % "0.6"
)

initialCommands := "import fr.skiffr.oceanrider._"
