package anorm

private[anorm] trait ProductToStatement {

  import shapeless.{ HList, HNil, Poly2, Nat, Generic }
  import shapeless.ops.hlist.{ LeftFolder, Length }
  import shapeless.ops.nat.ToInt
  import java.sql.PreparedStatement
  import scala.collection.immutable.SortedSet

  private implicit val hNilToStatement: ToStatement[HNil] =
    new ToStatement[HNil] with NotNullGuard {
      def set(s: PreparedStatement, offset: Int, ps: HNil) = ()
    }

  private object toStatement extends Poly2 {
    implicit def caseToStatement[T](implicit c: ToStatement[T]) =
      at[(PreparedStatement, Int), T]{ case ((s, i), p) => c.set(s, i, p); (s, i + 1) }
  }

  implicit def productToStatement[P <: Product, H <: HList](
    implicit generic: Generic.Aux[P, H],
    folder: LeftFolder[H, (PreparedStatement, Int), toStatement.type]): ToStatement[P] =
    new ToStatement[P] with NotNullGuard {
      def set(s: PreparedStatement, offset: Int, ps: P) =
        if (ps == null) throw new IllegalArgumentException()
        else generic.to(ps).foldLeft((s, offset))(toStatement)
    }

  implicit def productListToStatement[P <: Product, H <: HList, N <: Nat](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[List[P]] =
    productTraversableToStatement[P, H, N, List[P]]

  implicit def productSeqToStatement[P <: Product, H <: HList, N <: Nat](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[Seq[P]] =
    productTraversableToStatement[P, H, N, Seq[P]]

  implicit def productSetToStatement[P <: Product, H <: HList, N <: Nat](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[Set[P]] =
    productTraversableToStatement[P, H, N, Set[P]]

  implicit def productSortedSetToStatement[P <: Product, H <: HList, N <: Nat](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[SortedSet[P]] =
    productTraversableToStatement[P, H, N, SortedSet[P]]

  implicit def productStreamToStatement[P <: Product, H <: HList, N <: Nat](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[Stream[P]] =
    productTraversableToStatement[P, H, N, Stream[P]]

  implicit def productVectorToStatement[P <: Product, H <: HList, N <: Nat](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[Vector[P]] =
    productTraversableToStatement[P, H, N, Vector[P]]

  private implicit def productTraversableToStatement[P <: Product, H <: HList, N <: Nat, T <: Traversable[P]](
    implicit c: ToStatement[P], generic: Generic.Aux[P, H],
    length: Length.Aux[H, N], toInt: ToInt[N]): ToStatement[T] =
    new ToStatement[T] with NotNullGuard {
      def set(s: PreparedStatement, offset: Int, ps: T) =
        if (ps == null) throw new IllegalArgumentException()
        else ps.foldLeft(offset) { (i, p) => c.set(s, i, p); i + toInt.apply }
    }

}
