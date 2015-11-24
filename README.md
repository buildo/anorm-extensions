# anorm-extensions

Serialize `Product` directly in [anorm](https://github.com/playframework/anorm) queries.

## Installation
Using sbt:

```sbt
resolvers += Resolver.jcenterRepo

libraryDependencies += "io.buildo" %% "anorm-extension" % "0.1.0"
```

## Usage
Mix in the `ProductParameterValueSupport` trait.
That's it.


## Why
When using [anorm](https://github.com/playframework/anorm) this

```scala
val query = "where PARAM in ({paramVal})"
val paramVal = List("a", "b", "c")
``` 

is correctly mapped to

```sql
where PARAM in ('a','b','c')
```

However, passing a `List` of tuples doesn't work:

```scala
val query = "where (PARAM1, PARAM2) in ({paramVal})"
val paramVal = List(("a","b"), ("c","d"), ("e","f"))
```

This is what we would expect:

```sql
where (PARAM1, PARAM2) in (('a','b'), ('c','d'), ('e','f'))
```

but it fails at compile time instead.

**anorm-extensions** allows passing any `Product` (tuples, case classes, ...) as parameter in anorm.

## Under the hood

In order to leverage Anorm `ParameterValue[P]` implicit builder ([ParameterValue.scala](https://github.com/playframework/anorm/blob/master/core/src/main/scala/anorm/ParameterValue.scala)) we have to provide, implicitly, an instance of `ToSql[P]` and `ToStatement[P]`.

This library provides an implicit conversion from `Product` to `ParameterValue`, leveraging [shapeless](https://github.com/milessabin/shapeless) `Generic`.
