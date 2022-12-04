package fp.intro

object ReferentialTransparency {
  // Referential transparency
  // https://en.wikipedia.org/wiki/Referential_transparency

  // An expression is called referentially transparent if it can be replaced
  // with its corresponding value (and vice-versa) without changing the
  // program's behavior.

  def square(d: Double): Double = d * d // Referentially transparent

  def printConsole(s: String): Unit = println(s) //Not transparent

  printConsole("yes")

  // Reading from the network, reading from a db, logging, IO, a function with arity-0
  // are not transparent, they can also be called side-effecting
  // How can we overcome this? We will see later how we can use
  // side effecting functions in a referentially transparent way (as FP mandates)



  // Expressions vs statements
  // In FP because of referential transparency we don't have statements
  // Every expression is used by composing it in a bigger scope function

  square(100) //This statement does not provide anything as it is not composed in a coarser Function

  val sqr100 = square(100) //This value should be used somewhere (otherwise the compiler will error out)

  // Usually FP languages don't have return (as it is a statement)
  // The result is the last value in the function/method expression

  // Partial functions
  // https://en.wikipedia.org/wiki/Partial_function

  def div(n: Double, d: Double): Double = n / d

  def increment(i: Int): Int = i+1 //Not transparent

  div(4.5, 3)
  div(2, 0)

  Int.MaxValue + 1

  Math.sqrt(-1.0d)

  val maybeSqrt: Double => Option[Double] = {
    case d if d < 0 => None
    case d => Some(Math.sqrt(d))
  }

  val completeSqrt: Double => Either[String, Double] = {
    case d if d < 0 => Left("Imaginary numbers not supported")
    case d => Right(Math.sqrt(d))
  }

  completeSqrt(-1)

  completeSqrt(2)

  maybeSqrt(-100)

  maybeSqrt(-100).toRight("ImaginaryNumbersNotSupported")

  maybeSqrt(100).toRight("ImaginaryNumbersNotSupported")
}
