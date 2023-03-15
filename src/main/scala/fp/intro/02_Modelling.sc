// Modelling: https://scastie.scala-lang.org/javierarrieta/qi5WFf09Seyka0ySzzKHSw/1

/*
Make illegal states irrepresentable using the type system
*/

//Sum type
enum Shape {
  case Circle(radius: Double)
  //Product type
  case Rectangle(width: Double, height: Double)
//  case Triangle(base: Double, height: Double)
}

val area: Shape => Double = {
  case Shape.Circle(radius) => radius * radius * Math.PI
  case Shape.Rectangle(w, h) => w * h
}

area(Shape.Circle(5.0))

area(Shape.Rectangle(3.0, 6.0))

//area(Shape.Triangle(2.0,1.0))

//ADT
enum Color(val rgb: Int) {
  case Red extends Color(0xff0000)
  case Green extends Color(0x00ff00)
  case Blue extends Color(0x0000ff)
  case Mix(mix: Int) extends Color(mix)
}

object People {
  opaque type FirstName = String
  opaque type LastName = String

  private val validateName: String => Option[String] = n =>
    if (n.isEmpty || n.charAt(0).isLower) None else Some(n)

  object FirstName {
    val fromString: String => Option[FirstName] = validateName
  }
  object LastName {
    val fromString: String => Option[LastName] = validateName
  }

  case class Person(f: FirstName, l: LastName)
}

import People.*

//Person("John", "Doe")

for {
  f <- FirstName.fromString("John")
  l <- LastName.fromString("Doe")
} yield Person(f, l)

for {
  f <- FirstName.fromString("")
  l <- LastName.fromString("Doe")
} yield Person(f, l)


object Shopping:
  opaque type Money = BigDecimal
  object Money:
    def apply(m: BigDecimal): Money = m
  opaque type UnitPrice = BigDecimal
  object UnitPrice:
    def apply(up: BigDecimal): UnitPrice = up
  opaque type Quantity = Int
  object Quantity:
    def apply(q: Int): Quantity = q
  opaque type Discount = BigDecimal
  object Discount:
    def apply(d: BigDecimal): Either[String, Discount] = d match {
      case neg if neg < 0 => Left("Discount cannot be < 0%")
      case tooBig if tooBig > 100 => Left("Discount cannot be > 100%")
      case v => Right(v)
    }

  extension (q: Quantity) {
    def *(up: UnitPrice): Money = (up: BigDecimal) * up
  }
import Shopping.*

Quantity(1) * UnitPrice(BigDecimal(10))