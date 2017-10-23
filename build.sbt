name := "akka-http-rest"
organization := "me.archdev"
version := "1.0.0"
scalaVersion := "2.12.3"

libraryDependencies ++= {
  val akkaV = "10.0.10"
  val scalaTestV = "3.0.4"
  val slickVersion = "3.2.1"
  val circeV = "0.8.0"
  val sttpV = "0.0.20"
  Seq(
    // HTTP server
    "com.typesafe.akka" %% "akka-http" % akkaV,
    "com.typesafe.akka" %% "akka-http-core" % akkaV,

    // Support of CORS requests, version depends on akka-http
    "ch.megard" %% "akka-http-cors" % "0.2.2",

    // SQL generator
    "com.typesafe.slick" %% "slick" % slickVersion,

    // Postgres driver
    "org.postgresql" % "postgresql" % "42.1.4",

    // Migration for SQL databases
    "org.flywaydb" % "flyway-core" % "4.2.0",

    // Connection pool for database
    "com.zaxxer" % "HikariCP" % "2.7.0",

    // Encoding decoding sugar, used in passwords hashing
    "com.roundeights" %% "hasher" % "1.2.0",

    // Parsing and generating of JWT tokens
    "com.pauldijou" %% "jwt-core" % "0.14.0",

    // Config file parser
    "com.github.pureconfig" %% "pureconfig" % "0.8.0",

    // JSON serialization library
    "io.circe" %% "circe-core" % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser" % circeV,

    // Sugar for serialization and deserialization in akka-http with circe
    "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",

    // Validation library
    "com.wix" %% "accord-core" % "0.7.1",

    // Http client, used currently only for IT test
    "com.softwaremill.sttp" %% "core" % sttpV % Test,
    "com.softwaremill.sttp" %% "akka-http-backend" % sttpV % Test,

    "org.scalatest" %% "scalatest" % scalaTestV % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % Test,
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "2.4" % Test,
    "org.mockito" % "mockito-all" % "1.9.5" % Test
  )
}

enablePlugins(UniversalPlugin)
enablePlugins(DockerPlugin)

// Needed for Heroku deployment, can be removed
enablePlugins(JavaAppPackaging)