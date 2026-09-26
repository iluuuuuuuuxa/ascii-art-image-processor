package io.output

import image.pixel.AsciiPixel
import image.Image

/**
 * Abstraction for any target that can accept an ASCII Image.
 * * DESIGN DECISION:
 * We use 'protected' shared logic here to avoid code duplication 
 * in concrete implementations (DRY Principle).
 */
trait ImageOutput {
  def save(image: Image[AsciiPixel]): Unit

  /**
   * Helper to convert image grid to iterable lines.
   * Allows concrete classes to process line-by-line without 
   * knowing the internal Image structure.
   */
  protected def imageToLines(image: Image[AsciiPixel]): Iterator[String] = {
    Iterator.tabulate(image.height) { y =>
      val sb = new StringBuilder()
      
      for (x <- 0 until image.width) 
      {
        sb.append(image.getPixel(x, y).value)
      }
      
      sb.toString()
    }
  }
}