package project.examples.advice

//import org.apache.logging.log4j.LogManager
import com.typesafe.scalalogging.Logger
import org.aspectj.lang.annotation._
import org.aspectj.lang.{JoinPoint, ProceedingJoinPoint}
import project.examples.util.ProjectDateTime

/**
  * Created by Cubean Liu on 20/4/17.
  */
@Aspect
class TimeLoggerAdvice {

  /**
    * around execution of Log
    */
  @Around("@annotation(timeLog) && execution(* *.*(..))")
  def aroundLog(joinPoint: ProceedingJoinPoint, timeLog: TimeLogger): Object = {

    val lg = getLogger(joinPoint)

    val startTime = ProjectDateTime.now
    val result = joinPoint.proceed
    val endTime = ProjectDateTime.now
    val timeSpan = ProjectDateTime.timeSpan(startTime, endTime)

    lg.trace(s"--Method (timeSpan $timeSpan): ${joinPoint.toShortString}")
    if (timeLog.logBefore)
      lg.trace(s"--Input  ($startTime): ${joinPoint.getArgs.mkString(" | ")}")
    if (timeLog.logAfter) lg.trace(s"--Output ($endTime): $result")

    result
  }

  @AfterThrowing(value = "@annotation(timeLog)", throwing = "ex")
  @throws[Throwable]
  def logAfterThrowingException(joinPoint: JoinPoint, timeLog: TimeLogger, ex: Exception): Unit = {
    getLogger(joinPoint).error(s"-----error occurred when invoking ${joinPoint.toShortString}", ex)
    throw ex
  }

  private def getLogger(joinPoint: JoinPoint) =
    Logger(joinPoint.getTarget.getClass)
//    LogManager.getLogger(joinPoint.getTarget.getClass.getName)
}
