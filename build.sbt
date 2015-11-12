name := """play-getting-started"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  ws,
  "org.specs2" %% "specs2-core" % "3.6.5" % "test",
  "org.specs2" %% "specs2-junit" % "3.6.3" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )
