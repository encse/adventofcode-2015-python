import scala.annotation.tailrec

case object Day19 extends App {

  class Elf(var id: Int, var elfPrev: Elf, var elfNext: Elf)

  def solve(ielfStart: Int, ielfVictimStart: Int, celf: Int,
            elfNextVictim: (Elf, Int) => Elf): Int = {

    @tailrec
    def solveRecursive(elf: Elf, elfVictim: Elf, celf: Int): Int = {
      if (celf == 1) {
        elf.id
      } else {
        val elfPrefT = elfVictim.elfPrev
        val elfNextT = elfVictim.elfNext
        elfPrefT.elfNext = elfNextT
        elfNextT.elfPrev = elfPrefT
        solveRecursive(elf.elfNext, elfNextVictim(elfVictim, celf - 1), celf - 1)
      }
    }

    val rgelf = (1 to celf).map(i => new Elf(i, null, null)).toArray
    for (i <- rgelf.indices) {
      rgelf(i).elfNext = rgelf((i + 1) % celf)
      rgelf(i).elfPrev = rgelf((i - 1 + celf) % celf)
    }
    solveRecursive(rgelf(ielfStart), rgelf(ielfVictimStart), celf)
  }

  def solve1(celf: Int): Int = {
    solve(0, 1, celf, (elfVictim, _) =>
      elfVictim.elfNext.elfNext
    )
  }

  def solve2(celf: Int): Int = {
    solve(0, celf / 2, celf, (elfVictim, celf) =>
      if (celf % 2 == 1) elfVictim.elfNext else elfVictim.elfNext.elfNext
    )
  }

  val input = 3017957
  println(solve1(input))
  println(solve2(input))
}
