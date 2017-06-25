# Development Environment

## Install Spark 2.0.1

assume Java, Scala and Homebrew are installed.

```sh
$ brew install wget
$ wget http://d3kbcqa49mib13.cloudfront.net/spark-2.0.1-bin-hadoop2.7.tgz
$ tar -xvzf spark-2.0.1-bin-hadoop2.7.tgz
$ mv  spark-2.0.1-bin-hadoop2.7 /usr/local/spark
$ rm spark-2.0.1-bin-hadoop2.7.tgz
```

### test

```sh
$ cd /usr/local/spark
$ ./bin/spark-shell # spark shell
```
quit spark shell by :quit

```sh
$ ./bin/run-example org.apache.spark.examples.SparkPi
```

Edit host name in /etc/hosts if the following error is encountered (source)

```
host name can be identified by typing hostname in terminal
```

```
$ sudo nano /etc/hosts
```
edit [127.0.0.1      localhost] into [#127.0.0.1      Macbook-Pro]

## RDD

### Pair RDDs operations
[Spark API page for PariRDDFunctions (ScalaDoc)]
(http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.PairRDDFunctions)

(But not available on regular RDDs)

Transformations:

- groupByKey
- reduceByKey

```scala
// Example: Compute the average budget per event organizer

// Calculate a pair (as a key's value) containing (budget, #events)
val intermediate = 
     eventsRdd.mapValues(b => (b, 1))
     .reduceByKey((v1, v2) => (v1._1 + v2._1, v1._2 + v2._2)
 // intermediate: RDD[(String, (Int, Int))]
 
 val avgBudgets = intermediate.mapValues {
   case (budget, numberOfEvents) => budget / numberOfEvents
 }
 
 avgBudgets.collect().foreach(println)
 // (Prime Sound, 42000)
 // (Sportorg, 12133)
 // (Innotech, 106666)
 ...
```
- mapValues

```scala
  def mapValues[U](f: V => U): RDD[(K, U)]
  rdd.map { case (x, y): (x, func(y))}
```
- keys

```scala
  case class Visitor(ip: String, timestamp: String, duration: String)
  val visits: RDD[Visitor] = sc.textfile(...)
  						.map(v -> (v.ip, v.duration))
  val numUniqueVisits = visits.keys.distinct().count()
  
  // numUniqueVisites: Long = 3391
```
- join 
  
  (innerJoin / outerJoin (leftOuterJoin / rightOuterJoin))
  
  ```scala
	val names1 = sc.parallelize(List("abe", "abby", "apple")).map(a => (a, 1))
	val names2 = sc.parallelize(List("apple", "beatty", "beatrice")).map(a => (a, 1))
	
	names1.join(names2).collect
	
	//res735: Array[(String, (Int, Int))] = Array((apple,(1,1)))
	
	names1.leftOuterJoin(names2).collect
	
	//res736: Array[(String, (Int, Option[Int]))] = Array((abby,(1,None)), (apple,(1,Some(1))), (abe,(1,None)))
	
	names1.rightOuterJoin(names2).collect
	
	//res737: Array[(String, (Option[Int], Int))] = Array((apple,(Some(1),1)), (beatty,(None,1)), (beatrice,(None,1)))
   ```

Action

- countByKey

```scala
countByKey (def countByKey(): Map[K, Long])
```

### Examples

[APACHE SPARK: EXAMPLES OF TRANSFORMATIONS](https://www.supergloo.com/fieldnotes/apache-spark-examples-of-transformations/)

## Partition and Shuffle

Operations on Pair RDDs that hold to (and propagate) a partitioner:

- cogroup
- groupWith
- join
- leftOuterJoin
- rightOuterJoin
- groupByKey
- reduceByKey
- foldByKey
- combineByKey
- partitionBy
- sort
- mapValues (if parent has a partitioner)
- flatMapValues (if parent has a partitioner)
- filter (if parent has a partitioner)

Try to use narrow dependencies but not wide dependences.

Transformations with wide dependencies:
(Operations that might cause a shuffle)

- cogroup
- groupWith
- join
- leftOuterJoin
- rightOuterJoin
- groupByKey
- reduceByKey
- combineByKey
- distinct
- intersection
- repartition
- coalesce

### Dependencies method on RDDs


Narrow dependency objects:

- OneToOneDependency
- PruneDependency
- RangeDependency

Wide dependency objects:

- ShuffleDependency


#### dependencies method
```scala
val wordsRdd = sc.parallelize(largeList)
val pairs = wordsRdd.map(c => (c, 1))
						.groupByKey()
						.dependencies
// pairs: Seq[org.apache.spark.Dependency[_]] = 
// List(org.apache.spark.ShuffleDependency@4294a23d)

```

#### toDebugString method on RDDs
```scala
val wordsRdd = sc.parallelize(largeList)
val pairs = wordsRdd.map(c => (c, 1))
						.groupByKey()
						.toDebugString
// pairs: String = 
// (8) ShuffledRDD[219] at groupByKey at <console>:38 []
// +-(8) MapPartitionsRDD[218] at map at <console>:37 []
// | ParallelCollectionRDD[217] at parallelize at <console>:36 []

```

### Partioning Data: partitionBy

```scala
val pairs = purchasesRdd.map(p=> (p.customerId, p.price))

val tunedPartitioner = new RangePartitioner(8, pairs)
val partitioned = pairs.partitionBy(tunedPartitioner).persist()

```

Creating a RangerPartitioner requires:

1. Specifying the desired number of partitions.
2. Providing a Pair RDD with ordered keys. This RDD is sampled to create a suitable set of sorted ranges.

**Important: the result of partitionBy should be pesisted. Otherwise, the partitioning is repeatedly applied (involving shuffling!) each time the partitioned RDD is used.**

### Methods avoid much or all network shuffling

There are a few ways to use operations that might cause a shuffle and to still avoid much or all network shuffling.

1. reduceByKey running on a pre-partitioned RDD will cause the values to be computed locally, requiring only the final reduced value has to be sent from the worker to the driver.
2. join called on two RDDs that are pre-partitioned with the same partitioner and cached on the same machine will cause the join to be computed locally, with no shuffling across the network.


### Lineages graphs are the key to fault tolerance in Spark


