package io.output

import image.pixel.AsciiPixel
import image.Image
import java.io.{File, PrintWriter}

/**
 * Implementation of ImageOutput that persists the ASCII art to a text file.
 * Automatically handles resource management (closing the file writer).
 */
class FileOutput(path: String) extends ImageOutput {
  override def save(image: Image[AsciiPixel]): Unit = {
    val file = new File(path)
    val writer = new PrintWriter(file)

    try {
      imageToLines(image).foreach(writer.println)
    } finally {
      writer.close()
    }
  }
}