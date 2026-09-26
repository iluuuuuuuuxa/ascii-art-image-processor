package converter

import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class NonLinearAsciiConverterTest extends AnyFunSuite {
  test("NonLinearAsciiConverter respects thresholds") {
    // Arrange:
    val distribution = Seq(
      (100, 'A'),
      (200, 'B'),
      (255, 'C')
    )
    val converter = new NonLinearAsciiConverter(distribution)

    // Act & Assert

    // 1. Test strictly below threshold
    assert(converter.charFromGray(GrayscalePixel(50)).value == 'A')

    // 2. Test exact threshold boundary
    assert(converter.charFromGray(GrayscalePixel(100)).value == 'A')

    // 3. Test just above threshold
    assert(converter.charFromGray(GrayscalePixel(101)).value == 'B')

    // 4. Test last bracket
    assert(converter.charFromGray(GrayscalePixel(228)).value == 'C')
  }

  test("NonLinearAsciiConverter uses last char if value exceeds all thresholds") {
    // Arrange
    val distribution = Seq((100, 'A'))
    val validDist = Seq((255, 'Z'))
    val converter = new NonLinearAsciiConverter(validDist)

    // Act & Assert
    assert(converter.charFromGray(GrayscalePixel(255)).value == 'Z')
  }

  test("NonLinearAsciiConverter throws error if distribution does not end at 255") {
    // Arrange
    val invalidDist = Seq((100, 'A'))

    // Assert
    assertThrows[IllegalArgumentException] {
      new NonLinearAsciiConverter(invalidDist)
    }
  }

  test("NonLinearAsciiConverter handles 0 value (Start of range)") {
    // Arrange
    val distribution = Seq((100, 'A'), (255, 'B'))
    val converter = new NonLinearAsciiConverter(distribution)

    // Act & Assert
    assert(converter.charFromGray(GrayscalePixel(0)).value == 'A')
  }
}