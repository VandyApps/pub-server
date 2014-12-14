
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
swamped. The API key `password` is passed as the first option. The key ensures that only authorized personnels can 
add an order to the queue. The port number is passed as the second option. The `production` option makes sure that 
the static file server could find the resource files.

    $ java -Xms256m -Xmx256m -jar target/pub-server.jar password 8090 production &

[Note December 13th, 2014] Changed the command line options to be easier to use
