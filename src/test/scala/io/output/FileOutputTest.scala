package io.output

import image.Image
import image.pixel.AsciiPixel
import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import scala.io.Source

class FileOutputTest extends AnyFunSuite {
  // Helper to read file content back as a single string
  private def readFile(path: String): String = {
    val source = Source.fromFile(path)
    try source.mkString finally source.close()
  }

  test("FileOutput writes correct ASCII characters to file") {
    // Arrange
    val tempFile = File.createTempFile("ascii_test", ".txt")
    tempFile.deleteOnExit()

    val output = new FileOutput(tempFile.getAbsolutePath)

    val data = Vector(
      Vector(AsciiPixel('A'), AsciiPixel('B')),
      Vector(AsciiPixel('C'), AsciiPixel('D'))
    )
    val image = new Image(data)

    // Act
    output.save(image)

    // Assert
    val content = readFile(tempFile.getAbsolutePath)
    val lines = content.split("\\r?\\n")

    assert(lines.length == 2)
    assert(lines(0) == "AB")
    assert(lines(1) == "CD")
  }

  test("FileOutput overwrites existing file") {
    // Arrange
    val tempFile = File.createTempFile("overwrite_test", ".txt")
    tempFile.deleteOnExit()

    val writer = new java.io.PrintWriter(tempFile)
    writer.print("Old Data")
    writer.close()

    val output = new FileOutput(tempFile.getAbsolutePath)
    val image = new Image(Vector(Vector(AsciiPixel('X'))))

    // Act
    output.save(image)

    // Assert
    val content = readFile(tempFile.getAbsolutePath).trim
    assert(content == "X")
  }

  test("FileOutput throws exception for invalid path") {
    // Arrange
    val invalidPath = "wrong_non_existing_dir/output.txt"
    val output = new FileOutput(invalidPath)

    val image = new Image(Vector(Vector(AsciiPixel('A'))))

    // Act & Assert
    assertThrows[java.io.FileNotFoundException] {
      output.save(image)
    }
  }
}