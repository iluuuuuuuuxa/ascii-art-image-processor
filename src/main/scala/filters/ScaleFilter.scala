package filters

import image.pixel.GrayscalePixel
import image.Image

/**
 * Resizes the image by a given factor.
 * Uses averaging (for down scaling) to preserve detail and
 * nearest-neighbor (for up scaling) to maintain structure.
 */
class ScaleFilter(scale: Double) extends ImageFilter[GrayscalePixel] {
  override def filter(image: Image[GrayscalePixel]): Image[GrayscalePixel] = {
    val newWidth = (image.width * scale).toInt
    val newHeight = (image.height * scale).toInt

    if (newWidth == 0 || newHeight == 0) return image

    val newGrid = Vector.tabulate(newHeight, newWidth) { (y, x) =>
      if (scale >= 1.0)
      {
        val srcX = (x / scale).toInt
        val srcY = (y / scale).toInt
        image.getPixel(srcX, srcY)
      }
      else
      {
        val blockSize = (1.0 / scale).toInt
        val startX = x * blockSize
        val startY = y * blockSize

        var sum = 0
        var count = 0

        for {
          by <- 0 until blockSize
          bx <- 0 until blockSize
          if (startX + bx) < image.width && (startY + by) < image.height
        } {
          sum += image.getPixel(startX + bx, startY + by).value
          count += 1
        }

        if (count > 0) GrayscalePixel(sum / count) else GrayscalePixel(0)
      }
    }

    Image(newGrid)
  }
}