import scala.annotation.tailrec
import scala.io.Source

case object Day02 extends App {

  class Map(st: String) {
    private val map = st.split("\\|")
    val ccol: Int = map(0).length
    val crow: Int = map.length

    def isValid(irow: Int, icol: Int): Boolean =
      irow >= 0 && irow < crow && icol >= 0 && icol < ccol && map(icol)(irow) != ' '

    def get(irow: Int, icol: Int): String = map(irow)(icol).toString
  }

  case class Player(map: Map, irow: Int, icol: Int) {

    def step(ch: Char): Player = {
      val (drow, dcol) = ch match {
        case 'U' => (-1, 0)
        case 'L' => (0, -1)
        case 'R' => (0, 1)
        case 'D' => (1, 0)
      }

      val (icolNew, irowNew) = (icol + dcol, irow + drow)
      if (map.isValid(irowNew, icolNew))
        copy(irow = irowNew, icol = icolNew)
      else
        this
    }

    def go(steps: String): Player = {
      if (steps.length == 0)
        this
      else
        step(steps(0)).go(steps.substring(1))
    }

    def pos: String = map.get(irow, icol)
  }

  def solve(map: Map, lines: Array[String]) = {
    val playerStart = Player(map, map.ccol / 2, map.crow / 2)
    val (_, code) = lines.foldLeft((playerStart, "")) {
      case ((player, codeT), steps) =>
        val playerNext = player.go(steps)
        (playerNext, codeT + playerNext.pos)
    }
    code
  }

  var input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray
  println(solve(new Map("123|456|789"), input))
  println(solve(new Map("  1  | 234 |56789| ABC |  D  "), input))
}