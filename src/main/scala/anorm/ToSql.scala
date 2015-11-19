package anorm

private[anorm] trait ProductToSql {

  import shapeless.{ HList, HNil, Poly2, Generic }
  import shapeless.ops.hlist.LeftFolder

  private implicit val hNilToSql: ToSql[HNil] = new ToSql[HNil] {
    def fragment(values: HNil): (String, Int) = ("" -> 0)
  }

  private object toSql extends Poly2 {
    implicit def caseToSql[T](implicit conv: ToSql[T] = null) =
      at[(StringBuilder, Int), T]{
        case ((sb, i), v) =>
          val c: T => (String, Int) =
            if (conv == null) _ => ("?" -> 1) else conv.fragment
          val frag = c(v)
          val st = if (i > 0) sb ++= ", " ++= frag._1 else sb ++= frag._1
          (st, i + frag._2)
      }
  }

  implicit def productToSql[P <: Product, H <: HList](
    implicit generic: Generic.Aux[P, H],
    folder: LeftFolder.Aux[H, (StringBuilder, Int), toSql.type, (StringBuilder, Int)]): ToSql[P] =
    new ToSql[P] {
      def fragment(values: P): (String, Int) = {
        val sql = generic.to(values).foldLeft((new StringBuilder(), 0))(toSql)
        s"(${sql._1.toString})" -> sql._2
      }
    }

}
