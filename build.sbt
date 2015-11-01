name := "smpp-akka"
organization := "org.bulatnig"
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += Resolver.mavenLocal

libraryDependencies += "org.bulatnig" %% "smpp-api" % "1.0-SNAPSHOT"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
