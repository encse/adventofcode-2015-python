import scala.collection.mutable

class Matcher(val salt:String, val func:String => String) {

  private val hashCache: mutable.Map[String, String] = mutable.Map()
  private val matchCache: mutable.Map[Char, mutable.Queue[Int]] = mutable.Map()
  var i = 0
  def step(): Option[Int] = {

    for (ch <- getMatch(salt + i.toString, 5)) {
      if (!matchCache.contains(ch))
        matchCache(ch) = mutable.Queue()
      matchCache(ch).enqueue(i)
    }

    for (queue <- matchCache.values) {
      while (queue.nonEmpty && queue.head + 1000 <= i)
        queue.dequeue()
    }

    var res: Option[Int] = None
    if (i >= 1000) {
      for (ch <- getMatch(salt + (i - 1000).toString, 3)) {
        if (matchCache.contains(ch) && matchCache(ch).nonEmpty)
          res = Some(i - 1000)
      }
    }
    i += 1
    res
  }

  private def getMatch(st: String, l: Int): Option[Char] = {
    if(!hashCache.contains(st))
      hashCache(st) = func(st)
    val hash = hashCache(st)
    val optionI = ((l - 1) until hash.length).find {
      i => (1 until l).forall(k => hash(i - k) == hash(i))
    }

    optionI.map(i => hash(i))
  }

}

case object Day14 extends App {

  def md5(st: String): String = {
    def toHexString(md5: Array[Byte]): String =
      md5.map(byt => Integer.toHexString((byt & 0xFF) | 0x100).substring(1, 3)).mkString

    toHexString(java.security.MessageDigest.getInstance("MD5").digest(st.getBytes()))
  }

  def repeatedMd5(st: String): String = {
    def toHexChar(byte: Int): Int =
      if (byte < 10) byte + '0' else (byte - 10) +'a'

    val md5 = java.security.MessageDigest.getInstance("MD5")
    val res = (0 until 2017).foldLeft(st.getBytes()) { (bytes, _) => {
        val hash = md5.digest(bytes)
        var ab = Array.fill(hash.length * 2)(0.toByte)
        for (i <- 0 until hash.length) {
          ab(i * 2) = toHexChar((hash(i) & 0xff) >> 4).toByte
          ab(i * 2 + 1) = toHexChar(hash(i) & 0xf).toByte
        }
        ab
      }
    }
    res.map(ch => ch.toChar).mkString
  }

  def solve(salt: String, matcher: Matcher): Int = {
    val dt = System.currentTimeMillis()
    var keyCount = 0
    var lastKeyIndex = 0

    while (keyCount < 64) {
      for (i <- matcher.step()) {
        keyCount += 1
        lastKeyIndex = i
      }
    }
    println(System.currentTimeMillis() - dt, "ms")
    lastKeyIndex
  }

  def solve1(salt:String): Int = solve(salt, new Matcher(salt, md5))
  def solve2(salt:String): Int = solve(salt, new Matcher(salt, repeatedMd5))

  println(solve1("jlmsuwbz"))
  println(solve2("jlmsuwbz"))
}
