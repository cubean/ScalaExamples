# ScalaExamples

Some useful Scala examples

- 2017-03-24 ImplicitObj - learning implicit class
- 2017-03-24 LazyValObj - learning executions of val/lazy val/def 


- 2017-04-24 scalatest.FunSuite, Log4j2 and AspectJ 
  - Show scala test, log4j correctly and combined with AOP
  - Log4j2 could output result to both console and log files
  - AOP used AspectJ working with @Log(logBefore = true, logAfter = true)
  - Run $ sbt test
  - Output: 
    - Method (timeSpan 0:0:0:7): execution(AspectJExample.checkSum(..))
    - Input  (2017-04-24T15:45:26.679): 2 | 3
    - Output (2017-04-24T15:45:26.686): 5