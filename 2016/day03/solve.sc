def validCount(triplets: Array[Array[Int]]) =
  triplets.map(_.sorted).count(s => s(0) + s(1) > s(2))

def solve1(triplets: Array[Array[Int]]) =
  validCount(triplets)

def solve2(triplets: Array[Array[Int]]) =
  validCount(triplets.grouped(3).toArray.map(_.transpose).flatten)

val input = io.Source.stdin.getLines.toArray
val triplets = input.map(line => line.trim().split("\\s+").map(_.toInt))
println(solve1(triplets))
println(solve2(triplets))
