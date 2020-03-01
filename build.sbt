name := """pcms-with-mongo"""
organization := "ikaee"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.8.0"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.6"
libraryDependencies += "org.apache.commons" % "commons-email" % "1.5"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "ikaee.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "ikaee.binders._"
