import scala.collection.mutable

class Matcher(val func:String => String) {

  case class CacheObj(long: Option[Char], short: Option[Char])

  private val cache: mutable.Map[String, CacheObj] = mutable.Map()

  private def getMatch(hash: String, l: Int): Option[Char] = {

    val optionI = ((l - 1) until hash.length).find {
      i => (1 until l).forall(k => hash(i - k) == hash(i))
    }

    optionI.map(i => hash(i))
  }

  private def ensureCache(st: String): CacheObj = {
    if (!cache.contains(st)) {
      val hash = func(st)
      cache(st) = CacheObj(long = getMatch(hash, 5), short = getMatch(hash, 3))
    }

    cache(st)
  }

  def longMatch(st: String): Option[Char] = ensureCache(st).long

  def shortMatch(st: String): Option[Char] = ensureCache(st).short
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
    val res = (0 until 2017).foldLeft(st.getBytes()) { (bytes, _) =>
        md5.digest(bytes).flatMap(byte => Array(toHexChar((byte & 0xff) >> 4).toByte, toHexChar(byte & 0xf).toByte))
    }
    res.map(ch => ch.toChar).mkString
  }

  def hasLongMatch(salt:String, ch: Char, iStart: Int, matcher: Matcher): Boolean = {
    (iStart to iStart + 1000).exists { i => matcher.longMatch(salt + i.toString).contains(ch)}
  }

  def solve(salt: String, matcher: Matcher): Int = {
    var keyCount = 0
    var lastKeyIndex = 0
    var i = 0

    while (keyCount < 64) {
      for (ch <- matcher.shortMatch(salt + i.toString) if hasLongMatch(salt, ch, i+1, matcher)) {
        keyCount += 1
        lastKeyIndex = i
      }
      i+=1
    }
    lastKeyIndex
  }

  def solve1(salt:String) = solve(salt, new Matcher(md5))
  def solve2(salt:String) = solve(salt, new Matcher(repeatedMd5))

  println(solve1("jlmsuwbz"))
  println(solve2("jlmsuwbz"))
}
