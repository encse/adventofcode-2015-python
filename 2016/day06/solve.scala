import scala.io.Source

case object Day06 extends App {
  def solveI(st: Array[String], key: (Char, Int) => Int): String = {
    val transposed = st.map(x => x.toCharArray).transpose
    val res = for (line <- transposed)
      yield line
        .groupBy(ch => ch)
        .toArray
        .sortBy(x => key(x._1, x._2.length))
        .head
        ._1

    res.mkString
  }

  def solve1(st: Array[String]): String = solveI(st, (ch, count) => -count)

  def solve2(st: Array[String]): String = solveI(st, (ch, count) => count)

  var input =  Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray
  println(solve1(input))
  println(solve2(input))
}
