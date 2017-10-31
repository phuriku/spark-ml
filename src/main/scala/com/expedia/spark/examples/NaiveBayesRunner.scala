package com.expedia.spark.examples

import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession

object NaiveBayesRunner extends App {

  val spark = SparkSession
    .builder()
    .appName("Naive Bayes")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  val trainingData = spark.read.format("libsvm")
    .load("sample_gender_data.txt")

  val nb = new NaiveBayes()
    .setSmoothing(1.0)
    .setModelType("multinomial")
  val model = nb.fit(trainingData)

  val testData = Seq((6, 130, 8))
    .toDF("height", "weight", "foot size")
  val testSet = new VectorAssembler()
    .setInputCols(Array("height", "weight", "foot size"))
    .setOutputCol("features")
    .transform(testData)
    .select("features")

  val predictions =  model.transform(testSet)
  predictions.show()
}
