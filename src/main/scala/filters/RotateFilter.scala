package filters

import image.Image

/**
 * Rotates the image by 90-degree increments.
 * Uses an internal algorithm to map coordinates (x, y) -> (y, height - x).
 */
class RotateFilter[T](degrees: Int) extends ImageFilter[T] {
  private def rotate90(image: Image[T]): Image[T] = {
    val newWidth = image.height
    val newHeight = image.width
    val newGrid = Vector.tabulate(newHeight, newWidth) { (y, x) =>
      image.getPixel(y, image.height - 1 - x)
    }

    Image(newGrid)
  }

  override def filter(image: Image[T]): Image[T] = {
    // Normalize the degrees (handle negative numbers like -90)
    // -90 becomes 270, 450 becomes 90, etc.
    val standardizedDegrees = ((degrees % 360) + 360) % 360

    if (standardizedDegrees % 90 != 0)
      throw new IllegalArgumentException(s"Rotation must be divisible by 90. You provided: $degrees")

    val turns = standardizedDegrees / 90

    var result = image

    for (_ <- 1 to turns)
    {
      result = rotate90(result)
    }

    result
  }
}