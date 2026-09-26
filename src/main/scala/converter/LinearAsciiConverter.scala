package converter

import image.pixel.{GrayscalePixel, AsciiPixel}

/**
 * Implements a linear mapping strategy.
 * The 0-255 grayscale range is divided equally among the characters in the table.
 */
class LinearAsciiConverter(table: String) extends AsciiConverter {
  if (table.isEmpty)
    throw new IllegalArgumentException("Custom table cannot be empty.")

  override def charFromGray(gray: GrayscalePixel): AsciiPixel = {
    if (table.length == 1) return AsciiPixel(table.head)

    val index = (gray.value * table.length) / 256

    val safeIndex = math.min(table.length - 1, math.max(0, index))

    AsciiPixel(table(safeIndex))
  }
}

object LinearAsciiConverter {
  val PaulBourke = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. "

  val Default = " .:-=+*#%@"
}