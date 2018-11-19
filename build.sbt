name := """NDRToolWeb"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.apache.jena" % "apache-jena-libs" % "3.0.0",
  "mysql" % "mysql-connector-java" % "5.1.32"
)
