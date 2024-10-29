name := """baccarat-live"""
organization := "com.tykhe.table.baccarat"
maintainer := "wilson.sam@tykhegaming.com"
version := "1"
scalaVersion := "2.13.6"

lazy val root = (project in file(".")).
  enablePlugins(PlayScala)

pipelineStages := Seq(digest)

libraryDependencies ++= Seq(
  jdbc,
  "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided",
  "org.postgresql" % "postgresql" % "42.2.18",
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config"  % "3.5.0",
  "ch.qos.logback"  %  "logback-classic" % "1.2.3",
  "de.svenkubiak" % "jBCrypt" % "0.4.1",
  "com.lihaoyi" %% "os-lib" % "0.7.8",
  "com.lihaoyi" %% "upickle" % "1.4.0",
  "net.virtual-void" %%  "json-lenses" % "0.6.2",
  "ai.x" %% "play-json-extensions" % "0.42.0",
  "com.github.oshi"         % "oshi-core"               %  "5.8.2",
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

PlayKeys.devSettings += "play.server.http.port" -> "9000"
PlayKeys.devSettings += "play.server.http.idleTimeout" -> "60000s"

//PlayKeys.devSettings += "play.server.https.port" -> "8080"
//PlayKeys.devSettings += "play.crypto.secret" -> "changethissosomethingsecret"
//PlayKeys.devSettings += "play.server.https.keyStore.path" ->  "/home/tykhe/i-gaming.online.jks"
//PlayKeys.devSettings += "play.server.https.keyStore.password" -> "HyMc34"
//PlayKeys.devSettings += "play.server.https.keyStore.type" -> "JKS"
