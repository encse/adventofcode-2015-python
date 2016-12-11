import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Queue
import scala.io.Source
import scala.util.matching.Regex
trait Item {def kind: Int}
case class Generator(kind:Int) extends Item
case class Microchip(kind:Int) extends Item

case class Level(ilevel:Int, generators:Int, microchips:Int) {
  def addGenerator(generator: Generator): Level =
    Level(ilevel, generators + generator.kind, microchips)

  def removeGenerator(generator: Generator): Level =
    Level(ilevel, generators & ~generator.kind, microchips)

  def addMicrochip(microchip: Microchip): Level =
    Level(ilevel, generators, microchips | microchip.kind)

  def removeMicrochip(microchip: Microchip): Level =
    Level(ilevel, generators, microchips & ~microchip.kind)

  def valid = {
    val fedettChipek = microchips & generators
    val nemFedettChipek = microchips - fedettChipek
    val nemFedettGeneratorok = generators - fedettChipek
    nemFedettChipek == 0 || generators == 0
  }


  def allItems(): List[Item] = {
    var res: List[Item] = Nil
    var i = 1
    while (i <= generators || i <= microchips) {
      if ((generators & i) != 0)
        res = Generator(i) :: res
      if ((microchips & i) != 0)
        res = Microchip(i) :: res
      i *= 2
    }
    res
  }

  def isEmpty = generators == 0 && microchips == 0

}

case class Building(levels: Array[Level], elevator:Int) {
  override def toString: String = {
    val stLevels = levels.map(items => items.toString).mkString("\n")
    s"$stLevels\nElevator: $elevator"
  }

  def adjustLevels(ilevelA: Int, levelA: Level, ilevelB: Int, levelB: Level): Array[Level] = {
    val newLevels = levels.clone()
    newLevels(ilevelA) = levelA
    newLevels(ilevelB) = levelB
    newLevels
  }

  def move(items: List[Item], elevatorSrc: Int, elevatorDst: Int): Option[Building] = {
    if (elevatorDst < 0 || elevatorDst >= levels.length)
      None
    else {
      var levelSrc = levels(elevatorSrc)
      var levelDst = levels(elevatorDst)
      for(item <- items) {
        item match {
          case generator@Generator(_) =>
            levelSrc = levelSrc.removeGenerator(generator)
            levelDst = levelDst.addGenerator(generator)

          case microchip@Microchip(_) =>
            levelSrc = levelSrc.removeMicrochip(microchip)
            levelDst = levelDst.addMicrochip(microchip)
        }
      }
      if (levelSrc.valid && levelDst.valid)
        Some(Building(adjustLevels(elevatorSrc, levelSrc, elevatorDst, levelDst), elevatorDst))
      else
        None
    }
  }

  def steps(): List[Building] = {
    val single = for {
        item <- levels(elevator).allItems()
        elevatorDst <- List(elevator + 1, elevator - 1)
        optionalBuilding = move(List(item), elevator, elevatorDst)
        if optionalBuilding.isDefined
      } yield optionalBuilding.get

    val double = for {
      itemA <- levels(elevator).allItems()
      itemB <- levels(elevator).allItems()
      if itemA != itemB
      elevatorDst <- List(elevator + 1, elevator - 1)
      optionalBuilding = move(List(itemA, itemB), elevator, elevatorDst)
      if optionalBuilding.isDefined
    } yield optionalBuilding.get

    single ::: double
  }

  def key():BigInt = {
    var res = 0
    for(level <- levels){
      res = res * 1000000 + (level.generators * 1000 + level.microchips)
    }
    res * 1000000 + elevator
  }

}

case object Day11 extends App {

  val regexMicrochip = """([^ ]*)-compatible""".r
  val regexGenerator = """([^ ]*) generator""".r

  def parse(levelDescriptions:Array[String]):Building = {
    var nextMask = 1
    var kindToMask: Map[String,Int] = Map()
    def ensureKind(kind:String): Int = {
      if (!kindToMask.contains(kind)) {
        kindToMask += ((kind, nextMask))
        nextMask = nextMask << 1
      }
      kindToMask(kind)
    }

    val levels: ArrayBuffer[Level] = ArrayBuffer()
    for(levelDescription <- levelDescriptions) {
      val microchipKinds = regexMicrochip.findAllMatchIn(levelDescription).map(m => m.group(1))
      val generatorKinds = regexGenerator.findAllMatchIn(levelDescription).map(m => m.group(1))

      val microchips = microchipKinds.foldLeft(0) {
        case (res, kind) => res | ensureKind(kind)
      }

      val generators = generatorKinds.foldLeft(0) {
        case (res, kind) => res | ensureKind(kind)
      }

      levels.append(Level(levels.length, generators, microchips))
    }
    Building(levels.toArray, 0)
  }

  def solve1(input:Array[String]): Int = {
    val x= parse(input)
    println(x)
    val queue = Queue((0, x))
    var seen:Set[BigInt] = Set()
    while(queue.nonEmpty){
      val (steps, building) = queue.dequeue()
      seen += building.key()
//      println("----------------")
//      println(building)
//      println()
      if (building.levels.forall(level => level.ilevel == building.levels.length -1 || level.isEmpty)) {
        return steps
      }
      for (buildingNew <- building.steps()) {
        if(!seen.contains(buildingNew.key())) {
          seen += buildingNew.key()
        //  println(buildingNew)
         // println()
          queue.enqueue((steps + 1, buildingNew))
        }
      }
    }
    return -1
  }

  var input = Source.fromFile(productPrefix.toLowerCase + "/input2.in").getLines().toArray
  println(solve1(input))
}
