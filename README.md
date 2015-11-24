![](https://travis-ci.org/buildo/anorm-extensions.svg)

# anorm-extensions

Anorm to `ParameterValue` implicit conversion is *NOT* available for multiple values `IN` clauses:
- **WORKING**: ```val query = "where PARAM in ({paramVal})"``` with ```val paramVal = List("a", "b", "c")``` is correctly mapped to ```where PARAM in ('a','b','c')```
- **NOT WORKING**: ```val query = "where (PARAM1, PARAM2) in ({paramVal})"``` with ```val paramVal = List(("a","b"), ("c","d"), ("e","f"))``` is not correctly mapped to ```where (PARAM1, PARAM2) in (('a','b'), ('c','d'), ('e','f'))```

In order to leverage Anorm `ParameterValue[P]` implicit builder ([ParameterValue.scala](https://github.com/playframework/anorm/blob/master/core/src/main/scala/anorm/ParameterValue.scala)) we have to provide, implicitly, an instance of `ToSql[P]` and `ToStatement[P]`.

This Anorm extension contains an implicit conversion from `Product` to `ParameterValue` built leveraging shapeless `HList`s.

In order to exploit this capability you only have to mix `ProductParameterValueSupport` and then proceed as normal.

If the code looks a little weird is because my goal is to be more consistent as possible with Anorm code structure: [ToSql.scala](https://github.com/playframework/anorm/blob/master/core/src/main/scala/anorm/ToSql.scala), [ToStatement.scala](https://github.com/playframework/anorm/blob/master/core/src/main/scala/anorm/ToStatement.scala)
