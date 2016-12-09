import scala.io.Source
import scala.util.matching.Regex

case object Day09 extends App {

  private val regexMarker: Regex = """\((\d+)x(\d+)\)""".r

  def decompressLength(st: String, acc: Long, ichFirst: Int, ichLim: Int, recursive: Boolean): Long = {
    if (ichFirst == ichLim) {
      acc
    } else if (st(ichFirst) == '(') {
      val marker = st.substring(ichFirst, st.indexOf(')', ichFirst) + 1)

      val (cch, times) = marker match {
        case regexMarker(stCch, stTimes) => (stCch.toInt, stTimes.toInt)
      }
      val ichFirstPattern = ichFirst + marker.length
      val ichLimPattern = ichFirstPattern + cch

      val patternLength = if (recursive) decompressLength(st, 0, ichFirstPattern, ichLimPattern, recursive) else cch
      decompressLength(st, acc + patternLength * times, ichLimPattern, ichLim, recursive)
    }
    else {
      decompressLength(st, acc + 1, ichFirst + 1, ichLim, recursive)
    }
  }

  def solve1(line: String): Long =
    decompressLength(line, 0, 0, line.length, recursive = false)

  def solve2(line: String): Long =
    decompressLength(line, 0, 0, line.length, recursive = true)


  var input = Source.fromFile(productPrefix.toLowerCase + "/input.in").mkString
  println(solve1(input))
  println(solve2(input))
}
