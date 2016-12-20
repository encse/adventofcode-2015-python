import scala.io.Source

case object Day20 extends App {

  val pattern = """(\d+)-(\d+)""".r

  def solve1(lines: Array[String]): Long = {
    val ranges = lines.map { case pattern(stFrom, stTo) => (stFrom.toLong, stTo.toLong) }
    def foo(ip:Long): Long = {
      val range = ranges.find{case (from,to) => from <= ip && ip <= to}
      range match {
        case None => ip
        case Some((_, to)) => foo(to + 1)
      }
    }
    foo(0)
  }
  def solve2(lines: Array[String]): Long = {
    val ranges = lines
      .map { case pattern(stFrom, stTo) => (stFrom.toLong, stTo.toLong) }
      .sortBy(p => p._2)
      .sortBy(p => p._1)
    var res = 0L
    var ip = 0L
    for(i <- ranges.indices){
      if (ip < ranges(i)._1) {
        res += ranges(i)._1 - ip - 1
      }
      ip = Math.max(ip, Math.max(ranges(i)._1, ranges(i)._2))
    }
   if(ip < 4294967295L)
      res += 4294967295L - ip
    res
  }
  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray

  println(solve1(input))
  println(solve2(input))
}
