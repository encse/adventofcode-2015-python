import scala.annotation.tailrec

case object Day19 extends App {

  class Elf(var id: Int, var elfPrev: Elf, var elfNext: Elf)

  def solve(elfCount: Int, ielfStart: Int, ielfVictimStart: Int, nextVictimCallback: (Elf, Int) => Elf): Int = {

    @tailrec
    def solveRecursive(elf: Elf, elfVictim: Elf, elfCount: Int): Int = {
      if (elfCount == 1) {
        elf.id
      } else {
        elfVictim.elfPrev.elfNext = elfVictim.elfNext
        elfVictim.elfNext.elfPrev = elfVictim.elfPrev
        solveRecursive(elf.elfNext, nextVictimCallback(elfVictim, elfCount - 1), elfCount - 1)
      }
    }

    val elves = (1 to elfCount).map(i => new Elf(i, null, null)).toArray
    for (i <- elves.indices) {
      elves(i).elfNext = elves((i + 1) % elfCount)
      elves(i).elfPrev = elves((i - 1 + elfCount) % elfCount)
    }
    solveRecursive(elves(ielfStart), elves(ielfVictimStart), elfCount)
  }

  def solve1(elfCount: Int): Int = 
    solve(elfCount, 0, 1, (elfVictim, _) =>
          elfVictim.elfNext.elfNext)

  def solve2(elfCount: Int): Int =
    solve(elfCount, 0, elfCount / 2, (elfVictim, elfCount) =>
          if (elfCount % 2 == 1) elfVictim.elfNext else elfVictim.elfNext.elfNext)

  val input = 3017957
  println(solve1(input))
  println(solve2(input))
}
