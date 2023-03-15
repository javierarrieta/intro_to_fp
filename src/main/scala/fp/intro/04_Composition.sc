// Error handling and basic composition: https://scastie.scala-lang.org/javierarrieta/YnhlFjGvQNWy2GVTkhKVrw/1

import cats.instances.list.*
import cats.syntax.all.*
import cats.data.{Validated, NonEmptyList}

val romanChar2IntPF: PartialFunction[Char, Int] = _.toLower match {
  case 'i' => 1
  case 'v' => 5
  case 'x' => 10
  case 'l' => 50
  case 'c' => 100
  case 'd' => 500
  case 'm' => 1000
}

val romanChar2Int: Char => Either[String, Int] =
  romanChar2IntPF.andThen(Right.apply).orElse(c => Left(s"'$c' is not a valid Roman digit'"))
//i => Right(romanChar2IntPf(i))
romanChar2Int('V')
romanChar2Int('P')
//
//val romanChar2IntValidated: (Char => Either[String, Int]) => (Char => Validated[NonEmptyList[String], Int]) =
//  romanChar2Int.andThen(_.toValidatedNel)

//Javi: Use different types for the accummulating
val roman2IntValidated: NonEmptyList[Char] => Validated[NonEmptyList[String], NonEmptyList[Int]] =
  _.traverse(romanChar2Int.andThen(_.toValidatedNel)) //Char => Validated[Nel[String], Int]

// Either[String, Int] => Validated[NonEmptyList[String], Int]

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

roman2Int("")
roman2Int("XJIOPE")
roman2Int("LCVI")
roman2Int("MMXXII")
roman2Int("MXDII")