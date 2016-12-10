import scala.io.Source
import scala.util.matching.Regex
case class BotId(v:String) extends AnyVal{
  override def toString: String = v
}
case class Value(v:Int) extends AnyVal{
  override def toString: String = v.toString
}

class Factory() {
  var bots: Map[BotId,Bot] = Map.empty

  def getBot(botId: BotId): Bot = {
    if (!bots.contains(botId)) {
      bots += ((botId, new Bot(botId, None, None, None, None)))
    }
    bots(botId)
  }

  def botGives(botId: BotId, botIdLow: BotId, botIdHi: BotId):Unit = {
    val bot = getBot(botId)
    bot.outputLow = Some(botIdLow)
    bot.outputHi = Some(botIdHi)
  }

  def valueGoesTo(value: Value, botId: BotId): Unit = {
    val bot = getBot(botId)
    bot.addValue(value)
  }

  def step():Boolean = {
    val optionBot = bots.values.find(bot => bot.value2.isDefined)
    optionBot match {
      case None => false
      case Some(bot) => {
        getBot(bot.outputLow.get).addValue(Value(Math.min(bot.value1.get.v, bot.value2.get.v)))
        getBot(bot.outputHi.get).addValue(Value(Math.max(bot.value1.get.v, bot.value2.get.v)))
        bot.value1 = None
        bot.value2 = None
        true
      }
    }

  }

  override def toString: String = {
    bots.values.toArray.sortBy(b => b.botId.v).map(bot => bot.toString).mkString("\n")
  }
}

class Bot(val botId:BotId,
          var outputLow:Option[BotId],
          var outputHi:Option[BotId],
          var value1:Option[Value],
          var value2:Option[Value]) {

  def addValue(value: Value) = {
    require(!value2.isDefined)
    if (value1.isDefined)
      value2 = Some(value)
    else
      value1 = Some(value)
  }
  def tsto[T](option: Option[T]) =
    option match {
      case Some(t) => t.toString
      case None => "none"
    }
  override def toString: String = {

    s"$botId: ${tsto(value1)} -> ${tsto(outputLow)}; ${tsto(value2)} -> ${tsto(outputHi)}"
  }
}


case object Day10 extends App {

  private val regexValueGoesTo: Regex = """value (.+) goes to (.+)""".r
  private val regexBotGives: Regex = """(.+) gives low to (.+) and high to (.+)""".r

  def parse(commands:List[String], factory: Factory):Factory = {
    commands match {
      case Nil => factory
      case command::commandsT => {
        command match {
          case regexValueGoesTo(value, botId) => factory.valueGoesTo(Value(value.toInt), BotId(botId))
          case regexBotGives(botId, botIdLow, botIdHi) => factory.botGives(BotId(botId), BotId(botIdLow), BotId(botIdHi))
        }
        parse(commandsT, factory)
      }
    }
  }

  def solve1(input:List[String]): (Int, String) = {
    var factory = parse(input, new Factory())
    val value17 = Some(Value(17))
    val value61 = Some(Value(61))
  //  println(factory)
    while(true){
      val optionBot = factory.bots.values.find(bot =>
        bot.value2 == value17 && bot.value1 == value61 ||
        bot.value1 == value17 && bot.value2 == value61
      )
      if(optionBot.isDefined) {
        while (factory.step()) {
          ;
        }
        println(factory)
        return (
          factory.getBot(BotId("output 0")).value1.get.v *
            factory.getBot(BotId("output 1")).value1.get.v *
            factory.getBot(BotId("output 2")).value1.get.v,
          optionBot.get.botId.v)
      }
      factory.step()
     // println()
     // println(factory)
    }
    ???
  }

  var input = Source.fromFile(productPrefix.toLowerCase + "/input.in").getLines().toList
  println(solve1(input))
}
