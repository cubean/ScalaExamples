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
//            println((path :+ x).mkString("[", ", ", "]"))
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


object ParallelCountChange {

  /** Returns the number of ways change can be made from the specified list of
    * coins for the specified amount of money.
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0) 1
    else {

      def cal(money: Int, coins: List[Int], path: List[Int]): Int =
        coins.map { x => {
          val total = (path :+ x).sum

          if (total < money) cal(money, coins.dropWhile(_ < x), path :+ x)
          else if (total == money) {
            //            println((path :+ x).mkString("[", ", ", "]"))
            1
          }
          else 0
        }
        }.sum

      cal(money, coins.sorted, List[Int]())
    }
  }

  type Threshold = (Int, List[Int]) => Boolean

  /** In parallel, counts the number of ways change can be made from the
    * specified list of coins for the specified amount of money.
    */
  def parCountChange(money: Int, coins: List[Int], threshold: Threshold): Int = {
    if (money == 0) 1
    else {

      def cal(m: Int, c: List[Int], path: List[Int]): Int =
        c.map { x => {
          val total = (path :+ x).sum

          if (total < m) cal(m, c.dropWhile(_ < x), path :+ x)
          else if (total == m) {
            //            println((path :+ x).mkString("[", ", ", "]"))
            1
          }
          else 0
        }
        }.sum

      def parCal(m: Int, c: List[Int], path: List[Int]): Int =
        c.par.map { x => {
          val total = (path :+ x).sum

          if (total < m) cal(m, c.dropWhile(_ < x), path :+ x)
          else if (total == m) {
            //            println((path :+ x).mkString("[", ", ", "]"))
            1
          }
          else 0
        }
        }.sum

      if (threshold(money, coins))
        cal(money, coins.sorted, List[Int]())
      else parCal(money, coins.sorted, List[Int]())
    }
  }

  /** Threshold heuristic based on the starting money. */
  def moneyThreshold(startingMoney: Int): Threshold =
    (realValue: Int, _: List[Int]) => if (realValue <= 2 * startingMoney / 3) true else false

  /** Threshold heuristic based on the total number of initial coins. */
  def totalCoinsThreshold(totalCoins: Int): Threshold =
    (_: Int, currentCoins: List[Int]) => if (currentCoins.length <= 2 * totalCoins / 3) true else false


  /** Threshold heuristic based on the starting money and the initial list of coins. */
  def combinedThreshold(startingMoney: Int, allCoins: List[Int]): Threshold = {
    (realValue: Int, currentCoins: List[Int]) =>
      if (realValue * currentCoins.length <= (startingMoney * allCoins.length / 2)) true else false
  }
}

