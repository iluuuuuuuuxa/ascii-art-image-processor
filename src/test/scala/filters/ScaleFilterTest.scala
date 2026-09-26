package filters

import image.Image
import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class ScaleFilterTest extends AnyFunSuite {
  test("ScaleFilter (0.5) shrinks image") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(100), GrayscalePixel(100)),
      Vector(GrayscalePixel(100), GrayscalePixel(100))
    )
    val input = new Image(data)
    val filter = new ScaleFilter(0.5)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 1)
    assert(result.height == 1)
    assert(result.getPixel(0, 0).value == 100)
  }

  test("Scale 1.0 (Identity)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(10)))
    val input = new Image(data)
    val filter = new ScaleFilter(1.0)
    
    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 1)
    assert(result.getPixel(0, 0).value == 10)
  }

  test("Scale 2.0 (Up scaling Nearest Neighbor)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(50)))
    val input = new Image(data)
    val filter = new ScaleFilter(2.0)
    
    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 2)
    assert(result.height == 2)
    assert(result.getPixel(0, 0).value == 50)
    assert(result.getPixel(1, 1).value == 50)
  }

  test("ScaleFilter handles empty image (0x0)") {
    val data = Vector.empty[Vector[GrayscalePixel]]
    val input = new Image(data)
    val filter = new ScaleFilter(2.0)

    val result = filter.filter(input)

    assert(result.width == 0)
    assert(result.height == 0)
  }
}
