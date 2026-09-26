package converter

import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class LinearAsciiConverterTest extends AnyFunSuite{
  test("LinearAsciiConverter maps min/max correctly") {
    // Arrange
    // 0-127 should be 'A', 128-255 should be 'B'
    val converter = new LinearAsciiConverter("AB")

    // Act & Assert
    // Min Value (0) -> First Char
    assert(converter.charFromGray(GrayscalePixel(0)).value == 'A')

    // Max Value (255) -> Last Char
    assert(converter.charFromGray(GrayscalePixel(255)).value == 'B')
  }

  test("LinearAsciiConverter handles middle values") {
    // Arrange
    // Range 0-255 divided by 3 is approx 85.3
    // 0-84 -> A
    // 85-170 -> B
    // 171-255 -> C
    val converter = new LinearAsciiConverter("ABC")

    // Act & Assert
    assert(converter.charFromGray(GrayscalePixel(40)).value == 'A')
    assert(converter.charFromGray(GrayscalePixel(100)).value == 'B')
    assert(converter.charFromGray(GrayscalePixel(200)).value == 'C')
  }

  test("LinearAsciiConverter handles single character table") {
    // Arrange
    val converter = new LinearAsciiConverter(".")

    // Act & Assert
    assert(converter.charFromGray(GrayscalePixel(0)).value == '.')
    assert(converter.charFromGray(GrayscalePixel(128)).value == '.')
    assert(converter.charFromGray(GrayscalePixel(255)).value == '.')
  }

  test("LinearAsciiConverter handles long tables") {
    // Arrange
    val table = "0123456789"
    val converter = new LinearAsciiConverter(table)

    // Act & Assert
    assert(converter.charFromGray(GrayscalePixel(0)).value == '0')
    assert(converter.charFromGray(GrayscalePixel(255)).value == '9')
  }

  test("LinearAsciiConverter throws error for empty table") {
    assertThrows[IllegalArgumentException] {
      new LinearAsciiConverter("")
    }
  }
}