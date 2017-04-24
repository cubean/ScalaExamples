package project.examples

import project.examples.advice.Log

/**
  * Created by Cubean Liu on 24/4/17.
  */
class AspectJExample {
  @Log(logBefore = true, logAfter = true)
  def checkSum(a: Int, b: Int): Int = a + b
}
