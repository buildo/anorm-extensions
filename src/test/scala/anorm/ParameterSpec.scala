package anorm

import scala.collection.immutable.SortedSet

import acolyte.jdbc.{
  DefinedParameter => DParam,
  ParameterMetaData => ParamMeta,
  UpdateExecution
}
import acolyte.jdbc.AcolyteDSL.{ connection, handleStatement }
import acolyte.jdbc.Implicits._

object ParameterSpec
    extends org.specs2.mutable.Specification with ProductParameterValueSupport {

  "Parameter" title

  val SqlStr = ParamMeta.Str
  val SqlBool = ParamMeta.Bool
  val SqlInt = ParamMeta.Int

  case class ProductParam(a: String, b: Int)

  def withConnection[A](ps: (String, String)*)(f: java.sql.Connection => A): A = f(connection(handleStatement withUpdateHandler {
    case UpdateExecution("set-tuple (?, ?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam(false, SqlBool) :: Nil) => 1 // ok
    case UpdateExecution("set-product (?, ?)",
      DParam("a", SqlStr) :: DParam(7, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-tuple-list (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam("b", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-tuple-seq (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam("b", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-tuple-set (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam("b", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-tuple-sortedset (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam("b", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-tuple-stream (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam("b", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-tuple-vector (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(1, SqlInt) :: DParam("b", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-product-list (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(7, SqlInt) :: DParam("c", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-product-seq (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(7, SqlInt) :: DParam("c", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-product-set (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(7, SqlInt) :: DParam("c", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-product-stream (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(7, SqlInt) :: DParam("c", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-product-vector (?, ?), (?, ?)",
      DParam("a", SqlStr) :: DParam(7, SqlInt) :: DParam("c", SqlStr) :: DParam(3, SqlInt) :: Nil) => 1 // ok
    case UpdateExecution("set-null-tuple-list ?",
      DParam(null, SqlStr) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-tuple-seq ?",
      DParam(null, SqlInt) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-tuple-set ?",
      DParam(null, SqlInt) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-tuple-sortedset ?",
      DParam(null, SqlStr) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-tuple-stream ?",
      DParam(null, SqlInt) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-tuple-vector ?",
      DParam(null, SqlStr) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-product-list ?",
      DParam(null, SqlStr) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-product-seq ?",
      DParam(null, SqlInt) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-product-set ?",
      DParam(null, SqlInt) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-product-stream ?",
      DParam(null, SqlInt) :: Nil) => 1 /* ok */
    case UpdateExecution("set-null-product-vector ?",
      DParam(null, SqlStr) :: Nil) => 1 /* ok */
  }, ps: _*))

  "Named parameters" should {
    "for multi-value without null" >> {
      "accept Tuple" in withConnection() { implicit c =>
        SQL("set-tuple {tuple}").on('tuple -> ("a", 1, false)).
          execute() must beFalse
      }

      "accept Product" in withConnection() { implicit c =>
        SQL("set-product {product}").on('product -> ProductParam("a", 7)).
          execute() must beFalse
      }

      "accept Tuple List" in withConnection() { implicit c =>
        SQL("set-tuple-list {tupleList}").on('tupleList -> List(("a", 1), ("b", 3))).
          execute() must beFalse
      }

      "accept Tuple Seq" in withConnection() { implicit c =>
        SQL("set-tuple-seq {tupleSeq}").on('tupleSeq -> Seq(("a", 1), ("b", 3))).
          execute() must beFalse
      }

      "accept Tuple Set" in withConnection() { implicit c =>
        SQL("set-tuple-set {tupleSet}").on('tupleSet -> Set(("a", 1), ("b", 3))).
          execute() must beFalse
      }

      "accept Tuple SortedSet" in withConnection() { implicit c =>
        SQL("set-tuple-sortedset {tupleSortedSet}").on('tupleSortedSet -> SortedSet(("a", 1), ("b", 3))).
          execute() must beFalse
      }

      "accept Tuple Stream" in withConnection() { implicit c =>
        SQL("set-tuple-stream {tupleStream}").on('tupleStream -> Stream(("a", 1), ("b", 3))).
          execute() must beFalse
      }

      "accept Tuple Vector" in withConnection() { implicit c =>
        SQL("set-tuple-vector {tupleVector}").on('tupleVector -> Vector(("a", 1), ("b", 3))).
          execute() must beFalse
      }

      "accept Product List" in withConnection() { implicit c =>
        SQL("set-product-list {productList}").on('productList -> List(ProductParam("a", 7), ProductParam("c", 3))).
          execute() must beFalse
      }

      "accept Product Seq" in withConnection() { implicit c =>
        SQL("set-product-seq {productSeq}").on('productSeq -> Seq(ProductParam("a", 7), ProductParam("c", 3))).
          execute() must beFalse
      }

      "accept Product Set" in withConnection() { implicit c =>
        SQL("set-product-set {productSet}").on('productSet -> Set(ProductParam("a", 7), ProductParam("c", 3))).
          execute() must beFalse
      }

      "accept Product Stream" in withConnection() { implicit c =>
        SQL("set-product-stream {productStream}").on('productStream -> Stream(ProductParam("a", 7), ProductParam("c", 3))).
          execute() must beFalse
      }

      "accept Product Vector" in withConnection() { implicit c =>
        SQL("set-product-vector {productVector}").on('productVector -> Vector(ProductParam("a", 7), ProductParam("c", 3))).
          execute() must beFalse
      }
    }

    "refuse multi-value" >> {
      "for Tuple List" in withConnection() { implicit c =>
        SQL("set-null-tuple-list {list}").on('list -> null.asInstanceOf[List[(String, Int)]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Tuple Seq" in withConnection() { implicit c =>
        SQL("set-null-tuple-seq {seq}").on('seq -> null.asInstanceOf[Seq[(String, Int)]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Tuple Set" in withConnection() { implicit c =>
        SQL("set-null-tuple-set {set}").on('set -> null.asInstanceOf[Set[(String, Int)]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Tuple SortedSet" in withConnection() { implicit c =>
        SQL("set-null-tuple-sortedset {sortedSet}").on('sortedSet -> null.asInstanceOf[SortedSet[(String, Int)]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Tuple Stream" in withConnection() { implicit c =>
        SQL("set-null-tuple-stream {stream}").on('stream -> null.asInstanceOf[Stream[(String, Int)]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Tuple Vector" in withConnection() { implicit c =>
        SQL("set-null-tuple-vector {vector}").on('vector -> null.asInstanceOf[Vector[(String, Int)]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Product List" in withConnection() { implicit c =>
        SQL("set-null-product-list {list}").on('list -> null.asInstanceOf[List[ProductParam]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Product Seq" in withConnection() { implicit c =>
        SQL("set-null-product-seq {seq}").on('seq -> null.asInstanceOf[Seq[ProductParam]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Product Set" in withConnection() { implicit c =>
        SQL("set-null-product-set {set}").on('set -> null.asInstanceOf[Set[ProductParam]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Product Stream" in withConnection() { implicit c =>
        SQL("set-null-product-stream {stream}").on('stream -> null.asInstanceOf[Stream[ProductParam]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }

      "for Product Vector" in withConnection() { implicit c =>
        SQL("set-null-product-vector {vector}").on('vector -> null.asInstanceOf[Vector[ProductParam]]).
          aka("parameter conversion") must throwA[IllegalArgumentException]

      }
    }
  }

  private def pv[A](v: A)(implicit s: ToSql[A] = null, p: ToStatement[A]) =
    ParameterValue(v, s, p)

}
