package com.expedia.spark.util

import java.io.FileWriter
import java.nio.file.{Files, Paths}

import scala.io.Source
import scala.util.Try

object IrisDatasetCleaner extends App {

  val source = "iris_dataset.txt"
  val target = "iris_dataset_cleaned.txt"

  def extractSVMLib(): Unit = {
    if (Try(Paths.get(target)).toOption.isDefined) Files.delete(Paths.get(target))
    val fw = new FileWriter(target, true)
    try {
      Source.fromFile(source).getLines.foreach { line =>
        fw.write(extractLine(line) + "\n")
      }
    } finally {
      fw.close()
    }
  }

  def extractLine(line: String): String = {
    (line match {
      case x: String if x.endsWith(",Iris-setosa") => s"1 1:${x.replaceAll(",Iris-setosa", "")}"
      case x: String if x.endsWith(",Iris-versicolor") => s"2 1:${x.replaceAll(",Iris-versicolor", "")}"
      case x: String if x.endsWith(",Iris-virginica") => s"3 1:${x.replaceAll(",Iris-virginica", "")}"
    }).replaceFirst(",", " 2:").replaceFirst(",", " 3:").replaceFirst(",", " 4:")
  }

}
