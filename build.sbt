ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "intro_to_fp",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.4.1",
    ),
    scalacOptions ++= Seq("-Xfatal-warnings")
  )
