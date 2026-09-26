package filters

import image.pixel.GrayscalePixel
import image.Image

/**
 * Corrects the aspect ratio distortion caused by non-square console characters.
 */
class FontAspectRatioFilter(ratioStr: String) extends ImageFilter[GrayscalePixel] {
  override def filter(image: Image[GrayscalePixel]): Image[GrayscalePixel] = {
    val parts = ratioStr.split(":")

    if (parts.length != 2)
      throw new IllegalArgumentException("Ratio must be in format w:h (e.g., 1:2)")

    val fontWidth = parts(0).toDouble
    val fontHeight = parts(1).toDouble

    val scaleY = fontWidth / fontHeight

    val newHeight = (image.height * scaleY).toInt

    if (newHeight == 0) return image

    val newGrid = Vector.tabulate(newHeight, image.width) { (y, x) =>
      val srcY = (y / scaleY).toInt
      val safeY = math.min(srcY, image.height - 1)
      image.getPixel(x, safeY)
    }

    Image(newGrid)
  }
}