package project.examples

/**
  * Created by Cubean Liu on 6/5/17.
  */
object RecursiveCoin extends ExampleBase {

  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) 1
    else {

      def cal(money: Int, coins: List[Int], path: List[Int]): Int =
        coins.map { x => {
          val total = (path :+ x).sum

          if (total < money) cal(money, coins.dropWhile(_ < x), path :+ x)
          else if (total == money) {
            println((path :+ x).mkString("[", ", ", "]"))
            1
          }
          else 0
        }
        }.sum

      cal(money, coins.sorted, List[Int]())
    }
  }

  override def runAll(): Unit = {
    println(countChange(4, List(1, 2)))
  }
}
