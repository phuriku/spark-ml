package com.expedia.spark.examples

import com.expedia.spark.examples.LinearRegressionRunner.model
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object RegularizationExample extends App {

  val spark = SparkSession
    .builder()
    .appName("Regularization Example")
    .master("local")
    .getOrCreate()

  // Load training data, and train the model with the training data.
  val data = spark.read.format("libsvm")
    .load("my_dataset.txt")
  val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

  val model = new LinearRegression()
    .setRegParam(0.8)
    .setElasticNetParam(0)
    .fit(trainingData)

  // Predict labels for test set, and show this result.
  val predicted = model.transform(testData)
  predicted.show()

  // Print the coefficients and intercept for linear regression
  println(s"Coefficients: ${model.coefficients} Intercept: ${model.intercept}")

  // Summarize the model over the training set and print out some metrics
  println(s"MSE: ${model.summary.meanSquaredError}")
  println(s"Iterations: ${model.summary.totalIterations}")
}
