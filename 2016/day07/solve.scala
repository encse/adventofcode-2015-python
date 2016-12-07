import scala.io.Source

case object Day07 extends App {
  def supportsTls(st:String):Boolean = {
    var i=0
    var inside = 0
    var foundOutside = false
    var foundInside = false
    while(i<st.length){
      st(i) match {
        case '[' => inside += 1
        case ']' => inside -= 1
        case _ if i >= 3 =>
          if(st(i-1) != '[' && st(i-2) != '[' && st(i-3) != '[' &&
             st(i-1) != ']' && st(i-2) != ']' && st(i-3) != ']' &&
             st(i-1) != st(i) && st(i - 3) == st(i) && st(i - 2) == st(i - 1)) {
            if (inside == 0)
              foundOutside = true
            else
              foundInside = true
          }
        case _ => ;
      }
      i+=1
    }
    foundOutside && !foundInside
  }

  def supportsSsl(st:String):Boolean = {
    var i = 0
    var inside = 0
    var rgab: Set[String] = Set()
    var rgba: Set[String] = Set()
    while (i < st.length) {
      st(i) match {
        case '[' => inside += 1
        case ']' => inside -= 1
        case _ if i >= 2 =>
          if (st(i - 1) != '[' && st(i - 2) != '[' &&
            st(i - 1) != ']' && st(i - 2) != ']' &&
            st(i - 1) != st(i) && st(i - 2) == st(i)) {
            if (inside > 0) {
              rgba += st.substring(i - 2, i)
            } else {
              rgab += st.substring(i - 2, i)
            }
          }
        case _ => ;
      }
      i += 1
    }
    rgab.intersect(rgba.map(st => st.reverse)).size > 0
  }

  def solve1(rgst: List[String]): Int = rgst.count(supportsTls)
  def solve2(rgst: List[String]): Int = rgst.count(supportsSsl)

  var input =  Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toList
  println(solve1(input))
  println(solve2(input))
}
