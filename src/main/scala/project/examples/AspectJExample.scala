package project.examples

import project.examples.advice.TimeLogger

/**
  * Created by Cubean Liu on 24/4/17.
  */
class AspectJExample {
  @TimeLogger
  def checkSum(a: Int, b: Int): Int = a + b
}
