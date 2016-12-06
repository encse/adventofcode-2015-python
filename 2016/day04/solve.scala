import scala.util.matching.Regex
import scala.io.Source

case object Day04 extends App {

  case class Room(name: String, id: Int, checksum: String) {
    def valid: Boolean = {
      val groups = name.replace("-", "").groupBy(ch => ch)
      val letters = groups.mapValues(v => v.length).toList.sortWith {
        case ((chA, cA), (chB, cB)) => cA > cB || cA == cB && chA <= chB
      }
      letters.take(5).map(a => a._1).mkString == checksum
    }

    def decrypt: String = {
      name.map(ch => if (ch == '-') ' ' else ((ch - 'a' + id) % 26 + 'a').toChar)
    }
  }

  case object Room {
    private val pattern: Regex = """(.*)-(\d+)\[(.*)]""".r

    def parse(st: String): Room = {
      val (id, num, checksum) = st match {
        case pattern(a, b, c) => (a, b, c)
      }
      Room(id, num.toInt, checksum)
    }
  }

  def solve1(rooms: Iterable[Room]) = rooms.filter(_.valid).map(_.id).sum

  def solve2(rooms: Iterable[Room]) =
    rooms.find(room => room.decrypt.contains("northpole")).get.id

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toList
  val rooms = input.map(Room.parse)

  println(solve1(rooms))
  println(solve2(rooms))
}