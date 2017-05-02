package project.examples

/**
  * Created by cubean liu on 24/3/17.
  */

object ImplicitObj {

  implicit class Crossable[X](xs: Traversable[X]) {
    def cross[Y](ys: Traversable[Y]): Traversable[(X, Y)] = for {x <- xs; y <- ys} yield (x, y)
  }

  implicit class StringHandler(str: String) {
    def myPrint(): Unit = println(">>> " + str)
  }

  //implicit def myPrint(str: String): Unit = println(">>> " + str)

  val xs = Seq(1, 2)
  val ys = List("hello", "world", "bye")

  def getResult: Traversable[(Int, String)] = {
    println(this.getClass.getName)
    xs cross ys
  }

  def tryImplicitFunc(str: String): Unit = str.myPrint()

  def runAll(): Unit = {
    println("////////////////ImplicitObj////////////////")
    println(s"\n >>>>>ImplicitObj Result: ${ImplicitObj.getResult}")

    tryImplicitFunc("My test for Implicit.")
  }
}