package com.expedia.spark.examples

import org.apache.spark.ml.classification.{MultilayerPerceptronClassificationModel, MultilayerPerceptronClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object MultilayerPerceptronRunner extends App {

  val spark: SparkSession = SparkSession
    .builder()
    .appName("Multilayer Perceptron")
    .master("local")
    .getOrCreate()

  val timestampStart: Long = System.currentTimeMillis

  val data: DataFrame = spark.read.format("libsvm")
      .load("mnist.txt")

  // Split the data into training and test datasets.
  val splits: Array[Dataset[Row]] = data.randomSplit(Array(0.9, 0.1), seed = 1234L)
  val train = splits(0)
  val test = splits(1)

  // Input layer of size 780 (features; there were no features from pixels 781-784),
  // 1 hidden layer of 15 neurons,
  // and output of size 10 (categories)
  val layers: Array[Int] = Array[Int](780, 15, 10)

  val trainer: MultilayerPerceptronClassifier = new MultilayerPerceptronClassifier()
    .setLayers(layers)
    .setBlockSize(128)
    .setSeed(1234L)
    .setMaxIter(100)
    //.setTol(1E-7) // Default is 1E-4

  // Train the model.
  val model: MultilayerPerceptronClassificationModel = trainer.fit(train)

  // Compute accuracy on the test dataset.
  val result: DataFrame = model.transform(test)

  val predictionAndLabels: DataFrame = result.select("prediction", "label")
  val evaluator: MulticlassClassificationEvaluator = new MulticlassClassificationEvaluator()
    .setMetricName("accuracy")

  println(s"Duration: ${System.currentTimeMillis - timestampStart} ms")
  println(s"Accuracy = ${evaluator.evaluate(predictionAndLabels)}")
}
