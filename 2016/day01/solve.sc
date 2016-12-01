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

def solve(steps:List[Step]) = {
  val player = steps.foldLeft(Player(0, 0, Dir.N))(_.go(_))
  player.distance
}

def solve2(steps:List[Step]): Int = {

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

  solveI(steps.flatMap(flatten), Set((0, 0)), Player(0, 0, Dir.N))
}

var input = io.Source.stdin.getLines.mkString

val steps = for(st <- input.split(", ").toList)
  yield Step(Move.withName(st.substring(0, 1)), st.substring(1).toInt)

println(solve(steps))
println(solve2(steps))
