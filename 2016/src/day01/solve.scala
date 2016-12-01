import scala.annotation.tailrec

object Dir extends Enumeration {
  val N, E, S, W = Value
}

object Move extends Enumeration {
  val F = Value(0, "F")
  val R = Value(1, "R")
  val L = Value(3, "L")
}

case class Step(turn:Move.Value, distance:Int) {}

case object Step {
  def parse(st:String) = Step(
    Move.withName(st.substring(0, 1)),
    st.substring(1).toInt
  )
}

case class Player(x:Int, y:Int, dir:Dir.Value) {
  def go(step: Step): Player = {
    val newDir = Dir((dir.id + step.turn.id) % 4)
    val (dx, dy) = newDir match {
      case Dir.N => (0, -1)
      case Dir.E => (1, 0)
      case Dir.S => (0, 1)
      case Dir.W => (-1, 0)
    }
    Player(x + step.distance * dx, y + step.distance * dy, newDir)
  }
  def distance = Math.abs(x) + Math.abs(y)

}

def solve(st:String) = {
  val steps = st.split(", ").map(Step.parse)
  val player = steps.foldLeft(Player(0, 0, Dir.N))(_.go(_))
  player.distance
}

def solve2(st:String): Int = {

  @tailrec
  def solveI(steps: List[Step], seen: Set[(Int, Int)], pCurrent: Player): Int = steps match {
    case Nil => -1
    case step :: rest =>
      val p = pCurrent.go(step)
      if (seen contains ((p.x, p.y)))
        p.distance
      else
        solveI(rest, seen + ((p.x, p.y)), p)
  }

  def flatten(step: Step) = Step(step.turn, 1) +: List.fill(step.distance - 1)(Step(Move.F, 1))

  val steps = st
    .split(", ")
    .map(Step.parse)
    .flatMap(flatten)
    .toList

  solveI(steps, Set((0, 0)), Player(0, 0, Dir.N))
}

val input = io.Source.stdin.getLines.mkString
println(solve(input))
println(solve2(input))
