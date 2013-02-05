resolvers ++= Seq(Classpaths.typesafeResolver, ScalaToolsReleases)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12.3" % "test",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.1"
)

// disable publishing the main javadoc jar
publishArtifact in (Compile, packageDoc) := false
