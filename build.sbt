val ScalatraVersion = "2.8.2"

ThisBuild / scalaVersion := "2.13.9"
ThisBuild / organization := "com.example"

ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)

lazy val hello = (project in file("."))
  .settings(
    name := "scalatra-test",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.43.v20210629" % "container",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",

      "org.scalikejdbc" %% "scalikejdbc" % "4.0.0",
      "org.scalikejdbc" %% "scalikejdbc-test" % "4.0.0" % "test",
      "com.h2database" % "h2" % "1.4.200",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "mysql" % "mysql-connector-java" % "8.0.22",
      "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
    ),
  )
  .enablePlugins(SbtTwirl)
  .enablePlugins(JettyPlugin)


