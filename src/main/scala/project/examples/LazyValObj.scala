package project.examples

/**
  * Created by cubean liu on 24/3/17.
  */
object LazyValObj extends ExampleBase{
  def expr: Int = {
    val x = {
      print("x")
      1
    }

    lazy val y = {
      print("y")
      2
    }

    def z = {
      print("z")
      3
    }

    z + y + x + z + y + x
  }

  def getResult: Int = expr

  override def runAll(): Unit = {
    println(s"\n >>>>>LazyValObj Result: ${LazyValObj.getResult}")
  }
}
