ThisBuild / scalaVersion := "3.4.2"
ThisBuild / organization := "Chichi"

name := "scala-wordle"
version := "1.0.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "com.lihaoyi" %% "upickle" % "3.1.0"
)