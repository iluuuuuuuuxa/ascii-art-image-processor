package converter

import image.Image
import image.pixel.RGBPixel
import org.scalatest.funsuite.AnyFunSuite

class RgbToGrayscaleTest extends AnyFunSuite {
  test("RgbToGrayscale calculates correct luminance") {
    // Arrange
    val input = new Image(Vector(Vector(RGBPixel(255, 0, 0))))
    val converter = new RgbToGrayscale()

    // Act
    val result = converter.convert(input)

    // Assert
    assert(result.getPixel(0, 0).value == 76)
  }

  test("RgbToGrayscale handles White (Max Value)") {
    // Arrange
    val input = new Image(Vector(Vector(RGBPixel(255, 255, 255))))
    val converter = new RgbToGrayscale()

    // Act
    val result = converter.convert(input)

    // Assert
    assert(result.getPixel(0, 0).value == 255)
  }

  test("RgbToGrayscale handles Black (Min Value)") {
    // Arrange
    val input = new Image(Vector(Vector(RGBPixel(0, 0, 0))))
    val converter = new RgbToGrayscale()

    // Act
    val result = converter.convert(input)

    // Assert
    assert(result.getPixel(0, 0).value == 0)
  }

  test("RgbToGrayscale verifies specific Green and Blue coefficients") {
    // Arrange
    val converter = new RgbToGrayscale()
    val greenImg = new Image(Vector(Vector(RGBPixel(0, 255, 0))))
    val blueImg = new Image(Vector(Vector(RGBPixel(0, 0, 255))))

    // Act & Assert
    assert(converter.convert(greenImg).getPixel(0, 0).value == 150)
    assert(converter.convert(blueImg).getPixel(0, 0).value == 28)
  }
}