
pub-server
==========

Server backend for the pub apps. Written in Scala.

Operation
---------

Development requires [SBT](http://www.scala-sbt.org/), [Scala](http://www.scala-lang.org/), and Java 6+. Building 
requires SBT and Java 6+. Running requires only Java 6+.

Compile the program into an executable JAR in `target/`

    $ sbt compile assembly
    
Execute the server program as a background process. The JVM is capped to use only 256MB of memory so the host is not 
swamped. The `env` option makes sure that the static file server could find the resource files. Passing an empty string
to the `adminPort` turns off the feature.

    $ java -Xms256m -Xmx256m -Dcom.twitter.finatra.config.env=production -Dcom.twitter.finatra.config.adminPort='' -jar target/pub-server.jar &
