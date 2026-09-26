package converter

import image.Image

/**
 * A generic abstraction for transforming an image of type A to type B.
 * This decouples the conversion logic from the Image class itself.
 */
trait ImageConverter[A, B] {
  def convert(image: Image[A]): Image[B]
}