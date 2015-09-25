name := """crossover"""

version := "1.0-SNAPSHOT"

resolvers += Resolver.url("Edulify Repository", url("http://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)

resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.hibernate" % "hibernate-core" % "4.3.9.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Final",
  "mysql" % "mysql-connector-java" % "5.1.27",
  "org.scala-lang" % "scala-library" % "2.11.6",
  "com.edulify" %% "play-hikaricp" % "2.0.5"
)


