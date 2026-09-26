package image

/**
 * The core Domain Model representing a 2D grid of pixels.
 *
 * DESIGN DECISION:
 * This class uses a PRIVATE internal data structure to ensure **Encapsulation**.
 * The internal representation (Vector) is hidden from the outside world, allowing
 * us to change it later without breaking other modules.
 *
 * It is **Immutable**, meaning transformations return a new instance rather than
 * modifying the current one.
 */
class Image[T](private val data: Vector[Vector[T]]) {
  val height: Int = data.length
  val width: Int = if (height > 0) data.head.length else 0

  def getPixel(x: Int, y: Int): T = {
    if (x < 0 || x >= width || y < 0 || y >= height)
      throw new IndexOutOfBoundsException(s"Pixel ($x, $y) is out of bounds.")

    data(y)(x)
  }

  def map[R](f: T => R): Image[R] = {
    val newData = data.map(row => row.map(pixel => f(pixel)))

    new Image(newData)
  }
}

object Image {
  def apply[T](data: Seq[Seq[T]]): Image[T] = {
      new Image(data.map(_.toVector).toVector)
    }
}