package project.examples.test

/**
  * Created by Cubean Liu on 24/3/17.
  */

import org.scalatest.FunSuite

class AspectJTester extends FunSuite {

  test("AspectJTester -  should get back the expected result.") {

    println("""Please run this test in "sbt test", which doesn't work in IntelliJ test. """)
    println("//////////// AspectJTester start:")

    import project.examples.AspectJExample

    println("!!!!!! Pay more attention: The aspectj class cannot be under test folder!")
    val ase = new AspectJExample

    assert(ase.checkSum(2, 3) == 5)


    println("//////////// AspectJTester end.")
  }
}
