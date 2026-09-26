import controller.ConsoleController

object Main {
  def main(args: Array[String]): Unit = {
    val controller = new ConsoleController()
    controller.run(args)
  }
}