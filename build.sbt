name := "akka-http-rest"
organization := "me.archdev"
version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.4"
  val scalaTestV = "3.0.0-M15"
  val slickVersion = "3.1.1"
  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "org.mindrot" % "jbcrypt" % "0.3m",
    "org.flywaydb" % "flyway-core" % "3.2.1",

    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "1.13" % "test"
  )
}

Revolver.settings
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts := Seq(9000)
dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf")

parallelExecution in Test := false