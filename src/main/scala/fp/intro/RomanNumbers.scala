package fp.intro

object RomanNumbers extends App {
  // Error handling and basic composition


  import cats.instances.list.*
  import cats.syntax.all.*
  import cats.data.{Validated, NonEmptyList}

  val romaChar2IntPF: PartialFunction[Char, Int] = _.toLower match {
    case 'i' => 1
    case 'v' => 5
    case 'x' => 10
    case 'l' => 50
    case 'c' => 100
    case 'd' => 500
    case 'm' => 1000
  }

  val romanChar2Int: Char => Either[String, Int] =
    romaChar2IntPF.andThen(Right.apply).orElse(c => Left(s"'$c' is not a valid Roman digit'"))

  val roman2IntValidated: NonEmptyList[Char] => Validated[NonEmptyList[String], NonEmptyList[Int]] =
    _.traverse(romanChar2Int.andThen(_.toValidatedNel))

  val computeSubtracts: List[Int] => Validated[NonEmptyList[String], Int] = {
    case first :: second :: Nil => Validated.Valid(if (first >= second) first else -first)
    case l => Validated.Invalid(NonEmptyList.one(s"Wrong function, needed 2 Ints and got $l"))
  }

  val roman2Int: String => Either[NonEmptyList[String], Int] = { s =>
    for {
      l <- NonEmptyList.fromList(s.toList).toRight(NonEmptyList.one("Cannot use an empty list as input"))
      ints <- roman2IntValidated(l).toEither
      r <- ints.toList.sliding(2).toList.traverse(computeSubtracts).map(b => (b :+ ints.last).sum).toEither
    } yield r
  }

  List("", "XJIOPE", "LCVI", "MMXXII", "MXDII").map(roman2Int)

}
