package com.expedia.spark.util

import java.io.FileWriter

import scala.io.Source

object MovieDatasetCleaner extends App {
  val input = "sample_als_data.txt"
  val target = "sample_als_data_cleaned.txt"

  extract()

  def extract(): Unit = {
    val fw = new FileWriter(target, true)
    try {
      Source.fromFile(input).getLines.foreach { line =>
        fw.write(extractLine(line) + "\n")
      }
    } finally {
      fw.close()
    }
  }

  def extractLine(line: String): String = {
    line.split("::").take(3).mkString("::")
  }
}