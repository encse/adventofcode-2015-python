def solveI(input:String, prefix:String, secondPhase:Boolean): String = {
  var res = Array.fill(8)('_')
  println(res.mkString)

  var i = 0
  var charsFound = 0
  val md5 = java.security.MessageDigest.getInstance("MD5")
  while (charsFound < 8) {
    val hash = md5
      .digest((input + i.toString).getBytes())
      .foldLeft(""){(res, b) => res + "%02x".format(b)}

    val pos = if (secondPhase) hash(5) - '0' else charsFound
    val ch =  if (secondPhase) hash(6) else hash(5)

    if (hash.startsWith(prefix) && pos < 8 && res(pos) == '_') {
      res(pos) = ch
      charsFound += 1
      println(res.mkString)
    }
    i += 1
  }
  res.mkString
}
def solve1(input:String): String = solveI(input, "00000", secondPhase = false)
def solve2(input:String): String = solveI(input, "00000", secondPhase = true)

val input = "reyedfim"
solve1(input)
solve2(input)
