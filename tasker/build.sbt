scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
    "com.typesafe.akka" %% "akka-actor" % "2.1.4",
    "play" %% "anorm" % "2.1.1",
    "com.github.seratch" %% "scalikejdbc" % "[1.6,)",
    "com.github.seratch" %% "scalikejdbc-config" % "[1.6,)",
    "mysql" % "mysql-connector-java" % "5.1.25",
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
    "org.fusesource.scalate" %% "scalate-core" % "1.6.1",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.9.10"
)
