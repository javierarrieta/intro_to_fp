// Functions and currying

def increment(a: Int): Int = a + 1

val inc: Int => Int = _ + 1 // a => a + 1

val inc2: Int => Int = 1.+

inc(1)
inc2(2)

def multiply(a: Double, b: Double) = a * b

/*
currying is the technique of translating the evaluation of a function
that takes multiple arguments into evaluating a sequence of functions,
each with a single argument.

https://en.wikipedia.org/wiki/Currying
*/
val mult: Double => Double => Double = a => b => a * b

val multc = multiply.curried

mult(2)(5)

multc(2)(5)

val mult2: Double => Double = mult(2)

mult2(5)
mult2(8)

List(1d, 2d, 3d).map(mult(5))
List(1d, 2d, 3d).map(i => multiply(5, i))