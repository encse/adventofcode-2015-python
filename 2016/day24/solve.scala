import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

case object Day24 extends App {

  case class Pos(irow:Int, icol:Int)
  def getPositions(map:Array[String]):Array[Pos] = {
    var rgposBuffer = mutable.ArrayBuffer[Pos]()
    for(irow <- map.indices){
      for(icol <- map(irow).indices){
        val ch = map(irow)(icol)
        if(ch >= '0' && ch <= '9'){
          rgposBuffer.append(Pos(irow, icol))
        }
      }
    }
    rgposBuffer.toArray.sortBy(pos => map(pos.irow)(pos.icol))
  }

  def neighbours(pos: Pos, map: Array[String]):Iterable[Pos] =
    for{
      dcol <- List(-1, 0, 1)
      drow <- List(-1, 0, 1)
      if Math.abs(dcol) + Math.abs(drow) == 1
      icol = dcol + pos.icol
      irow = drow + pos.irow
      if icol >= 0 && icol < map(0).length && irow >= 0 && irow < map.length && map(irow)(icol) != '#'
    } yield Pos(irow, icol)

  def findPath(posStart: Pos, posEnd: Pos, map: Array[String], cache:mutable.Map[(Pos, Pos), List[Pos]]):List[Pos] = {
    val key = (posStart, posEnd)
    if(cache.contains(key))
      return cache(key)

    val q = mutable.Queue((List(posStart), posStart))
    val seen = mutable.Set(posStart)

    while (q.nonEmpty) {
      val (path, pos) = q.dequeue()
      for (posNeighbour <- neighbours(pos, map)) {
        if (!seen.contains(posNeighbour)) {
          seen.add(posNeighbour)
          if (posNeighbour == posEnd) {
            cache(key) = posNeighbour +: path
            return cache(key)
          }
          q.enqueue((posNeighbour +: path, posNeighbour))
        }
      }
    }
    require(false)
    Nil
  }

  def solve(map: Array[String]): Int = {
    val rgpos = getPositions(map)
    var k = 0
    var dMin = Int.MaxValue
    val cache = mutable.Map[(Pos, Pos), List[Pos]]()

    for(perm <- rgpos.permutations) {
//      k+=1
//      if(k % 100 == 0)
//        print(".")

      var d = 0
      var i = 0
      val seen = mutable.Set[Pos](rgpos(0))
      var posCur = rgpos(0)
      while(seen.size < perm.length){
        var posNext = perm.find(pos => !seen.contains(pos) ).get
        val path = findPath(posCur, posNext, map, cache)
        for(pos <- path){
          if(map(pos.irow)(pos.icol) != '.'){
            seen.add(pos)
          }
        }
        posCur = posNext
        d += path.length - 1
      }

      dMin = Math.min(dMin, d)
    }

    dMin
  }


  def solve2(map: Array[String]): Int = {
    val rgpos = getPositions(map)
    var k = 0
    var dMin = Int.MaxValue
    val cache = mutable.Map[(Pos, Pos), List[Pos]]()

    for(perm <- rgpos.permutations) {
      //      k+=1
      //      if(k % 100 == 0)
      //        print(".")

      var d = 0
      var i = 0
      val seen = mutable.Set[Pos](rgpos(0))
      var posCur = rgpos(0)
      while(seen.size < perm.length){
        var posNext = perm.find(pos => !seen.contains(pos) ).get
        val path = findPath(posCur, posNext, map, cache)
        for(pos <- path){
          if(map(pos.irow)(pos.icol) != '.'){
            seen.add(pos)
          }
        }
        posCur = posNext
        d += path.length - 1
      }
      d += findPath(posCur, rgpos(0), map, cache).length - 1
      dMin = Math.min(dMin, d)
    }

    dMin
  }

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray
  println(solve(input))
  println(solve2(input))

}
