package filters

import image.Image

/**
 * Represents a transformation operation on an image.
 * * @tparam T The type of pixel this filter operates on. 
 * Generic to support both Grayscale and RGB filters.
 */
trait ImageFilter[T] {
  def filter(image: Image[T]): Image[T]
}