package io.input

import image.pixel.RGBPixel
import image.Image

/**
 * Abstraction for any source that can provide an RGB Image.
 * * DESIGN DECISION:
 * This trait is universal. It does not strictly require a File.
 * Implementations can load from Files, Network, or Generate Random noise.
 */
trait ImageLoader {
  def load(): Image[RGBPixel]
}