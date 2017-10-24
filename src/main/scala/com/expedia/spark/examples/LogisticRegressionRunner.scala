package com.expedia.spark.examples

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession

object LogisticRegressionRunner extends App {

  val spark = SparkSession
    .builder()
    .appName("Logistic Regression")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  // Prepare the test set and transform it into a Spark Dataset.
  val testSet = Seq(2,4,9)
    .toDF("input")
  val dataset = new VectorAssembler()
    .setInputCols(Array("input"))
    .setOutputCol("features")
    .transform(testSet)
    .select("features")

  // Load training data, and train the model with the training data.
  val training = spark.read.format("libsvm")
    .load("sample_logistic_regression_data.txt")
  val model = new LogisticRegression().fit(training)

  // Predict labels for test set, and show this result.
  val predicted = model.transform(dataset)
  predicted.show()

  // Print the coefficients and intercept for logistic regression
  println(s"Coefficients: \n${model.coefficientMatrix} \nIntercept: ${model.interceptVector}")
  println(s"Iterations: ${model.summary.totalIterations}")
}
