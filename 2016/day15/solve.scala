import scala.io.Source

case object Day15 extends App {

  val pattern = """Disc #\d+ has (\d+) positions; at time=0, it is at position (\d+).""".r

  def solve1(input:List[String]):Int = {
    val rgTimePosMod = input.zipWithIndex.map{case (pattern(stNumPos, stPos), i) => (i, stPos.toInt, stNumPos.toInt)}.toArray
    var x=0
    while(true){
      if (rgTimePosMod.forall{case (time, pos, mod) => (time+x+1+pos)% mod == 0})
        return x
      x+=1
    }
    -1
  }
  def solve2(input:List[String]):Int =
    solve1(input :+ s"Disc #${input.length+1} has 11 positions; at time=0, it is at position 0.")

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toList

  println(solve1(input))
  println(solve2(input))
}
