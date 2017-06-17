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
