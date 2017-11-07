package com.expedia.spark.examples

import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg.{Matrix, SingularValueDecomposition, Vectors}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

object SVDRunner extends App {

  val conf = new SparkConf()
    .setAppName("SVD")
    .setMaster("local")
  val sc = new SparkContext(conf)

  val data = (1 to 20).map { x =>
    Vectors.dense(x * 3 + 1, x, Random.nextInt(10))
  }.toArray

  val rows = sc.parallelize(data)
  val mat: RowMatrix = new RowMatrix(rows)

  val svd: SingularValueDecomposition[RowMatrix, Matrix] = mat.computeSVD(2, computeU = true)
  val U: RowMatrix = svd.U  // The U factor is a RowMatrix.
  val S = svd.s             // The singular values are stored in a local dense vector.
  val V: Matrix = svd.V     // The V factor is a local dense matrix.
  println(s"First 2 eigenvalues in S: $S")
}