package filters

import image.Image
import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class FlipFilterTest extends AnyFunSuite {
  test("Flip X (Reverse Row)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(1), GrayscalePixel(2)))
    val input = new Image(data)
    val filter = new FlipFilter[GrayscalePixel](FlipAxis.X)
    
    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 2)
    assert(result.getPixel(1, 0).value == 1)
  }

  test("Flip Y (Reverse Column)") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(1), GrayscalePixel(2)),
      Vector(GrayscalePixel(3), GrayscalePixel(4))
    )
    val input = new Image(data)
    val filter = new FlipFilter[GrayscalePixel](FlipAxis.Y)
    
    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 3)
    assert(result.getPixel(1, 0).value == 4)
  }

  test("Double Flip X restores original image") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(10), GrayscalePixel(20)))
    val input = new Image(data)
    val filter = new FlipFilter[GrayscalePixel](FlipAxis.X)

    // Act
    val flippedOnce = filter.filter(input)
    val flippedTwice = filter.filter(flippedOnce)

    // Assert
    assert(flippedTwice.getPixel(0, 0).value == 10)
    assert(flippedTwice.getPixel(1, 0).value == 20)
  }

  test("Double Flip Y restores original image") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(10)),
      Vector(GrayscalePixel(20))
    )
    val input = new Image(data)
    val filter = new FlipFilter[GrayscalePixel](FlipAxis.Y)

    // Act
    val flippedOnce = filter.filter(input)
    val flippedTwice = filter.filter(flippedOnce)

    // Assert
    assert(flippedTwice.getPixel(0, 0).value == 10)
    assert(flippedTwice.getPixel(0, 1).value == 20)
  }

  test("Flip X then Flip Y works correctly") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(1), GrayscalePixel(2)),
      Vector(GrayscalePixel(3), GrayscalePixel(4))
    )
    val input = new Image(data)

    val flipX = new FlipFilter[GrayscalePixel](FlipAxis.X)
    val flipY = new FlipFilter[GrayscalePixel](FlipAxis.Y)

    // Act
    val xResult = flipX.filter(input)
    val finalResult = flipY.filter(xResult)

    // Assert
    assert(finalResult.getPixel(0, 0).value == 4)
    assert(finalResult.getPixel(1, 0).value == 3)
    assert(finalResult.getPixel(0, 1).value == 2)
    assert(finalResult.getPixel(1, 1).value == 1)
  }

  test("FlipFilter handles empty image (0x0)") {
    // Arrange
    val data = Vector.empty[Vector[GrayscalePixel]]
    val input = new Image(data)
    val filter = new FlipFilter[GrayscalePixel](FlipAxis.X)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 0)
    assert(result.height == 0)
  }
}