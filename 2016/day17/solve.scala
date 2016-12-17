import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class State(seed:String, irow:Int, icol:Int, steps:String)

case object Day17 extends App {

  def md5(st: String): String = {
    def toHexString(md5: Array[Byte]): String =
      md5.map(byt => Integer.toHexString((byt & 0xFF) | 0x100).substring(1, 3)).mkString

    toHexString(java.security.MessageDigest.getInstance("MD5").digest(st.getBytes()))
  }

  def doors(state:State):List[Char] = {
    val st = state.seed + state.steps
    val hash = md5(st)
   // println(st, hash)
    val res:ArrayBuffer[Char] = new ArrayBuffer
    if (hash(0)>'a') res.append('U')
    if (hash(1)>'a') res.append('D')
    if (hash(2)>'a') res.append('L')
    if (hash(3)>'a') res.append('R')
    res.toList
  }

  def go(state: State, dir: Char): Option[State] = {
    val (drow, dcol) = dir match {
      case 'U' => (-1,0)
      case 'D' => (1,0)
      case 'L' => (0,-1)
      case 'R' => (0,1)
    }
    val (irow,icol) = (state.irow + drow, state.icol+dcol)
    if (irow >= 0 && irow <4 && icol >= 0 && icol < 4)
      Some(state.copy(irow = irow, icol = icol, steps = state.steps + dir))
    else
      None
  }

  def solve1(seed: String): String = {
    val q = mutable.Queue(State(seed, 0, 0, ""))
    while(q.nonEmpty) {
      val state = q.dequeue()
      // println(state)

      for {
        door <- doors(state)
        newState <- go(state, door)
      }
        if (newState.irow == 3 && newState.icol == 3) {
          return newState.steps
        }
        else {
          q.enqueue(newState)
        }
    }

    ""
  }
  def solve2(seed: String): Int = {
    val q = mutable.Queue(State(seed, 0, 0, ""))
    var res = ""
    while(q.nonEmpty) {
      val state = q.dequeue()
      // println(state)

      for {
        door <- doors(state)
        newState <- go(state, door)
      }
        if (newState.irow == 3 && newState.icol == 3) {
          res = newState.steps
        }
        else {
          q.enqueue(newState)
        }
    }

    res.length
  }

  println(solve1("edjrjqaa"))
  println(solve2("edjrjqaa"))
}
