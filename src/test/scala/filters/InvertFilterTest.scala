package filters

import image.Image
import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class InvertFilterTest extends AnyFunSuite{
  test("InvertFilter flips values correctly") {
    // Arrange
    val input = new Image(Vector(Vector(GrayscalePixel(10))))
    val filter = new InvertFilter()

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 245)
  }

  test("InvertFilter handles boundary values (0 and 255)") {
    // Arrange:
    val data = Vector(Vector(GrayscalePixel(0), GrayscalePixel(255)))
    val input = new Image(data)
    val filter = new InvertFilter()

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 255)
    assert(result.getPixel(1, 0).value == 0)
  }

  test("InvertFilter is its own inverse (Double Invert = Original)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(123)))
    val input = new Image(data)
    val filter = new InvertFilter()

    // Act
    val once = filter.filter(input)
    val twice = filter.filter(once)

    // Assert
    assert(twice.getPixel(0, 0).value == 123)
  }

  test("InvertFilter handles mixed values correctly") {
    // Arrange
    val data = Vector(Vector(
      GrayscalePixel(50),
      GrayscalePixel(100),
      GrayscalePixel(150),
      GrayscalePixel(200)
    ))
    val input = new Image(data)
    val filter = new InvertFilter()

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 205)
    assert(result.getPixel(1, 0).value == 155)
    assert(result.getPixel(2, 0).value == 105)
    assert(result.getPixel(3, 0).value == 55)
  }

  test("InvertFilter handles empty image (0x0)") {
    // Arrange
    val data = Vector.empty[Vector[GrayscalePixel]]
    val input = new Image(data)
    val filter = new InvertFilter()

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 0)
    assert(result.height == 0)
  }
}