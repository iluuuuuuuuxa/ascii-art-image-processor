package image.pixel

/**
 * The common interface for all types of pixels in the application.
 * * DESIGN DECISION:
 * By using a common trait, we achieve Polymorphism.
 * This allows our Image class to be generic (Image[T]) and handle
 * any type of pixel (RGB, Grayscale, or ASCII) uniformly.
 */
trait Pixel

/**
 * Represents a raw color pixel loaded from an image source.
 * Contains helper logic to ensure channel values remain valid (0-255).
 */
case class RGBPixel(r: Int, g: Int, b: Int) extends Pixel {
  def clamp: RGBPixel = {
    def fix(v: Int) = math.min(255, math.max(0, v))

    RGBPixel(fix(r), fix(g), fix(b))
  }
}

/**
 * Represents a pixel containing a single intensity value.
 * This is the intermediate format used for all filter operations.
 */
case class GrayscalePixel(value: Int) extends Pixel

/**
 * Represents the final output unit of the application.
 * Wraps a specific character that will be rendered to the Console or File.
 */
case class AsciiPixel(value: Char) extends Pixel