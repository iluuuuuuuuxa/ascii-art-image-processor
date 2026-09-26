# ASCII Art Image Processor

## Overview
An extensible command-line application (CLI) developed in Scala that loads images, processes them through a custom filter pipeline, and renders them into high-quality ASCII art. The project demonstrates strong object-oriented design principles, decoupled architecture, and test-driven development.

## Architecture & Core Features
* **Filter Pipeline:** A robust image processing pipeline supporting multiple sequential transformations, including Rotation (`--rotate degrees`), Scaling (`--scale value`), Inversion (`--invert`), Flipping (`--flip x` and/or `--flip y`), Brightness adjustment (`--brightness value`), and Font Aspect Ratio correction (`--font-aspect-ratio x:y`).
* **OOP Design Patterns:** Architected using standard GoF design patterns (e.g., Strategy for interchangeable ASCII mapping algorithms, Factory concepts for image loading, and Decorator-like filter chaining) to ensure decoupled and maintainable components.
* **Immutable Domain Model:** The core `Image` data structure is strictly immutable. All filters and transformations return newly generated instances, preventing unwanted side effects and ensuring high testability.
* **Extensible Conversion Engine:** Supports both linear and non-linear grayscale-to-ASCII mapping algorithms. The system is easily extensible for custom character sets and mapping logic.
* **Flexible I/O:** Safely handles file loading (JPEG, PNG, GIF) via standard APIs, procedural random image generation, and outputs to multiple targets (Console and File).

## Technical Stack
* **Language:** Scala 3
* **Build Tool:** SBT (Scala Build Tool)
* **Testing:** ScalaTest, Mockito (Comprehensive unit test coverage for all modules)
* **Concepts:** Object-Oriented Design (GoF), Immutable Data Structures, CLI Engineering, Image Processing

## How to Build and Run
This project uses `sbt` for compilation and execution.

1. **Run the application:**
You can run the CLI with various arguments to load an image, apply filters sequentially, and define the output.

* Example: `sbt "run --image src/main/resources/test1.jpg --rotate 90 --invert --scale 0.5 --output-console"`
* Alternatively, you can generate a random image: `sbt "run --image-random --table nonlinear --output-file output.txt"`

## Run Unit Tests
To execute the comprehensive ScalaTest suite: `sbt test`

## Disclaimer
*This project was developed as part of the Object-Oriented Programming course at the Faculty of Information Technology, CTU in Prague. The code is provided here primarily for demonstration of software architecture, OOP design patterns, and Scala proficiency.*
