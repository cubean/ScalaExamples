# Scala Examples (Part 1) - Parallel Programming and Thread Safety


## Parallel programming

### Data Parallel

Using collection.par

```scala
import scala.collection.parallel._

val s1 = 1 to 10

val s2 = s1.par

// Using all cpu cores
val cpuCores = Runtime.getRuntime.availableProcessors()
val forkNum = if(cpuCores > 2) cpuCores - 1 else 1

s2.tasksupport = new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(forkNum))

val s3 = s2.map(_ * 10)

val s5 = s1.reduce(_ - _)

val s4 =
  s2.reduce((a, b) => {
    println(a, b)
    a - b
  })

val start1: Long = System.currentTimeMillis()
s1.count(_ % 2 == 0)
val timeSpan1 = System.currentTimeMillis() - start1

println(timeSpan1)

val start2: Long = System.currentTimeMillis()
s2.count(_ % 2 == 0)
val timeSpan2 = System.currentTimeMillis() - start2

println(timeSpan2)
```
Output result:

```sh
import scala.collection.parallel._

s1: scala.collection.immutable.Range.Inclusive = Range(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

s2: scala.collection.parallel.immutable.ParRange = ParRange(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

cpuCores: Int = 4
forkNum: Int = 3

s2.tasksupport: scala.collection.parallel.TaskSupport = 
   scala.collection.parallel.ForkJoinTaskSupport@50d09fdf

s3: scala.collection.parallel.immutable.ParSeq[Int] = 
   ParVector(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)

s5: Int = -53

(4,5)
(1,2)
(6,7)
			// If forkNum = 2, there will be 2 pairs picked up first. 
			// forkNum = 4 is same with 3. forkNum = 5 is same with 1.
(3,-1) 
(8,9)
(-1,10)

(-1,-11)
(-1,4)
(-5,10)

s4: Int = -15


// To simple operations in collection, even the collection is big, 
// both timespans are almost same and in most situations, parellel operation is slower.

start1: Long = 1501309254876
res0: Int = 5
timeSpan1: Long = 2


start2: Long = 1501309254885
res2: Int = 5
timeSpan2: Long = 3

```
### Task Parallel

#### Using Scala's parallel collections of Functions

```scala
def longTask1 = 1 + 2
def longTask2 = 1 * 2
def longTask3 = 1f / 2

val tasks = Vector(longTask1 _, longTask2 _, longTask3 _)
val results = tasks.par.map(_()).seq
```

Result

```scala
results: scala.collection.immutable.Vector[AnyVal] = Vector(3, 2, 0.5)
```

#### Task Parallel Function in Cousera class

```scala
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

```

## Thread Safety

### synchronization
Mutexes provide ownership semantics. When you enter a mutex, you own it. The most common way of using a mutex in the JVM is by synchronizing on something. In this case, we’ll synchronize on our Person.

In the JVM, you can synchronize on any instance that’s not null.

```scala
class Person(var name: String) {
  def set(changedName: String) {
    this.synchronized {
      name = changedName
    }
  }
}
```

### volatile
With Java 5’s change to the memory model, volatile and synchronized are basically identical except with volatile, nulls are allowed.

synchronized allows for more fine-grained locking. volatile synchronizes on every access.

```scala
class Person(@volatile var name: String) {
  def set(changedName: String) {
    name = changedName
  }
}
```

### AtomicReference
Also in Java 5, a whole raft of low-level concurrency primitives were added. One of them is an AtomicReference class

```scala
import java.util.concurrent.atomic.AtomicReference

class Person(val name: AtomicReference[String]) {
  def set(changedName: String) {
    name.set(changedName)
  }
}
```

### Does this cost anything?
@AtomicReference is the most costly of these two choices since you have to go through method dispatch to access values.

volatile and synchronized are built on top of Java’s built-in monitors. Monitors cost very little if there’s no contention. Since synchronized allows you more fine-grained control over when you synchronize, there will be less contention so synchronized tends to be the cheapest option.

When you enter synchronized points, access volatile references, or deference AtomicReferences, Java forces the processor to flush their cache lines and provide a consistent view of data.

## scala.collection.concurrent.Map

### scala.collection.concurrent.TrieMap

```scala

val his = scala.collection.concurrent.TrieMap[Int, Int]()

def fib(i: Int): Int = {
  if (his.contains(i)) his(i)
  else {
    val r = i match {
      case 0 => 1
      case 1 => 1
      case n => fib(n - 1) + fib(n - 2)
    }
    his += i -> r
    r
  }
}

def sumFib(i: Int): Int = {
  require(i >= 0)
  (0 to i).map(fib).sum
}
```

The scala.collection.concurrent.Map trait is not meant to be mixed-in with an existing mutable Scala Map to obtain a thread-safe version of the map instance. The SynchronizedMap mixin existed for this purpose before 2.11, but is now deprecated.

Currently, Scala has the scala.collection.concurrent.TrieMap implementation for the scala.collection.concurrent.Map interface, but can wrap Java classes as well.

The scala.collection.concurrent.Map, in versions prior to 2.10 known as scala.collection.mutable.ConcurrentMap, interface is used when you:

want to implement your own concurrent, thread-safe Map from scratch
want to wrap an existing Java concurrent map implementation:
E.g:

```scala
import scala.collection._
import scala.collection.convert.decorateAsScala._
import java.util.concurrent.ConcurrentHashMap

val map: concurrent.Map[String, String] = new ConcurrentHashMap().asScala

```
want to write generic code that works concurrent maps, and don't want to commit to a specific implementation:
E.g.:

```scala
import scala.collection._

def foo(map: concurrent.Map[String, String]) = map.putIfAbsent("", "")

foo(new concurrent.TrieMap)
foo(new java.util.concurrent.ConcurrentSkipListMap().asScala)
```
you could implement your own wrapper around a single-threaded mutable map implementation by using synchronized (but you would need to ensure that your program is accessing the mutable map only through this wrapper and never directly).
E.g.:

```scala
class MySynchronizedMap[K, V](private val underlying: mutable.Map[K, V])
extends concurrent.Map[K, V] {
  private val monitor = new AnyRef
  def putIfAbsent(k: K,v: V): Option[String] = monitor.synchronized {
    underlying.get(k) match {
      case s: Some[V] => s
      case None =>
        underlying(k) = v
        None
    }
  }
  def remove(k: K, v: V): Boolean = monitor.synchronized {
    underlying.get(k) match {
      case Some(v0) if v == v0 => underlying.remove(k); true
      case None => false
    }
  }
  // etc.
}
```