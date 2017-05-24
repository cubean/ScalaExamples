package project.examples

/**
  * Created by Cubean Liu on 24/3/17.
  */
object MainApp {
  def main(args: Array[String]): Unit = {
    println("Welcome to cubean's Scala examples!")

    //    ExampleLib add LazyValObj
    //    ExampleLib add ImplicitObj
    //    ExampleLib add ShellCmd
    //    ExampleLib add SprayJson
    //    ExampleLib add RecursiveCoin
    ExampleLib add ConcurrentMap

    ExampleLib.allExamples.foreach(c => {
      println(s"\n**${c.getClass.getName}" + "*" * 60)
      c.runAll()
    }
    )

    println("Scala examples show finished")
  }
}
