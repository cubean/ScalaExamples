package project.examples

/**
  * Created by Cubean Liu on 6/5/17.
  */
object RecursiveCoin extends ExampleBase {

  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) 1
    else {
      //      val allChanges = new ArrayBuffer[List[Int]]()
      var numTotal = 0

      def cal(money: Int, coins: List[Int], accum: List[Int]): Unit =
        coins.foreach { x => {
          val total = (accum :+ x).sum
          if (total < money) cal(money, coins.dropWhile(_ < x), accum :+ x)
          else if (total == money) numTotal += 1 //allChanges.append(accum :+ x)
          else if (total > money) {}
        }
        }


      cal(money, coins.sorted, List[Int]())

      //      allChanges.foreach(l => println(l.mkString("[", ", ", "]")))

      //allChanges.length
      numTotal
    }
  }

  override def runAll(): Unit = {
    println(countChange(4, List(1, 2)))
  }
}
