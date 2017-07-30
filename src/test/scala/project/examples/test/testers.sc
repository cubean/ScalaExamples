import scala.concurrent.forkjoin.{ForkJoinPool, ForkJoinTask, ForkJoinWorkerThread, RecursiveTask}
import scala.util.DynamicVariable

val forkJoinPool = new ForkJoinPool

abstract class TaskScheduler {
  def schedule[T](body: => T): ForkJoinTask[T]
  def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
    val right = task {
      taskB
    }
    val left = taskA
    (left, right.join())
  }
}

class DefaultTaskScheduler extends TaskScheduler {
  def schedule[T](body: => T): ForkJoinTask[T] = {
    val t = new RecursiveTask[T] {
      def compute = body
    }
    Thread.currentThread match {
      case wt: ForkJoinWorkerThread =>
        t.fork()
      case _ =>
        forkJoinPool.execute(t)
    }
    t
  }
}

val scheduler =
  new DynamicVariable[TaskScheduler](new DefaultTaskScheduler)

def task[T](body: => T): ForkJoinTask[T] = {
  scheduler.value.schedule(body)
}

def parallel[A](tasks: (() => A)*): Seq[A] = {
  if (tasks.isEmpty) Nil
  else {
    val pendingTasks = tasks.tail.map(t => task {
      t()
    })
    tasks.head() +: pendingTasks.map(_.join())
  }
}


def longTask() = {
  println("starting longTask execution")
  Thread.sleep(1000)
  42 + Math.random
}

// Use a list of tasks:
val tasks = List(longTask _, longTask _, longTask _, longTask _)
val results = parallel(tasks: _*)
println(results)


val start = System.currentTimeMillis()

// or pass any number of individual tasks directly:
println(parallel(longTask, longTask, longTask))
println(parallel(longTask, longTask))
println(parallel(longTask))
println(parallel())

println(s"Done in ${System.currentTimeMillis() - start} ms")
