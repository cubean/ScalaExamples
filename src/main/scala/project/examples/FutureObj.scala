package project.examples

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Cubean Liu on 27/6/17.
  */
object FutureObj extends ExampleBase {

  override def runAll(): Unit = {
    val result = Future {
      println(">> Inner function")
      1024
    }

    result.map(println)
  }
}
