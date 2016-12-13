import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
case class Pos(x:Int, y:Int)

case object Day13 extends App {

  def isWall(pos:Pos, seed:Int): Boolean = {
    if (pos.y < 0 || pos.x < 0)
      return true
    var v = pos.x * pos.x + 3 * pos.x + 2 * pos.x * pos.y + pos.y + pos.y * pos.y + seed
    var s =0
    while (v>0) {
      if ((v & 1) == 1)
        s += 1
      v >>= 1
    }
    (s & 1) == 1
  }

  def steps(pos:Pos, seed:Int):List[Pos] = {
    val foo = List(Pos(pos.x-1, pos.y), Pos(pos.x, pos.y-1), Pos(pos.x+1, pos.y), Pos(pos.x, pos.y+1))
    foo.filter(pos => !isWall(pos, seed))
  }

  def solve1(seed:Int): Int = {
    val seen = mutable.Set[Pos]()
    val q = mutable.Queue((0,Pos(1,1)))
    while(q.nonEmpty) {
      val (distance, pos) = q.dequeue()
      if(pos.x == 31 && pos.y == 39)
        return distance
      for(newPos <- steps(pos, seed)) {
        if(!seen.contains(newPos)) {
          seen.add(newPos)
          q.enqueue((distance + 1, newPos))
        }
      }
    }
    -1
  }
  def solve2(seed:Int): Int = {
    val seen = mutable.Set[Pos]()
    val q = mutable.Queue((0,Pos(1,1)))
    while(q.nonEmpty) {
      val (distance, pos) = q.dequeue()
      if(distance < 50) {
        for(newPos <- steps(pos, seed)) {
          if(!seen.contains(newPos)) {
            seen.add(newPos)
            q.enqueue((distance + 1, newPos))
          }
        }
      }
    }
    seen.size
  }


  println(solve1(1350))
  println(solve2(1350))

}
