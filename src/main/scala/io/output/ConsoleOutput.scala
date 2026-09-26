package io.output

import image.pixel.AsciiPixel
import image.Image

/**
 * Implementation of ImageOutput that prints the ASCII art directly to the Standard Output.
 */
class ConsoleOutput extends ImageOutput {
  override def save(image: Image[AsciiPixel]): Unit = {
    imageToLines(image).foreach(println)
  }
}