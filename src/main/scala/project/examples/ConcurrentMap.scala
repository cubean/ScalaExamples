package project.examples

/**
  * Created by Cubean Liu on 24/5/17.
  */
object ConcurrentMap extends ExampleBase {

  // refer to the document of ScalaExamples.md : scala.collection.concurrent.Map
  val his = new scala.collection.concurrent.TrieMap[Int, Int]()

  private def fib(i: Int): Int = {
    if (his.contains(i)) {
      his(i)
    }
    else {
      //println(">>>>" + i)
      val r = i match {
        case 0 => 1
        case 1 => 1
        case n => fib(n - 1) + fib(n - 2)
      }
      his.put(i, r)
      r
    }
  }

  def sumFib(i: Int): Int = {
    require(i >= 0)
    (0 to i).map(fib).sum
  }

  override def runAll(): Unit = {
    val startTime = System.currentTimeMillis()
    sumFib(10000)
    val endTime = System.currentTimeMillis()

    println(s"Overall time: ${endTime - startTime} ms")
  }
}
