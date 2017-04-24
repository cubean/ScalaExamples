package project.examples.advice

import org.apache.logging.log4j.LogManager
import org.aspectj.lang.annotation._
import org.aspectj.lang.{JoinPoint, ProceedingJoinPoint}

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
    lg.info(s"-----Method: ${joinPoint.toShortString}")
    if (log.logBefore)
      lg.info(s"-----Input: ${joinPoint.getArgs.mkString(" | ")}")

    val result = joinPoint.proceed
    if (log.logAfter) lg.info(s"-----Result: $result")

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
