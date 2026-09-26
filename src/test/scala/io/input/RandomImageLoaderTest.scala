package io.input

import org.scalatest.funsuite.AnyFunSuite

class RandomImageLoaderTest extends AnyFunSuite {

  test("Random Loader is deterministic with seed") {
    // Arrange
    val seed = 12345L
    val loader1 = new RandomImageLoader(Some(seed))
    val loader2 = new RandomImageLoader(Some(seed))

    // Act
    val image1 = loader1.load()
    val image2 = loader2.load()

    // Assert
    assert(image1.width == image2.width)
    assert(image1.height == image2.height)
    assert(image1.getPixel(0, 0) == image2.getPixel(0, 0))
  }

  test("Random Loader generates valid RGB pixels") {
    // Arrange
    val loader = new RandomImageLoader(Some(1L))

    // Act
    val image = loader.load()
    val pixel = image.getPixel(0, 0)

    // Assert
    assert(pixel.r >= 0 && pixel.r <= 255)
    assert(pixel.g >= 0 && pixel.g <= 255)
    assert(pixel.b >= 0 && pixel.b <= 255)
  }

  test("Different seeds produce different images") {
    // Arrange
    val loader1 = new RandomImageLoader(Some(111L))
    val loader2 = new RandomImageLoader(Some(999L))

    // Act
    val image1 = loader1.load()
    val image2 = loader2.load()

    val areIdentical = (image1.width == image2.width) &&
      (image1.height == image2.height) &&
      (image1.getPixel(0, 0) == image2.getPixel(0, 0))

    assert(!areIdentical, "Different seeds should generate different images")
  }

  test("Random image dimensions are within reasonable bounds") {
    // Arrange
    val loader = new RandomImageLoader(Some(123L))

    // Act
    val image = loader.load()

    // Assert
    assert(image.width >= 100 && image.width <= 400)
    assert(image.height >= 100 && image.height <= 400)
  }
}