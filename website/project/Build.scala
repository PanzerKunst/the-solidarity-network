import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "website"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.25",
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.2.1",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
