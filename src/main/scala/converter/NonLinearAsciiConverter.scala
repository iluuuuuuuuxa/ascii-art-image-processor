package converter

import image.pixel.{GrayscalePixel, AsciiPixel}

/**
 * Implements a weighted mapping strategy.
 * Certain characters cover a larger range of the spectrum than others,
 * allowing for higher contrast or specific artistic effects.
 */
class NonLinearAsciiConverter(distribution: Seq[(Int, Char)]) extends AsciiConverter {
  if (distribution.isEmpty || distribution.last._1 < 255)
    throw new IllegalArgumentException("Distribution must end with threshold 255.")

  override def charFromGray(gray: GrayscalePixel): AsciiPixel = {
    distribution
      .find { case (threshold, _) => gray.value <= threshold }
      .map { case (_, char) => AsciiPixel(char) }
      .getOrElse(AsciiPixel(distribution.last._2))
  }
}

object NonLinearAsciiConverter {
  val Default: Seq[(Int, Char)] = Seq(
    (140, '.'),
    (200, '|'),
    (255, '@')
  )
}