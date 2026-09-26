package filters

trait FlipAxis

object FlipAxis {
  case object X extends FlipAxis
  case object Y extends FlipAxis

  def fromString(s: String): Option[FlipAxis] = s.toLowerCase match {
    case "x" => Some(X)
    case "y" => Some(Y)
    case _   => None
  }
}