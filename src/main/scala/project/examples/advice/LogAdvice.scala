package project.examples.advice

import org.apache.logging.log4j.LogManager
import org.aspectj.lang.annotation._
import org.aspectj.lang.{JoinPoint, ProceedingJoinPoint}
import project.examples.util.ProjectDateTime

/**
  * Created by Cubean Liu on 20/4/17.
  */
@Aspect
class LogAdvice {

  /**
    * around execution of Log
    */
  @Around("@annotation(log) && execution(* *.*(..))")
  def aroundLog(joinPoint: ProceedingJoinPoint, log: Log): Object = {

    val lg = getLogger(joinPoint)

    val startTime = ProjectDateTime.now
    val result = joinPoint.proceed
    val endTime = ProjectDateTime.now
    val timeSpan = ProjectDateTime.timeSpan(startTime, endTime)

    lg.trace(s"--Method (timeSpan $timeSpan): ${joinPoint.toShortString}")
    if (log.logBefore)
      lg.trace(s"--Input  ($startTime): ${joinPoint.getArgs.mkString(" | ")}")
    if (log.logAfter) lg.trace(s"--Output ($endTime): $result")

    result
  }

  @AfterThrowing(value = "@annotation(log)", throwing = "ex")
  @throws[Throwable]
  def logAfterThrowingException(joinPoint: JoinPoint, log: Log, ex: Exception): Unit = {
    getLogger(joinPoint).error(s"-----error occurred when invoking ${joinPoint.toShortString}", ex)
    throw ex
  }

  private def getLogger(joinPoint: JoinPoint) = LogManager.getLogger(joinPoint.getTarget.getClass.getName)
}
