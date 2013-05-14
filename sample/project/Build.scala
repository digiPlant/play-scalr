import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-scalr-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "se.digiplant" %% "play-scalr" % "1.0.1",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.9"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    templatesImport ++= Seq(
      "se.digiplant._"
    )
  )

}
