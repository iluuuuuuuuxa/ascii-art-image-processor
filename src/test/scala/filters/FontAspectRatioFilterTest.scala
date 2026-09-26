package filters

import image.Image
import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class FontAspectRatioFilterTest extends AnyFunSuite{
  test("FontAspectRatioFilter shrinks height") {
    // Arrange
    val data = Vector.tabulate(100, 100)((y, x) => GrayscalePixel(0))
    val input = new Image(data)
    val filter = new FontAspectRatioFilter("1:2")

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 100)
    assert(result.height == 50)
  }

  test("FontAspectRatioFilter handles rectangular input") {
    // Arrange
    val data = Vector.tabulate(100, 200)((_, _) => GrayscalePixel(0))
    val input = new Image(data)
    val filter = new FontAspectRatioFilter("1:2")

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 200)
    assert(result.height == 50)
  }

  test("FontAspectRatioFilter handles 1:1 ratio (Identity)") {
    // Arrange
    val data = Vector.tabulate(100, 100)((y, x) => GrayscalePixel(50))
    val input = new Image(data)
    val filter = new FontAspectRatioFilter("1:1")

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 100)
    assert(result.height == 100)
  }

  test("FontAspectRatioFilter expands height for wide fonts (2:1)") {
    // Arrange
    val data = Vector.tabulate(100, 100)((_, _) => GrayscalePixel(50))
    val input = new Image(data)

    val filter = new FontAspectRatioFilter("2:1")

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 100)
    assert(result.height == 200)
  }

  test("FontAspectRatioFilter handles empty image (0x0)") {
    // Arrange
    val data = Vector.empty[Vector[GrayscalePixel]]
    val input = new Image(data)
    val filter = new FontAspectRatioFilter("1:2")

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 0)
    assert(result.height == 0)
  }
}