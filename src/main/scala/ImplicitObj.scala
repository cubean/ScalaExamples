/**
  * Created by cubean liu on 24/3/17.
  */

object ImplicitObj {

  implicit class Crossable[X](xs: Traversable[X]) {
    def cross[Y](ys: Traversable[Y]): Traversable[(X, Y)] = for {x <- xs; y <- ys} yield (x, y)
  }

  val xs = Seq(1, 2)
  val ys = List("hello", "world", "bye")

  def getResult: Traversable[(Int, String)] = {
    xs cross ys
  }
}