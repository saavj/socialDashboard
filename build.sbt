name := """social-dashboard"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  ws,
  specs2 % Test,
  "org.specs2" %% "specs2-scalacheck" % "3.6" % "test",
  "org.specs2" %% "specs2-matcher-extra" % "3.6" % "test",
  "net.codingwell"              %% "scala-guice"     % "4.0.0",
  "pl.matisoft"                 %% "swagger-play24"  % "1.4",
  "com.typesafe.scala-logging"  %% "scala-logging"   % "3.1.0",
  "com.netaporter"              %% "scala-uri"       % "0.4.9"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
