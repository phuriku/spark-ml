package com.expedia.spark.examples

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SparkSession

object ALSRunner extends App {

  val spark = SparkSession
    .builder()
    .appName("Alternative Least Squares")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  case class Rating(userId: Int, movieId: Int, rating: Float)

  def parseRating(str: String): Rating = {
    val fields = str.split("::")
    Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat)
  }

  val ratings = spark.read.textFile("sample_als_data_cleaned.txt")
    .map(parseRating)
    .toDF()
  val Array(training, test) = ratings.randomSplit(Array(0.7, 0.3))

  val als = new ALS()
    .setMaxIter(5)
    .setRegParam(0.01)
    .setUserCol("userId")
    .setItemCol("movieId")
    .setRatingCol("rating")
  val model = als.fit(training)

  // Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
  model.setColdStartStrategy("drop")
  val predictions = model.transform(test)
  println("Predictions:")
  predictions.show()

  val evaluator = new RegressionEvaluator()
    .setMetricName("rmse")
    .setLabelCol("rating")
    .setPredictionCol("prediction")
  val rmse = evaluator.evaluate(predictions)
  println(s"MSE = ${Math.pow(rmse, 2)}")

  // Generate top 5 movie recommendations for each user
  val userRecs = model.recommendForAllUsers(5)
  println("Top 5 Movie Recommendations for Each User:")
  userRecs.show()
}
