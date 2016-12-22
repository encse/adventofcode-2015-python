import scala.io.Source

case object Day22 extends App {

  val pattern = """.*node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T\s+(\d+)T\s+(\d+)%""".r

  case class Grid(nodes: Array[Array[Node]]) {
    val ccol: Int = nodes(0).length
    val crow: Int = nodes.length

    def apply(irow: Int, icol: Int): Node = {
      nodes(irow)(icol)
    }

    override def toString: String = {
      var st = ""
      for {
        irow <- 0 until crow
        icol <- 0 until ccol
      } {
        val node = this (irow, icol)
        if (node.irow == 0 && node.icol == 0)
          st += "(.)"
        else if (node.irow == 0 && node.icol == ccol - 1)
          st += " G "
        else if (node.size > 300)
          st += " # "
        else if (node.used == 0)
          st += " _ "
        else
          st += " . "

        if (icol == ccol - 1)
          st += "\n"
      }
      st
    }
  }

  case class Node(icol: Int, irow: Int, size: Int, used: Int) {
    require(size >= used)
  }

  def parseGrid(lines: Array[String]): Grid = {
    val nodes = for (line <- lines.drop(2))
      yield line match {
        case pattern(stX, stY, stSize, stUsed, _, _) =>
          Node(stX.toInt, stY.toInt, stSize.toInt, stUsed.toInt)
      }
    val ccol = nodes.maxBy(node => node.icol).icol + 1
    val crow = nodes.maxBy(node => node.irow).irow + 1
    val matrix = Array.ofDim[Node](crow, ccol)
    for (node <- nodes)
      matrix(node.irow)(node.icol) = node

    Grid(matrix)
  }

  def solve1(lines: Array[String]): Int = {
    val grid = parseGrid(lines)
    val viablePairs =
      for {
        icolA <- 0 until grid.ccol
        irowA <- 0 until grid.crow
        icolB <- 0 until grid.ccol
        irowB <- 0 until grid.crow
        nodeA = grid(irowA, icolA)
        nodeB = grid(irowB, icolB)
        if nodeA != nodeB && nodeA.used > 0 && nodeA.used + nodeB.used <= nodeB.size
      } yield (nodeA, nodeB)

    viablePairs.length
  }

  def solve2(lines: Array[String]): Grid = {
    parseGrid(lines)
  }

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray
  println(solve1(input))
  println(solve2(input))

}
