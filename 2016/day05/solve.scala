import scala.util.matching.Regex
import scala.io.Source

case object Day05 extends App {
  def solveI(input: String, secondPhase: Boolean): String = {
    var res = Array.fill(8)('_')

    var i = 0
    var charsFound = 0
    val md5 = java.security.MessageDigest.getInstance("MD5")
    while (charsFound < 8) {
      val hash = md5
        .digest((input + i.toString).getBytes())

      val pos = if (secondPhase) hash(2) & 0xf else charsFound
      val ch = if (secondPhase) (hash(3) >> 4) & 0xf else hash(2) & 0xf

      if (hash(0) == 0 && hash(1) == 0 && (hash(2) & 0xf0) == 0 && pos < 8 && res(pos) == '_') {
        res(pos) = if (ch < 10) (ch + '0').toChar else ('a' + ch - 10).toChar
        charsFound += 1
      }
      i += 1
    }
    res.mkString
  }

  def solve1(input: String): String = solveI(input, secondPhase = false)

  def solve2(input: String): String = solveI(input, secondPhase = true)

  val input = "reyedfim"
  println(solve1(input))
  println(solve2(input))
}