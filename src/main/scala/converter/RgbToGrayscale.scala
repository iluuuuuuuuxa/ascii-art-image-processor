package converter

import image.pixel.{RGBPixel, GrayscalePixel}
import image.Image

/**
 * Transforms an RGB image into Grayscale using the standard luminance formula:
 * value = 0.3*R + 0.59*G + 0.11*B.
 */
class RgbToGrayscale extends ImageConverter[RGBPixel, GrayscalePixel] {
  override def convert(image: Image[RGBPixel]): Image[GrayscalePixel] = {
    image.map { pixel =>
      val grayValue = (0.3 * pixel.r) + (0.59 * pixel.g) + (0.11 * pixel.b)
      GrayscalePixel(grayValue.toInt)
    }
  }
}