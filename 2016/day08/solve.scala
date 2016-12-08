import scala.io.Source
import scala.util.matching.Regex

class Screen(val ccol:Int, val crow:Int) {
  private var tbl = Array.fill(crow, ccol)(0)

  def rect(ccol:Int, crow:Int): Unit = {
    for {
      irow <- 0 until crow
      icol <- 0 until ccol
    } {
      tbl(irow)(icol) = 1
    }
  }

  def rotateRow(irow:Int, c:Int): Unit =
    tbl(irow) = tbl(irow).takeRight(c) ++ tbl(irow).take(tbl(irow).length - c)


  def rotateCol(icol:Int, c:Int): Unit = {
    tbl = tbl.transpose
    rotateRow(icol, c)
    tbl = tbl.transpose
  }

  def lit: Int = tbl.map(row => row.count(_ == 1)).sum

  override def toString: String =
    tbl.map(row => row.map(x => if (x == 1) '#' else '.').mkString("")).mkString("\n")
}

case object Day08 extends App {
  private val patternRect: Regex = """rect (\d+)x(\d+)""".r
  private val patternRotateRow: Regex = """rotate row y=(\d+) by (\d+)""".r
  private val patternRotateCol: Regex = """rotate column x=(\d+) by (\d+)""".r

  def solve(width:Int, height:Int, rgst: List[String]): Unit = {
    var screen = new Screen(width, height)
    for (st <- rgst) {
      st match {
        case patternRect(ccol, crow) => screen.rect(ccol.toInt, crow.toInt)
        case patternRotateRow(irow, c) => screen.rotateRow(irow.toInt, c.toInt)
        case patternRotateCol(icol, c) => screen.rotateCol(icol.toInt, c.toInt)
      }
    }
    println(screen.lit)
    println(screen)
  }

  var input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toList
  solve(50, 6, input)
}
