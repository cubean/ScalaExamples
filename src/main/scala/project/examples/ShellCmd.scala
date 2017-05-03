package project.examples

import scala.sys.process._

/**
  * Created by Cubean Liu on 3/5/17.
  */
object ShellCmd extends ExampleBase {

  val cmd: Seq[String] = Seq("ls", "/tmp", "-al")

  // Suppress all normal output.
  val loggerCmd = ProcessLogger(
    (_: String) => {},
    (_: String) => println)

  override def runAll(): Unit = {
    cmd ! loggerCmd
  }
}
