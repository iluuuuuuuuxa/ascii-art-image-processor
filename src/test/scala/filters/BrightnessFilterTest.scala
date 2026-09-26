package filters

import image.Image
import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class BrightnessFilterTest extends AnyFunSuite {
  test("BrightnessFilter (+10) increases value") {
    // Arrange
    val input = new Image(Vector(Vector(GrayscalePixel(100))))
    val filter = new BrightnessFilter(10)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 110)
  }

  test("BrightnessFilter applies to entire grid") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(10), GrayscalePixel(20)),
      Vector(GrayscalePixel(30), GrayscalePixel(40))
    )
    val input = new Image(data)
    val filter = new BrightnessFilter(10)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.getPixel(0, 0).value == 20)
    assert(result.getPixel(1, 0).value == 30)
    assert(result.getPixel(0, 1).value == 40)
    assert(result.getPixel(1, 1).value == 50)
  }

  test("Brightness Max Clamp (255)") {
    // Arrange
    val input = new Image(Vector(Vector(GrayscalePixel(250))))
    val filter = new BrightnessFilter(20)
    
    // Act
    val result = filter.filter(input)
    
    // Assert
    assert(result.getPixel(0, 0).value == 255)
  }

  test("Brightness Min Clamp (0)") {
    // Arrange
    val input = new Image(Vector(Vector(GrayscalePixel(10))))
    val filter = new BrightnessFilter(-20)
    
    // Act
    val result = filter.filter(input)
    
    // Assert
    assert(result.getPixel(0, 0).value == 0)
  }

  test("BrightnessFilter canceling out") {
    // Arrange
    val input = new Image(Vector(Vector(GrayscalePixel(100))))
    val filter1 = new BrightnessFilter(10)
    val filter2 = new BrightnessFilter(-10)

    // Act
    val result1 = filter1.filter(input)
    val result2 = filter2.filter(result1)

    // Assert
    assert(result2.getPixel(0, 0) == input.getPixel(0, 0))
    assert(result2.getPixel(0, 0).value == 100)
  }

  test("Brightness not equal after clamping") {
    // Arrange
    val input = new Image(Vector(Vector(GrayscalePixel(250))))
    val filter1 = new BrightnessFilter(20)
    val filter2 = new BrightnessFilter(-20)

    // Act
    val result1 = filter1.filter(input) // 250 + 20 -> 270 -> Clamped to 255
    val result2 = filter2.filter(result1) // 255 - 20 -> 235

    // Assert
    assert(result1.getPixel(0, 0).value == 255) // Validates the clamp worked
    assert(result2.getPixel(0, 0).value == 235) // Validates that info was lost
    assert(result2.getPixel(0, 0).value != input.getPixel(0, 0).value)
  }

  test("BrightnessFilter handles empty image (0x0)") {
    // Arrange
    val data = Vector.empty[Vector[GrayscalePixel]]
    val input = new Image(data)
    val filter = new BrightnessFilter(50)

    // Act
    val result = filter.filter(input)

    // Assert
    assert(result.width == 0)
    assert(result.height == 0)
  }
}