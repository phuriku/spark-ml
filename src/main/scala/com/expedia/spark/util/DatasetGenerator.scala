package com.expedia.spark.util

import java.io.FileWriter
import java.nio.file.{Files, Paths}

import scala.util.Random

object DatasetGenerator extends App {
  val target = "my_dataset.txt"
  val numPoints = 2000

  if (Files.exists(Paths.get(target))) Files.delete(Paths.get(target))
  val fw = new FileWriter(target, true)
  try {
    (1 to numPoints).foreach { _ =>
      fw.write(generateRandomLine() + "\n")
    }
  } finally {
    fw.close()
  }

  def generateRandomLine(): String = {
    val x = Random.nextInt(100)
    val y = Random.nextInt(200)

    s"$x 1:$y 2:${x*2}"
  }
}
