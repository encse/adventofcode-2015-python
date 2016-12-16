import scala.io.Source

case object Day16 extends App {

  def solve(input:String, length:Int):String = {
    var res = input
    while (res.length < length){
      val a = res
      val b = res.reverse.map{
        case '0' => 1
        case '1' => 0
      }.mkString
      res = new StringBuilder(a).append('0').append(b).mkString
    }
    res = res.substring(0, length)
    while(res.length % 2 == 0){
      var checksum:StringBuilder = new StringBuilder
      for(i <- 0 until res.length/2){
        checksum.append((if (res(2*i) == res(2*i+1)) "1" else "0"))
      }
      res = checksum.mkString
    }
    res
  }
  def solve1(st:String): String = solve(st, 272)
  def solve2(st:String): String = solve(st, 35651584)

  val input = "01111010110010011"
  println(solve1(input))
  println(solve2(input))
}
