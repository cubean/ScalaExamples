# Scala Examples

## String operation

### intern()

```scala
tags = if (arr.length >= 6) Some(arr(5).intern()) else None)
```

Document:

```
java.lang.String
@org.jetbrains.annotations.NotNull 
public String intern()
Returns a canonical representation for the string object.
A pool of strings, initially empty, is maintained privately by the class String.
When the intern method is invoked, if the pool already contains a string equal to this String object as determined by the equals(Object) method, then the string from the pool is returned. Otherwise, this String object is added to the pool and a reference to this String object is returned.
It follows that for any two strings s and t, s.intern() == t.intern() is true if and only if s.equals(t) is true.
All literal strings and string-valued constant expressions are interned. String literals are defined in section 3.10.5 of the The Java™ Language Specification.
Returns:
a string that has the same contents as this string, but is guaranteed to be from a pool of unique strings.
```

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

## scala.collection.concurrent.Map

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