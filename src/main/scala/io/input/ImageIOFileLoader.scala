package io.input

import image.pixel.RGBPixel
import image.Image
import java.io.File
import javax.imageio.ImageIO

/**
 * Loads images from the filesystem using the standard Java ImageIO library.
 */
class ImageIOFileLoader(path: String) extends ImageLoader {
  private val supportedExtensions = Set("jpg", "jpeg", "png", "gif")

  override def load(): Image[RGBPixel] = {
    val extension = path.split('.').lastOption.getOrElse("").toLowerCase

    if (!supportedExtensions.contains(extension))
      throw new IllegalArgumentException(s"Error: Unsupported file extension '.$extension'. Supported: ${supportedExtensions.mkString(", ")}")

    val file = new File(path)

    if (!file.exists())
      throw new IllegalArgumentException(s"File not found: $path")

    val bufferedImage = ImageIO.read(file)

    if (bufferedImage == null)
      throw new IllegalArgumentException(s"Format not supported or file broken: $path")

    val width = bufferedImage.getWidth
    val height = bufferedImage.getHeight

    val grid = Vector.tabulate(height, width) { (y, x) =>
      val rgb = bufferedImage.getRGB(x, y)

      val r = (rgb >> 16) & 0xFF
      val g = (rgb >> 8) & 0xFF
      val b = rgb & 0xFF

      RGBPixel(r, g, b)
    }

    Image(grid)
  }
}