// Referential transparency
// https://en.wikipedia.org/wiki/Referential_transparency

// An expression is called referentially transparent if it can be replaced
// with its corresponding value (and vice-versa) without changing the
// program's behavior.

def square(d: Double) = d * d // Referentially transparent

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

def addOne(i: Int): Int = i+1 //Not transparent

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

/*
How to represent effectful/impure computations in a referential transparent manner?

We use an effect type constructor, in this example would be IO[A] that represents a deferred
computation that would yield a result of type A when executed
*/

import cats.effect.std.Console
import cats.effect.IO
import cats.effect.unsafe.implicits.global

val myDeferredComputation: IO[Int] = IO.delay(42)

val myConsole = Console.make[IO]
val deferredA: IO[String] = myConsole.readLine

val echo = deferredA.flatMap(myConsole.println)

//Go to ./EchoProgram.scala for a demo
