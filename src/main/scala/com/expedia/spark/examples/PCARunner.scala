package com.expedia.spark.examples

import org.apache.spark.ml.feature.PCA
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

import scala.util.Random

object PCARunner extends App {

  val spark = SparkSession
    .builder()
    .appName("PCA")
    .master("local")
    .getOrCreate()

  val data = (1 to 20).map { x =>
    Vectors.dense(x * 3 + 1, x, Random.nextInt(10))
  }.toArray

  val df = spark
    .createDataFrame(data.map(Tuple1.apply))
    .toDF("features")

  val pca = new PCA()
    .setInputCol("features")
    .setOutputCol("New Feature Space")
    .setK(2)
    .fit(df)

  val result = pca
    .transform(df)
    .select("New Feature Space")

  result.show(false)
  spark.stop()
  println(s"Explained variance: ${pca.explainedVariance}")
}