import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

case object Day25 extends App {

  val regexCpy = """cpy (.*) (.*)""".r
  val regexJnz = """jnz (.*) (.*)""".r
  val regexInc = """inc (.*)""".r
  val regexDec = """dec (.*)""".r
  val regexTgl = """tgl (.*)""".r
  val regexAdd = """add (.*) (.*)""".r
  val regexMul = """mul (.*) (.*)""".r
  val regexNop = """nop""".r
  val regexOut = """out (.*)""".r

  class Machine(var registers: mutable.Map[String, Int], var stms: Array[Stm]) {
    var out:String = "";
    def set(reg: Arg, v: Int) = {
      reg match {
        case RegArg(r) => registers(r) = v
        case _ => ;
      }
    }
  }

  trait Stm {
    def run(m: Machine)
  }

  trait Arg {
    def eval(m: Machine): Int
  }

  case class NumArg(num: Int) extends Arg {
    override def eval(m: Machine): Int = num
  }

  case class RegArg(reg: String) extends Arg {
    override def eval(m: Machine): Int = m.registers(reg)
  }

  case class Cpy(src: Arg, dst: Arg) extends Stm {
    override def run(m: Machine) = {
      m.set(dst, src.eval(m))
      m.registers("ip") += 1
    }
  }

  case class Jnz(cond: Arg, addr: Arg) extends Stm {
    override def run(m: Machine) = {
      m.registers("ip") += (if (cond.eval(m) != 0) addr.eval(m) else 1)
    }
  }

  case class Inc(reg: Arg) extends Stm {
    override def run(m: Machine) = {
      m.set(reg, reg.eval(m) + 1)
      m.registers("ip") += 1
    }
  }

  case class Dec(reg: Arg) extends Stm {
    override def run(m: Machine) = {
      m.set(reg, reg.eval(m) - 1)
      m.registers("ip") += 1
    }
  }

  case class Add(dst: Arg, src: Arg) extends Stm {
    override def run(m: Machine) = {
      m.set(dst, dst.eval(m) + src.eval(m))
      m.registers("ip") += 1
    }
  }

  case class Mul(dst: Arg, src: Arg) extends Stm {
    override def run(m: Machine) = {
      m.set(dst, dst.eval(m) * src.eval(m))
      m.registers("ip") += 1
    }
  }

  case class Nop() extends Stm {
    override def run(m: Machine) = {
      m.registers("ip") += 1
    }
  }

  case class Out(v: Arg) extends Stm {
    override def run(m: Machine) = {
      m.out += v.eval(m)
      m.registers("ip") += 1
     // println(m.out)
    }
  }

  case class Tgl(dist: Arg) extends Stm {
    override def run(m: Machine) = {
      val addr = m.registers("ip") + dist.eval(m)
      if (addr >= 0 && addr < m.stms.length) {
        m.stms(addr) = m.stms(addr) match {
          case Inc(arg0) => Dec(arg0)
          case Dec(arg0) => Inc(arg0)
          case Tgl(arg0) => Inc(arg0)
          case Cpy(arg0, arg1) => Jnz(arg0, arg1)
          case Jnz(arg0, arg1) => Cpy(arg0, arg1)
        }
      }
      m.registers("ip") += 1
    }
  }

  def parseArg(arg0: String) = {
    try {
      NumArg(arg0.toInt)
    }
    catch {
      case _: NumberFormatException => RegArg(arg0)
    }
  }

  def solve(input: Array[String], regA:Int): String = {
    val registers = mutable.Map[String, Int](("ip", 0), ("a", regA), ("b", 0), ("c", 0), ("d", 0))
    val stms: Array[Stm] = input.map {
      case regexCpy(arg0, arg1) => Cpy(parseArg(arg0), parseArg(arg1))
      case regexJnz(arg0, arg1) => Jnz(parseArg(arg0), parseArg(arg1))
      case regexInc(arg0) => Inc(parseArg(arg0))
      case regexDec(arg0) => Dec(parseArg(arg0))
      case regexTgl(arg0) => Tgl(parseArg(arg0))
      case regexAdd(arg0, arg1) => Add(parseArg(arg0), parseArg(arg1))
      case regexMul(arg0, arg1) => Mul(parseArg(arg0), parseArg(arg1))
      case regexOut(arg0) => Out(parseArg(arg0))
      case regexNop() => Nop()
    }
    val m = new Machine(registers, stms)

    while(m.out.length < 12) {
      m.stms(m.registers("ip")).run(m)


    }

    m.out
  }

  def solve1(input: Array[String]) = solve(input, 14)
  //def solve2(input: Array[String]) = solve(input, 12)

  val input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toArray
 // println(solve(input, 148))
//  println(solve(input, 1))
//  println(solve(input, 2))
//  println(solve(input, 3))
//  println(solve(input, 4))
//  println(solve(input, 5))
//  println(solve(input, 6))

  for(i <- 1 to 500) {
    var m = solve(input, i)
    if (m == "010101010101")
      println(i)
  }
}
