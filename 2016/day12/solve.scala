import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

case object Day12 extends App {

  val regexCpyNum = """cpy (\d+) ([a-z])""".r
  val regexCpyReg = """cpy ([a-z]) ([a-z])""".r
  val regexJnzNum = """jnz (\d+) (\-?\d+)""".r
  val regexJnzReg = """jnz ([a-z]) (\-?\d+)""".r
  val regexInc = """inc ([a-z])""".r
  val regexDec = """dec ([a-z])""".r

  def solve(input:Array[String], regC:Int): Int = {
    var registers = mutable.Map[String,Int](("ip", 0), ("a", 0), ("b", 0), ("c", regC), ("d", 0))

    while (registers("ip") < input.length) {
      input(registers("ip")) match {
        case regexCpyNum(num, reg) =>
          registers(reg) = num.toInt
          registers("ip") += 1
        case regexCpyReg(regSrc, regDst) =>
          registers(regDst) = registers(regSrc)
          registers("ip") += 1
        case regexJnzNum(num, jmp) =>
          registers("ip") += (if (num.toInt != 0) jmp.toInt else 1)
        case regexJnzReg(reg, jmp) =>
          registers("ip") += (if (registers(reg) != 0) jmp.toInt else 1)
        case regexInc(reg) =>
          registers(reg) += 1
          registers("ip") += 1
        case regexDec(reg) =>
          registers(reg) -= 1
          registers("ip") += 1
      }
    }
    registers("a")
  }

  def solve1(input:Array[String]) = solve(input, 0)
  def solve2(input:Array[String]) = solve(input, 1)

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray
  println(solve1(input))
  println(solve2(input))

}
