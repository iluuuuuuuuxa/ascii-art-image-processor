package filters

import image.pixel.GrayscalePixel
import image.Image

/**
 * Inverts the colors of the image (creates a "Negative" effect).
 * Formula: NewValue = 255 - OldValue.
 */
class InvertFilter extends ImageFilter[GrayscalePixel] {
  override def filter(image: Image[GrayscalePixel]): Image[GrayscalePixel] = {
    image.map(p => GrayscalePixel(255 - p.value))
  }
}