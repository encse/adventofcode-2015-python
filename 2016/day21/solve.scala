import scala.io.Source

case object Day21 extends App {

  val patternSwapPositions = """swap position (\d+) with position (\d+)""".r
  val patternSwapLetter = """swap letter (.) with letter (.)""".r
  val patternRotateBasedOnPosition = """rotate based on position of letter (.)""".r
  val patternRotateRight = """rotate right (\d+) steps?""".r
  val patternRotateLeft = """rotate left (\d+) steps?""".r
  val patternReversePostions = """reverse positions (\d+) through (\d+)""".r
  val patternMovePosition = """move position (\d+) to position (\d+)""".r

  def swapPositions(stPassword: Array[Char], posA: Int, posB: Int): Array[Char] = {
    val ch = stPassword(posA)
    stPassword(posA) = stPassword(posB)
    stPassword(posB) = ch
    stPassword
  }

  def swapLetter(stPassword: Array[Char], chA: Char, chB: Char): Array[Char] = {
    stPassword.map {
      case ch if ch == chA => chB
      case ch if ch == chB => chA
      case ch => ch
    }
  }

  def rotateLeft(stPassword: Array[Char], cch: Int): Array[Char] = {
    val cchT = cch % stPassword.length
    stPassword.drop(cchT) ++ stPassword.take(cchT)
  }
  def rotateRight(stPassword: Array[Char], cch: Int): Array[Char] = {
    val cchT = cch % stPassword.length
    stPassword.takeRight(cchT) ++ stPassword.dropRight(cchT)
  }
  def rotateBasedOnPosition(stPassword: Array[Char], ch: Char): Array[Char] = {
    val pos = stPassword.indexOf(ch)
    val cch = (if(pos >= 4) pos + 2 else pos + 1) % stPassword.length
    stPassword.takeRight(cch) ++ stPassword.dropRight(cch)
  }
  def reversePositions(stPassword: Array[Char], ichStart:Int, ichEnd:Int): Array[Char] = {
    val ichLim = ichEnd + 1
    stPassword.take(ichStart) ++ stPassword.slice(ichStart, ichLim).reverse ++ stPassword.drop(ichLim)
  }

  def movePosition(stPassword: Array[Char], ichA:Int, ichB:Int): Array[Char] = {
    val ch = stPassword(ichA)
    val stRemoved = stPassword.take(ichA) ++ stPassword.drop(ichA + 1)
    val ichInsert = if (ichB >= ichA) ichB else ichB
    stRemoved.take(ichInsert) ++ Array(ch) ++ stRemoved.drop(ichInsert)
  }

  def encryptLine(stPassword: Array[Char], line:String): Array[Char] ={
    line match {
      case patternSwapPositions(stA,stB) =>
        swapPositions(stPassword, stA.toInt, stB.toInt)
      case patternSwapLetter(stA,stB) =>
        swapLetter(stPassword, stA(0), stB(0))
      case patternRotateLeft(stCch) =>
        rotateLeft(stPassword, stCch.toInt)
      case patternRotateRight(stCch) =>
        rotateRight(stPassword, stCch.toInt)
      case patternRotateBasedOnPosition(stA) =>
        rotateBasedOnPosition(stPassword, stA(0))
      case patternReversePostions(stA,stB) =>
        reversePositions(stPassword, stA.toInt, stB.toInt)
      case patternMovePosition(stA,stB) =>
        movePosition(stPassword, stA.toInt, stB.toInt)
    }
  }

  def decryptLine(stPassword: Array[Char], line:String): Array[Char] ={
    line match {
      case patternSwapPositions(stA, stB) =>
        swapPositions(stPassword, stA.toInt, stB.toInt)
      case patternSwapLetter(stA, stB) =>
        swapLetter(stPassword, stA(0), stB(0))
      case patternRotateLeft(stCch) =>
        rotateRight(stPassword, stCch.toInt)
      case patternRotateRight(stCch) =>
        rotateLeft(stPassword, stCch.toInt)
      case patternRotateBasedOnPosition(stA) =>
        val pos = stPassword.indices.find(i => rotateBasedOnPosition(rotateLeft(stPassword, i), stA(0)) sameElements stPassword)
        rotateLeft(stPassword, pos.get)
      case patternReversePostions(stA,stB) =>
        reversePositions(stPassword, stA.toInt, stB.toInt)
      case patternMovePosition(stA,stB) =>
        movePosition(stPassword, stB.toInt, stA.toInt)
    }
  }
  def encrypt(stPassword:String, lines:Array[String]): String =
    lines.foldLeft(stPassword.toCharArray)(encryptLine).mkString

  def decrypt(stPassword:String, lines:Array[String]): String =
    lines.reverse.foldLeft(stPassword.toCharArray)(decryptLine).mkString

  def solve1(lines: Array[String]): String = encrypt("abcdefgh", lines)
  def solve2(lines: Array[String]): String = decrypt("fbgdceah", lines)

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray

  println(solve1(input))
  println(solve2(input))
}
