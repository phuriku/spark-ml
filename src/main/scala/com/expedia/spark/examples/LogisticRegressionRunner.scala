package com.expedia.spark.examples

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

object LogisticRegressionRunner extends App {

  val spark = SparkSession
    .builder()
    .appName("Logistic Regression")
    .master("local")
    .getOrCreate()

  val training = spark.read.format("libsvm").load("sample_libsvm_data.txt")

  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)

  val lrModel = lr.fit(training)

  println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

}
