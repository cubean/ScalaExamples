package project.examples.test

/**
  * Created by Cubean Liu on 24/3/17.
  */

import org.scalatest.FunSuite
import project.examples.MainApp.Sum

class AspectJTester extends FunSuite {

  test("AspectJTester -  should get back the expected result.") {

    println("//////////// AspectJTester start:")


    val sum = new Sum

    assert(sum.checkSum(2, 3) == 5)


    println("//////////// AspectJTester end.")
  }
}
