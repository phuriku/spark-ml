package com.expedia.paidsearch

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkContext
import org.scalatest._

abstract class BaseSpec extends FlatSpec with BeforeAndAfterAll with Matchers {
  private val master = "local"
  private val appName = "testing"
  var sc: SparkContext = _
  var sqlContext: SQLContext = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    val session = SparkSession.builder
        .master(master)
        .appName(appName)
        .config("spark.driver.allowMultipleContexts", value = true)
        .getOrCreate
    sc = session.sparkContext
    sqlContext = session.sqlContext
  }

  override protected def afterAll(): Unit = {
    try {
      sc.stop()
      sc = null
      sqlContext = null
    } finally {
      super.afterAll()
    }
  }
}

