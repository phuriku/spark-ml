package com.expedia.spark.examples

import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object SGDExample extends App {

  val spark = SparkSession
    .builder()
    .appName("SGD Example")
    .master("local")
    .getOrCreate()

  // Load training data, and train the model with the training data.
  val data = spark.read.format("libsvm")
    .load("my_dataset.txt")
  val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

  val model = new LinearRegression()
    .setRegParam()
    .fit(trainingData)

  // Predict labels for test set, and show this result.
  val predicted = model.transform(testData)
  predicted.show()

  // Print the coefficients and intercept for linear regression
  println(s"Coefficients: ${model.coefficients} Intercept: ${model.intercept}")

  // Summarize the model over the training set and print out some metrics
  println(s"MSE: ${model.summary.meanSquaredError}")
}
