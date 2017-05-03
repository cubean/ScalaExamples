package project.examples

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Cubean Liu on 3/5/17.
  */
trait ExampleBase {
  /*
   * Run all examples
   */
  def runAll(): Unit
}

object ExampleLib {
  def add(example: ExampleBase): Unit = {
    allExamples.append(example)
  }

  val allExamples: ArrayBuffer[ExampleBase] = new ArrayBuffer[ExampleBase]
}
