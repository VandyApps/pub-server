
name := "pub-server"

version := "0.0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra" % "1.5.3"
)

resolvers += "Twitter" at "http://maven.twttr.com"


