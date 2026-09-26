package io.input

import image.pixel.RGBPixel
import image.Image
import scala.util.Random

/**
 * Generates an image composed of random noise.
 * Useful for testing or creating abstract art.
 *
 * @param seed Optional seed for the Random Number Generator.
 *             Allows for **Deterministic Testing** (the same seed produces the same image).
 */
class RandomImageLoader(seed: Option[Long] = None) extends ImageLoader {
  override def load(): Image[RGBPixel] = {
    val rng = seed.map(s => new Random(s)).getOrElse(new Random())

    val width = rng.nextInt(300) + 100
    val height = rng.nextInt(300) + 100

    val grid = Vector.tabulate(height, width) { (_, _) =>
      RGBPixel(
        rng.nextInt(256),
        rng.nextInt(256),
        rng.nextInt(256)
      )
    }

    Image(grid)
  }
}