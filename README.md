# anorm-extensions

Directly use instances of `Product` as parameters in [anorm](https://github.com/playframework/anorm) queries.

## Example

```scala
case class User(name: String, age: Int)

val users = List(User("Federico", 26), User("Gabriele", 25))

val query = s"where (NAME, AGE) in ($filterList)""")

val query = SQL"""
  select * from USERS
  where (NAME, AGE) in $users
"""
```

produces

```SQL
SELECT * FROM USERS
WHERE (NAME, AGE) IN ((Federico, 26), (Gabriele, 25))
```

## Installation
Using sbt:

```sbt
resolvers += Resolver.bintrayRepo("buildo", "maven")

libraryDependencies += "io.buildo" %% "anorm-extensions" % "0.1.0"
```

## Usage
Mix in the `ProductParameterValueSupport` trait.
That's it.


## Why
When using [anorm](https://github.com/playframework/anorm) this

```scala
val params = List("a", "b", "c")
val query = SQL"""
  select * from FOO
  where PARAM in $params
"""
``` 

is correctly mapped to

```sql
SELECT * FROM FOO
WHERE PARAM IN ('a','b','c')
```

However, passing a `List` of tuples (or any other product type) doesn't work:

```scala
val params = List(("a","b"), ("c","d"), ("e","f"))
val query = SQL"""
  select * from FOO
  where (PARAM1, PARAM2) in $params
"""
```

This is what we would expect:

```SQL
SELECT * FROM FOO
WHERE (PARAM1, PARAM2) IN (('a','b'), ('c','d'), ('e','f'))
```

but it fails at compile time instead.

Similarly, we can't pass a tuple or a case class instance directly.

**anorm-extensions** allows passing any `Product` (tuples, case classes, ...) as parameter in anorm, as shown in the example above.

## Under the hood

In order to leverage Anorm `ParameterValue[P]` implicit builder ([ParameterValue.scala](https://github.com/playframework/anorm/blob/master/core/src/main/scala/anorm/ParameterValue.scala)) we have to provide, implicitly, an instance of `ToSql[P]` and `ToStatement[P]`.

This library provides an implicit conversion from `Product` to `ParameterValue`, leveraging [shapeless](https://github.com/milessabin/shapeless) `Generic`.
