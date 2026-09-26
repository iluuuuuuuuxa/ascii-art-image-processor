package filters

import image.Image
import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class RotateFilterTest extends AnyFunSuite {
  test("Rotate +90° (Square Image)") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(1), GrayscalePixel(2)),
      Vector(GrayscalePixel(3), GrayscalePixel(4))
    )
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](90)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 3)
    assert(result.getPixel(1, 0).value == 1)
  }

  test("Rotate +180° (Rectangle Image)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(1), GrayscalePixel(2)))
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](180)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 2)
    assert(result.height == 1)
    assert(result.getPixel(0, 0).value == 2)
    assert(result.getPixel(1, 0).value == 1)
  }

  test("Rotate +270° (Square Image)") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(1), GrayscalePixel(2)),
      Vector(GrayscalePixel(3), GrayscalePixel(4))
    )
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](270)

    // Act
    val result = filter.filter(input)


    // Assert
    assert(result.getPixel(0, 0).value == 2)
    assert(result.getPixel(0, 1).value == 1)
  }

  test("Rotate +360° (Identity)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(1), GrayscalePixel(2)))
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](360)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == input.width)
    assert(result.getPixel(0, 0).value == 1)
  }

  test("Rotate -90° (Equivalent to 270°)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(1), GrayscalePixel(2)))
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](-90)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.height == 2)
    assert(result.getPixel(0, 0).value == 2)
  }

  test("Rotate -180° (Equivalent to 180°)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(1), GrayscalePixel(2)))
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](-180)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 2)
    assert(result.getPixel(1, 0).value == 1)
  }

  test("Rotate -270° (Equivalent to 90°)") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(1), GrayscalePixel(2)),
      Vector(GrayscalePixel(3), GrayscalePixel(4))
    )
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](-270)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 3)
    assert(result.getPixel(1, 0).value == 1)
  }

  test("Rotate -360° (Identity)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(99)))
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](-360)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 99)
  }

  test("Rotate 0° (Identity)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(99)))
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](0)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 99)
  }

  test("Rotate handles empty image (0x0)") {
    // Arrange
    val data = Vector.empty[Vector[GrayscalePixel]]
    val input = new Image(data)
    val filter = new RotateFilter[GrayscalePixel](90)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 0)
    assert(result.height == 0)
  }
}