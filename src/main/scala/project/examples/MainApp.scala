package project.examples

/**
  * Created by Cubean Liu on 24/3/17.
  */
object MainApp {
  def main(args: Array[String]): Unit = {
    println("Welcome to cubean's Scala examples!")

    println("////////////////LazyValObj////////////////")
    println(s"\n >>>>>LazyValObj Result: ${LazyValObj.getResult}")

    println("////////////////ImplicitObj////////////////")
    println(s"\n >>>>>ImplicitObj Result: ${ImplicitObj.getResult}")
  }


  import project.examples.advice.Log

  class Sum {
    @Log(logBefore = true, logAfter = true)
    def checkSum(a: Int, b: Int): Int = a + b
  }
}
