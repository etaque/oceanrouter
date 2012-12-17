import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

name := "Ocean Router"

organization := ""

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

scalacOptions := Seq("-Ydependent-method-types", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "GeoTools" at "http://download.osgeo.org/webdav/geotools/",
  "spray repo" at "http://repo.spray.io/",
  "akka repo" at "http://repo.akka.io/releases/"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12" % "test",
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "org.scalaz" %% "scalaz-geo" % "6.0.3",
  "org.scalaj" %% "scalaj-time" % "0.6",
  "net.sf.supercsv" % "super-csv" % "2.0.0",
  "org.geotools" % "gt-main" % "8.4",
  "org.geotools" % "gt-referencing" % "8.4",
  "io.spray" % "spray-can" % "1.0-M6",
  "io.spray" % "spray-routing" % "1.0-M6",
  "io.spray" %% "spray-json" % "1.2.3",
  "com.typesafe.akka" % "akka-actor" % "2.0.4"
)

initialCommands := "import oceanrouter._"

