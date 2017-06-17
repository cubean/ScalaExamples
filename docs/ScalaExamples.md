# Scala Examples

## Transformation

### groupBy

```scala
val ages = List(2,52,44,23,17,14,12,82,51,64)

val grouped = ages.groupBy { age =>
      if(age >= 18 && age < 65) "adult"
      else if(age < 18) "child"
      else "senior"
      }
      
//grouped: scala.collection.immutable.Map[String,List[Int]] = Map(senior -> List(82), adult -> List(52, 44, 23, 51, 64), child -> List(2, 17, 14, 12))

```

## Parallel programming

### collection.parallel

Using all cpu cores

```scala
import scala.collection.parallel._

val ds = (1 to 2147483647).par

val cpuCores = Runtime.getRuntime.availableProcessors()
val forkNum = if(cpuCores > 2) cpuCores - 1 else 1

ds.tasksupport = new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(forkNum))

val start = System.currentTimeMillis()
ds.filter( _ % 2 == 0).length
val timeSpan = System.currentTimeMillis() - start

println(timeSpan)
```