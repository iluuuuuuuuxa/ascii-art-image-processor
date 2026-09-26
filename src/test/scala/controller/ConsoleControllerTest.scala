package controller

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.io.{ByteArrayOutputStream, File}

class ConsoleControllerTest extends AnyFunSuite with BeforeAndAfterEach {
  // --- Helper to capture Console output ---
  // The Controller catches exceptions and prints them to stdout.
  // To test effectively, we must capture that output and assert against it.
  def withCapturedOut(block: => Unit): String = {
    val stream = new ByteArrayOutputStream()
    Console.withOut(stream){
      block
    }
    stream.toString
  }

  test("run should print error when arguments are empty") {
    // Arrange
    val controller = new ConsoleController()

    // Act
    val output = withCapturedOut {
      controller.run(Array.empty)
    }

    // Assert
    assert(output.trim.contains("Error: No arguments provided."))
  }

  test("run should print error when unexpected parameter") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "invert")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.trim.contains("Error: Unexpected parameter: 'invert'. Missing flag?"))
  }

  test("run should print error when no image source is specified") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--scale", "0.5")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: No image specified. Use --image <path> or --image-random"))
  }

  test("run should print error when multiple image sources are specified") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--image", "some/path.jpg")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Only one image source allowed"))
  }

  test("run should handle flag missing value (Next arg is flag)") {
    // Arrange
    val controller = new ConsoleController()
    // --brightness requires a value, but we give it another flag
    val args = Array("--image-random", "--brightness", "--invert")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Flag '--brightness' requires a value, but found another flag '--invert'"))
  }

  test("run should handle flag missing value (End of args)") {
    // Arrange
    val controller = new ConsoleController()
    // --rotate requires a value, but we don't provide one
    val args = Array("--image-random", "--rotate")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Flag '--rotate' requires a value"))
  }

  test("run should handle invalid rotation value") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--rotate", "abc")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Invalid rotation value: 'abc'. Must be an integer."))
  }

  test("run should handle invalid brightness value") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--brightness", "abc")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Invalid brightness value: 'abc'. Must be an integer."))
  }

  test("run should handle invalid numeric inputs") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--scale", "not-a-number")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Invalid scale value: 'not-a-number'. Must be a number (e.g. 0.25)."))
  }

  test("run should handle invalid flip axis") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--flip", "z")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Invalid flip axis: 'z'"))
  }

  test("run should handle invalid font aspect ratio format") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--font-aspect-ratio", "1")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Ratio must be in format w:h (e.g., 1:2)"))
  }

  test("run should handle unknown arguments") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--explode-cvut")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Unknown argument: '--explode-cvut'"))
  }

  test("run should handle successful pipeline with Random Image and Console Output") {
    // Arrange
    val controller = new ConsoleController()
    // We use RandomImageLoader because it doesn't require a real file on disk
    val args = Array(
      "--image-random",
      "--rotate", "90",
      "--invert",
      "--scale", "0.25",
      "--output-console"
    )

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    // 1. Check if image loaded
    assert(output.contains("Loaded image"))
    // 2. Since we output to console, the stream should contain ASCII characters
    // We can not know the exact pixels (random), but we ensure no error message was printed
    assert(!output.contains("Error:"))
  }

  test("run should select different ASCII tables correctly") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--table", "nonlinear", "--output-console")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Loaded image"))
    assert(!output.contains("Error:"))
  }

  test("run should handle unknown table") {
    // Arrange
    val controller = new ConsoleController()
    val args = Array("--image-random", "--table", "boom", "--output-console")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains("Error: Unknown predefined table: 'boom'"))
  }

  test("run should handle custom ASCII table") {
    // Arrange
    val controller = new ConsoleController()
    val customChars = "@#"
    val args = Array("--image-random", "--custom-table", customChars, "--output-console")

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    // The output should contain only our custom chars (and newlines)
    // but verifying exact pixel output from a random image is hard.
    // We just ensure the command parsed successfully.
    assert(output.contains("Loaded image"))
    assert(!output.contains("Error:"))
  }

  test("run should save to file successfully") {
    // Arrange
    val controller = new ConsoleController()
    // Create a temporary file path for output
    val tempFile = File.createTempFile("test-output", ".txt")
    tempFile.deleteOnExit() // Clean up after test

    val args = Array(
      "--image-random",
      "--output-file", tempFile.getAbsolutePath
    )

    // Act
    val output = withCapturedOut {
      controller.run(args)
    }

    // Assert
    assert(output.contains(s"Saved to ${tempFile.getAbsolutePath}"))
    assert(tempFile.exists())
    assert(tempFile.length() > 0)
  }
}