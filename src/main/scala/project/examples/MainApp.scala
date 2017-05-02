package project.examples

/**
  * Created by Cubean Liu on 24/3/17.
  */
object MainApp {
  def main(args: Array[String]): Unit = {
    println("Welcome to cubean's Scala examples!")

    println("////////////////LazyValObj////////////////")
    println(s"\n >>>>>LazyValObj Result: ${LazyValObj.getResult}")

    ImplicitObj.runAll
  }
}
