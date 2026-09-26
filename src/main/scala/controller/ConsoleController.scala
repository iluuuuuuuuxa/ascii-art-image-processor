package controller

import image.pixel.GrayscalePixel
import image.Image
import converter.{RgbToGrayscale, LinearAsciiConverter, NonLinearAsciiConverter, AsciiConverter}
import filters.{ScaleFilter, InvertFilter, FlipFilter, FlipAxis, RotateFilter, BrightnessFilter, FontAspectRatioFilter}
import io.input.{ImageIOFileLoader, RandomImageLoader}
import io.output.{ConsoleOutput, FileOutput}

/**
 * The main orchestrator of the application.
 *
 * Responsibilities:
 * - Bridges the User Interface (CLI) with the Domain Logic (Images/Filters).
 * - Enforces the "Single Responsibility Principle" by keeping parsing logic out of the Domain models.
 *
 * DESIGN DECISION:
 * We use a dedicated Controller to handle side effects (IO) and parsing,
 * ensuring the core 'Image' class remains pure and testable.
 */
class ConsoleController {
  /**
   * Helper method to safely get the next argument.
   * Encapsulates the logic of checking bounds and validating that we didn't accidentally grab another flag.
   */
  private def getArg(args: Array[String], index: Int, flagName: String): String = {
    if (index + 1 >= args.length)
      throw new IllegalArgumentException(s"Error: Flag '$flagName' requires a value.")

    val value = args(index + 1)

    if (value.startsWith("--"))
      throw new IllegalArgumentException(s"Error: Flag '$flagName' requires a value, but found another flag '$value'.")

    value
  }

  /**
   * Executes the main application pipeline.
   *
   * @param args The command line arguments passed from the Main entry point.
   * @throws IllegalArgumentException If arguments are invalid or the image source is missing.
   */
  def run(args: Array[String]): Unit = {
    if (args.isEmpty)
    {
      println("Error: No arguments provided.")
      return
    }

    try {
      // --- VALIDATION & LOADING ---
      val imageArgCount = args.count(_ == "--image")
      val randomArgCount = args.count(_ == "--image-random")
      val totalImageSources = imageArgCount + randomArgCount

      if (totalImageSources == 0)
        throw new IllegalArgumentException("No image specified. Use --image <path> or --image-random")

      if (totalImageSources > 1)
        throw new IllegalArgumentException(s"Error: Only one image source allowed. You specified $totalImageSources.")

      val rgbImage = if (randomArgCount == 1)
      {
        new RandomImageLoader().load()
      }
      else
      {
        val index = args.indexOf("--image")
        val path = getArg(args, index, "--image")

        new ImageIOFileLoader(args(index + 1)).load()
      }

      println(s"Loaded image: ${rgbImage.width}x${rgbImage.height}")

      var currentImage: Image[GrayscalePixel] = new RgbToGrayscale().convert(rgbImage)

      var asciiTable: AsciiConverter = new LinearAsciiConverter(LinearAsciiConverter.PaulBourke)

      var i = 0

      while (i < args.length)
      {
        val command = args(i)

        command match {
          // --- FILTERS ---
          case "--rotate" =>
            val rawValue = getArg(args, i, "--rotate")

            try {
              val degrees = rawValue.toInt
              currentImage = new RotateFilter[GrayscalePixel](degrees).filter(currentImage)
            } catch {
              case _: NumberFormatException =>
                throw new IllegalArgumentException(s"Invalid rotation value: '$rawValue'. Must be an integer.")
            }
            i += 1

          case "--invert" =>
            currentImage = new InvertFilter().filter(currentImage)

          case "--flip" =>
            val axisStr = getArg(args, i, "--flip")

            FlipAxis.fromString(axisStr) match {
              case Some(axis) =>
                currentImage = new FlipFilter[GrayscalePixel](axis).filter(currentImage)
              case None =>
                throw new IllegalArgumentException(s"Invalid flip axis: '$axisStr'. Use 'x' or 'y'.")
            }
            i += 1

          case "--brightness" =>
            val rawValue = getArg(args, i, "--brightness")

            try {
              currentImage = new BrightnessFilter(rawValue.toInt).filter(currentImage)
            } catch {
              case _: NumberFormatException =>
                throw new IllegalArgumentException(s"Invalid brightness value: '$rawValue'. Must be an integer.")
            }
            i += 1

          case "--scale" =>
            val rawValue = getArg(args, i, "--scale")

            try {
              currentImage = new ScaleFilter(rawValue.toDouble).filter(currentImage)
            } catch {
              case _: NumberFormatException =>
                throw new IllegalArgumentException(s"Invalid scale value: '$rawValue'. Must be a number (e.g. 0.25).")
            }
            i += 1

          case "--font-aspect-ratio" =>
            val ratio = getArg(args, i, "--font-aspect-ratio")
            // The filter itself validates the "w:h" format, so we just let it throw if invalid
            currentImage = new FontAspectRatioFilter(ratio).filter(currentImage)
            i += 1

          // --- TABLES ---
          case "--table" =>
            val name = getArg(args, i, "--table")

            if (name == "bourke")
              asciiTable = new LinearAsciiConverter(LinearAsciiConverter.PaulBourke)
            else if (name == "nonlinear")
              asciiTable = new NonLinearAsciiConverter(NonLinearAsciiConverter.Default)
            else
              throw new IllegalArgumentException(s"Unknown predefined table: '$name'.")
            i += 1

          case "--custom-table" =>
            val tableStr = getArg(args, i, "--custom-table")

            asciiTable = new LinearAsciiConverter(tableStr)
            i += 1

          // --- OUTPUTS ---
          case "--output-console" =>
            val asciiImage = asciiTable.convert(currentImage)
            new ConsoleOutput().save(asciiImage)

          case "--output-file" =>
            val path = getArg(args, i, "--output-file")
            val asciiImage = asciiTable.convert(currentImage)
            new FileOutput(path).save(asciiImage)

            println(s"Saved to $path")
            i += 1

          // --- SKIPS ---
          case "--image" | "--image-random" =>
            if (command == "--image") i += 1

          // --- ERROR HANDLING ---
          case unknown if unknown.startsWith("-") =>
            throw new IllegalArgumentException(s"Unknown argument: '$unknown'.")

          case dangling =>
            throw new IllegalArgumentException(s"Unexpected parameter: '$dangling'. Missing flag?")
        }
        i += 1
      }

    } catch {
      case e: Exception =>
        println(s"Error: ${e.getMessage}")
    }
  }
}