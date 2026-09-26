package filters

import image.pixel.GrayscalePixel
import image.Image

/**
 * Increases or decreases the brightness of the image.
 * Uses clamping to ensure values never exceed the 0-255 range.
 */
class BrightnessFilter(change: Int) extends ImageFilter[GrayscalePixel] {
  override def filter(image: Image[GrayscalePixel]): Image[GrayscalePixel] = {
    image.map { p =>
      val newValue = p.value + change
      val clipped = math.min(255, math.max(0, newValue))
      GrayscalePixel(clipped)
    }
  }
}