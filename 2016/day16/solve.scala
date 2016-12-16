import scala.collection.mutable

case object Day16 extends App {

  def encode(st: String, length: Int): String = {
    if (st.length >= length)
      st.substring(0, length)
    else {
      val a = st
      val b = st.reverse.map {
        case '0' => '1'
        case '1' => '0'
      }
      encode(
        new mutable.StringBuilder()
          .append(a)
          .append('0')
          .appendAll(b).mkString,
        length)
    }
  }

  def checksum(st: String): String =
    if (st.length % 2 == 1)
      st
    else
      checksum((0 until st.length / 2).map(i => if (st(2 * i) == st(2 * i + 1)) "1" else "0").mkString)

  def solve(input: String, length: Int): String = checksum(encode(input, length))

  def solve1(st: String): String = solve(st, 272)

  def solve2(st: String): String = solve(st, 35651584)

  val input = "01111010110010011"
  println(solve1(input))
  println(solve2(input))
}
