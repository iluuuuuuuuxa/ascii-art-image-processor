package converter

import image.pixel.{GrayscalePixel, AsciiPixel}
import image.Image

/**
 * A specific converter type that handles the final step of the pipeline:
 * Turning Grayscale intensity values into printable ASCII characters.
 */
trait AsciiConverter extends ImageConverter[GrayscalePixel, AsciiPixel] {
  def charFromGray(gray: GrayscalePixel): AsciiPixel

  override def convert(image: Image[GrayscalePixel]): Image[AsciiPixel] = {
    image.map(pixel => charFromGray(pixel))
  }
}