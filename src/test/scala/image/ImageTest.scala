package image

import image.pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite

class ImageTest extends AnyFunSuite {
  test("Create valid image and access pixels") {
    // Arrange
    val data = Vector(
      Vector(GrayscalePixel(10), GrayscalePixel(20)),
      Vector(GrayscalePixel(30), GrayscalePixel(40))
    )

    // Act
    val image = new Image(data)

    // Assert
    assert(image.width == 2)
    assert(image.height == 2)
    assert(image.getPixel(0, 0) == GrayscalePixel(10))
    assert(image.getPixel(1, 1) == GrayscalePixel(40))
  }

  test("Map function transforms all pixels correctly") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(10), GrayscalePixel(20)))
    val image = new Image(data)

    // Act
    val transformed = image.map(p => GrayscalePixel(p.value + 5))

    // Assert
    assert(transformed.getPixel(0, 0).value == 15)
    assert(transformed.getPixel(1, 0).value == 25)
  }

  test("Create 1x1 image") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(255)))

    // Act
    val image = new Image(data)

    // Assert
    assert(image.width == 1)
    assert(image.height == 1)
    assert(image.getPixel(0, 0).value == 255)
  }

  test("Create empty image (0x0)") {
    // Arrange
    val data = Vector.empty[Vector[GrayscalePixel]]

    // Act
    val image = new Image(data)

    // Assert
    assert(image.width == 0)
    assert(image.height == 0)
  }

  test("Get pixel out of bounds throws exception (Negative index)") {
    // Arrange
    val image = new Image(Vector(Vector(GrayscalePixel(0))))

    // Act & Assert
    assertThrows[IndexOutOfBoundsException] {
      image.getPixel(-1, 0)
    }
  }

  test("Get pixel out of bounds throws exception (Index too large)") {
    // Arrange
    val image = new Image(Vector(Vector(GrayscalePixel(0))))

    // Act & Assert
    assertThrows[IndexOutOfBoundsException] {
      image.getPixel(5, 5)
    }
  }

  test("Image companion object accepts generic Lists (Abstraction)") {
    // Arrange
    val listData = List(
      List(GrayscalePixel(1), GrayscalePixel(2)),
      List(GrayscalePixel(3), GrayscalePixel(4))
    )

    // Act
    val image = Image(listData)

    // Assert
    assert(image.width == 2)
    assert(image.height == 2)
    assert(image.getPixel(0, 0).value == 1)
  }

  test("Image is immutable (Map creates new instance)") {
    // Arrange
    val original = new Image(Vector(Vector(GrayscalePixel(10))))

    // Act
    val modified = original.map(p => GrayscalePixel(99))

    // Assert
    assert(original.getPixel(0, 0).value == 10)
    assert(modified.getPixel(0, 0).value == 99)
    assert(original != modified)
  }

  test("Handle rectangular images (Width != Height)") {
    // Arrange
    val data = Vector(Vector(GrayscalePixel(1), GrayscalePixel(2), GrayscalePixel(3)))

    // Act
    val image = new Image(data)

    // Assert
    assert(image.width == 3)
    assert(image.height == 1)
    assert(image.getPixel(2, 0).value == 3)
  }

  test("Get pixel throws exception at exact boundary limit") {
    // Arrange
    val image = new Image(Vector(Vector(GrayscalePixel(10))))

    // Act & Assert
    assertThrows[IndexOutOfBoundsException] {
      image.getPixel(1, 0)
    }
    assertThrows[IndexOutOfBoundsException] {
      image.getPixel(0, 1)
    }
  }
}