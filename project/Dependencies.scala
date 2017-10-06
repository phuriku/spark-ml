import sbt._

object Dependencies {
  lazy val sparkVersion = "2.2.0"
  lazy val log4jVersion = "2.4.1"
  lazy val specs2Version = "3.8.5"

  val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"

  val sparkSql = ("org.apache.spark" %% "spark-sql" % sparkVersion
    exclude("aopalliance", "aopalliance")
    exclude("javax.inject", "javax.inject"))

  val sparkSqlKafka = ("org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
    exclude("org.apache.kafka", "kafka_2.11"))

  val sparkMl = "org.apache.spark" %% "spark-mllib" % sparkVersion

  val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkVersion
  val sparkStreamingKafka = ("org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
    exclude("org.apache.kafka", "kafka_2.11"))

  val kafka = "org.apache.kafka" %% "kafka" % "0.11.0.0"

  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.12.2"

  val json4s = "org.json4s" %% "json4s-native" % "3.5.0"

  val log4jApi = "org.apache.logging.log4j" % "log4j-api" % log4jVersion
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % log4jVersion

  val sparkTestingBase = "com.holdenkarau" %% "spark-testing-base" % "2.2.0_0.7.2" % "test"

  val specs2Core = "org.specs2" %% "specs2-core" % specs2Version % "test"
  val specs2Mock = "org.specs2" %% "specs2-mock" % specs2Version % "test"

}
