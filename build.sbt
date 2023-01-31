name := "essential-slick"

version := "1.0"

scalaVersion := "2.13.10"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-feature",
  "-Xlint",
  "-Xfatal-warnings"
)

// Core dependencies:
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"           % "3.4.1",
  "ch.qos.logback"      % "logback-classic" % "1.4.5",
)

// Dependencies needed to connect to H2:
libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "2.1.214",
)

// Dependencies needed to connect to MySQL:
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "8.0.13",
)

Global / onChangedBuildSource := ReloadOnSourceChanges
