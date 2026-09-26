package io.input

import org.scalatest.funsuite.AnyFunSuite

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageIOFileLoaderTest extends AnyFunSuite {
  // Helper to create a temporary dummy image
  private def createTempImage(name: String): File = {
    val file = new File(System.getProperty("java.io.tmpdir"), name)
    val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    // Set pixel to Pure Red (255, 0, 0)
    img.setRGB(0, 0, new Color(255, 0, 0).getRGB)
    ImageIO.write(img, "png", file)
    file.deleteOnExit() // Clean up after ourselves
    file
  }

  test("Successfully loads a valid PNG") {
    // Arrange
    val tempFile = createTempImage("test_valid.png")
    val loader = new ImageIOFileLoader(tempFile.getAbsolutePath)

    // Act
    val image = loader.load()

    // Assert
    assert(image.width == 1)
    assert(image.height == 1)

    val pixel = image.getPixel(0, 0)
    assert(pixel.r == 255)
    assert(pixel.g == 0)
    assert(pixel.b == 0)
  }

  test("Throws exception for non-existent file") {
    // Arrange
    val loader = new ImageIOFileLoader("ghost_file.png")

    // Act & Assert
    assertThrows[IllegalArgumentException] {
      loader.load()
    }

    val error = intercept[IllegalArgumentException] {
      loader.load()
    }

    assert(error.getMessage.contains("File not found"))
  }

  test("Throws exception for unsupported extension (.txt)") {
    // Arrange
    val loader = new ImageIOFileLoader("document.txt")

    // Act & Assert
    val error = intercept[IllegalArgumentException] {
      loader.load()
    }
    
    // Verify the message mentions extensions
    assert(error.getMessage.contains("Unsupported file extension"))
  }

  test("Throws exception for corrupt/invalid image content") {
    // Arrange
    val file = File.createTempFile("corrupt", ".png")
    val writer = new java.io.PrintWriter(file)
    writer.write("This is just text, not a real image!")
    writer.close()
    file.deleteOnExit()

    val loader = new ImageIOFileLoader(file.getAbsolutePath)

    // Act & Assert
    val error = intercept[IllegalArgumentException] {
      loader.load()
    }
    assert(error.getMessage.contains("Format not supported or file broken"))
  }
}