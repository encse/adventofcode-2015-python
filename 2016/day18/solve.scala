
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

case object Day18 extends App {


  def safeCount(line: String):Int =
    line.map(ch => if (ch == '^') 0 else 1).sum

  def nextLine(line: String):String = {
    val sb = new StringBuilder
    for (ich <- line.indices) {
      val left = if (ich == 0) false else line(ich - 1) == '^'
      val center = line(ich) == '^'
      val right = if (ich == line.length - 1) false else line(ich + 1) == '^'

      val trap = (left && center && !right) ||
        (center && right && !left) ||
        (left && !center && !right) ||
        (right && !center && !left)
      sb.append(if (trap) "^" else ".")
    }
    sb.toString
  }


  def solve(line0: String, lineCount:Int): Int = {
    var line = line0
    var t = safeCount(line)
    var cLine = 1
    while(cLine < lineCount){
      line = nextLine(line)
      t += safeCount(line)
      cLine+=1
    }
    t
  }

  def solve1(line:String) = solve(line, 40)
  def solve2(line:String) = solve(line, 400000)

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().mkString

  println(solve1(input))
  println(solve2(input))

}
