package filters

import image.Image

/**
 * Reverses the image along a specific axis (X or Y).
 * This transformation is purely structural and does not modify pixel values.
 */
class FlipFilter[T](axis: FlipAxis) extends ImageFilter[T] {
  override def filter(image: Image[T]): Image[T] = {

    axis match {
      case FlipAxis.X =>
        generate(image.width, image.height, (x, y) => image.getPixel(image.width - 1 - x, y))

      case FlipAxis.Y =>
        generate(image.width, image.height, (x, y) => image.getPixel(x, image.height - 1 - y))
    }
  }

  private def generate(w: Int, h: Int, f: (Int, Int) => T): Image[T] = {
    val grid = Vector.tabulate(h, w)((y, x) => f(x, y))
    Image(grid)
  }
}