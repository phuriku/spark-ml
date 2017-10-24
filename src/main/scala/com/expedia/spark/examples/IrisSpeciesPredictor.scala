package com.expedia.spark.examples

import java.nio.file.{Files, Paths}

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.SparkSession

object IrisSpeciesPredictor extends App {

  val spark = SparkSession
    .builder()
    .appName("Iris Species Predictor")
    .master("local")
    .getOrCreate()

  // Load training data and test data.
  if (!Files.exists(Paths.get("iris_dataset_cleaned.txt"))) throw new Exception("Use IrisDatasetCleaner before running this program.")
  val data = spark.read.format("libsvm").load("iris_dataset_cleaned.txt")
  val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

  // Train the model using the training data.
  val model = new LogisticRegression()
    .setFamily("multinomial")
    .fit(trainingData)

  // Predict labels for test data, and show this result.
  val predicted = model.transform(testData)
  predicted.show()

  // Calculate which labels were the same as predicted among the test data.
  var correct = 0
  var wrong = 0
  predicted.foreach { row =>
    if (row(0) == row(4)) correct += 1
    else wrong += 1
  }
  println(s"Correct: $correct; Wrong: $wrong; Accuracy: ${correct/(correct+wrong*1.0)}\n")

  // Evaluate the Mean Square Error.
  val evaluator = new RegressionEvaluator()
    .setLabelCol("label")
    .setPredictionCol("prediction")
    .setMetricName("mse")
  val MSE = evaluator.evaluate(predicted)
  println(s"MSE = $MSE\n")

  // Print the coefficients and intercept for logistic regression
  println(s"Coefficients: \n${model.coefficientMatrix} \nIntercept: ${model.interceptVector}")
}
