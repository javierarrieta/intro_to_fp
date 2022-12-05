// Error handling and basic composition

import cats.data.{NonEmptyList, Validated}

import cats.instances.list.*
import cats.syntax.traverse.*
import cats.syntax.either.*

val romaChar2IntPF: PartialFunction[Char, Int] = _.toLower match {
  case 'i' => 1
  case 'v' => 5
  case 'x' => 10
  case 'l' => 50
  case 'c' => 100
  case 'd' => 500
  case 'm' => 1000
}

val romanChar2Int: Char => Validated[NonEmptyList[String], Int] = romaChar2IntPF.andThen(Validated.Valid.apply)
  .orElse(c => Validated.Invalid(NonEmptyList.one(s"'$c' is not a valid Roman digit'")))


val roman2Int: String => Validated[NonEmptyList[String], List[Int]] =
  _.toList.traverse(romanChar2Int)

roman2Int("VII")
roman2Int("MKCLJBI")
