package anorm

private[anorm] trait ProductToStatement {

  import shapeless.{ HList, HNil, Poly2, Nat, Generic }
  import shapeless.ops.hlist.{ LeftFolder, Length }
  import shapeless.ops.nat.ToInt
  import java.sql.PreparedStatement

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
    new ToStatement[List[P]] with NotNullGuard {
      def set(s: PreparedStatement, offset: Int, ps: List[P]) =
        if (ps == null) throw new IllegalArgumentException()
        else ps.foldLeft(offset) { (i, p) => c.set(s, i, p); i + toInt.apply }
    }

}
