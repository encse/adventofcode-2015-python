def solveI(st:Array[String], key: (Char,Int)=>Int) = {
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
def solve1(st:Array[String]) = solveI(st, (ch, count) => - count)
def solve2(st:Array[String]) = solveI(st, (ch, count) => count)

var input = io.Source.stdin.getLines.toArray
println(solve1(input))
println(solve2(input))
