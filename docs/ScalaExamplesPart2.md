# Scala Example (Part 2) - String, Transformation

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
