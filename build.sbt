import Dependencies._

val internalNexus = "http://nexuslab.alm/nexus/content/repositories"

lazy val projectSettings = Seq(
  organization := "com.jemberton",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.11",

  libraryDependencies ++= Seq(
    sparkSql,
    sparkSqlKafka,
    sparkStreaming,
    sparkStreamingKafka,
    sparkMl,
    kafka,
    mockito,
    scalaCheck,
    json4s,
    log4jApi,
    log4jCore,
    sparkTestingBase,
    specs2Core,
    specs2Mock
  ),

  resolvers ++= Seq(
    "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/",
    "Typesafe Simple Repository" at "http://repo.typesafe.com/typesafe/simple/maven-releases/",
    "twitter-repo" at "https://maven.twttr.com"
  ),

  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xlint"
  ),

  // These are necessary for our test framework, spark-testing-base.
  parallelExecution in Test := false,
  fork in Test := true,

  // This sends output in forked run to stdout by default (rather than stderr)
  outputStrategy in run := Some(StdoutOutput),

  mainClass in assembly := Some("com.expedia.spark.examples.LogisticRegressionRunner"),
  assemblyJarName in assembly := name.value + "-spark-streaming" + ".jar",

  mainClass in run := Some("com.expedia.spark.examples.LogisticRegressionRunner"),

  assemblyMergeStrategy in assembly := {
    case PathList("org", "apache", xs@_*) => MergeStrategy.last
    case PathList("com", "google", xs@_*) => MergeStrategy.last
    case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
    case PathList(ps@_*) if ps.last endsWith ".types" => MergeStrategy.last
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val root =
  Project("spark-examples", file("."))
    .settings(projectSettings: _*)
