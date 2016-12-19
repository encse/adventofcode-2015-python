
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class Elf(var i:Int, var elfPrev: Elf, var elfNext:Elf) {
}

case object Day19 extends App {

  def solve1(m: Int): Int = {

    val rgelf = (1 to m).map(i => new Elf(i, null, null)).toArray
    for (i <- rgelf.indices) {
      rgelf(i).elfNext = rgelf((i + 1) % m)
      rgelf(i).elfPrev = rgelf((i - 1 + m) % m)
    }
    var elf = rgelf(0)
    var celves = m
    while (celves > 1) {
      val elfAldozat = elf.elfNext
      val elfPref = elfAldozat.elfPrev
      val elfNext = elfAldozat.elfNext
      elfPref.elfNext = elfNext
      elfNext.elfPrev = elfPref
      celves -= 1
      elf = elf.elfNext
    }
    elf.i
  }

  def solve2(m: Int): Int = {
    val rgelf = (1 to m).map(i => new Elf(i, null, null)).toArray
    for (i <- rgelf.indices) {
      rgelf(i).elfNext = rgelf((i + 1) % m)
      rgelf(i).elfPrev = rgelf((i - 1 + m) % m)
    }

    var elf = rgelf(0)
    var elfAldozat = rgelf(m / 2)
    var celves = m
    while (celves > 1) {

      val elfPref = elfAldozat.elfPrev
      val elfNext = elfAldozat.elfNext
      elfPref.elfNext = elfNext
      elfNext.elfPrev = elfPref
      celves -= 1
      elf = elf.elfNext
      if(celves % 2 == 1)
        elfAldozat = elfAldozat.elfNext
      else
        elfAldozat = elfAldozat.elfNext.elfNext

    }

    elf.i
  }

  val input = 3017957
  println(solve1(input))
  println(solve2(input))
}
