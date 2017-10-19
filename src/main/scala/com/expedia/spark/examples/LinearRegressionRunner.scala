package com.expedia.spark.examples

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object LinearRegressionRunner extends App {

  val spark = SparkSession
    .builder()
    .appName("Linear Regression")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  // Prepare the test set and transform it into a Spark Dataset.
  val testSet = Seq(2,4,5)
    .toDF("input")
  val dataset = new VectorAssembler()
    .setInputCols(Array("input"))
    .setOutputCol("features")
    .transform(testSet)
    .select("features")

  // Load training data, and train the model with the training data.
  val training = spark.read.format("libsvm")
    .load("sample_linear_regression_data.txt")
  val model = new LinearRegression().fit(training)

  // Predict labels for test set, and show this result.
  val predicted = model.transform(dataset)
  predicted.show()

  // Print the coefficients and intercept for linear regression
  println(s"Coefficients: ${model.coefficients} Intercept: ${model.intercept}")

  // Summarize the model over the training set and print out some metrics
  println(s"MSE: ${model.summary.meanSquaredError}")
}
