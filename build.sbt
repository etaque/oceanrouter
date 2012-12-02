name := "Basic Project"

organization := "fr.skiffr"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers += "GeoTools" at "http://download.osgeo.org/webdav/geotools/"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12" % "test",
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "org.scalaz" %% "scalaz-geo" % "6.0.3",
  "org.scalaj" %% "scalaj-time" % "0.6",
  "net.sf.supercsv" % "super-csv" % "2.0.0",
  "org.geotools" % "gt-main" % "8.4",
  "org.geotools" % "gt-referencing" % "8.4"
)

initialCommands := "import fr.skiffr.oceanrider._"
