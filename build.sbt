
name := "pub-server"

version := "0.0.1"

scalaVersion := "2.10.3"

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-encoding", "utf8",
  "-feature",
  "-language:implicitConversions")

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra" % "1.5.3"
)

resolvers += "Twitter" at "http://maven.twttr.com"


