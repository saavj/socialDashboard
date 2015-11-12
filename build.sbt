name := """social-dashboard"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  ws,
  "org.specs2" %% "specs2-core"  % "3.6.5" % "test",
  "org.specs2" %% "specs2-junit" % "3.6.5" % "test",
  "org.specs2" %% "specs2-mock"  % "3.6.5" % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )
