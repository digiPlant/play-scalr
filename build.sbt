import play.PlayImport.PlayKeys._

name := "play-scalr"

version := "1.1.2"

scalaVersion := "2.11.1"

javacOptions ++= Seq("-source", "1.7")

crossScalaVersions := Seq("2.10.4", "2.11.1")

organization := "se.digiplant"

playPlugin := true

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "se.digiplant" %% "play-res" % "1.1.1",
  "com.typesafe.play" %% "play" % play.core.PlayVersion.current % "provided",
  "com.typesafe.play" %% "play-test" % play.core.PlayVersion.current % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
