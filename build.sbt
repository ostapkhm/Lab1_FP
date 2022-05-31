ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.0.2"

lazy val root = (project in file("."))
  .settings(
    name := "FirstLab_FP"
  )

libraryDependencies += "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test