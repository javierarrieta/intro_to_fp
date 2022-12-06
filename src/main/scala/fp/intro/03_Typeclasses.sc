// Type classes

// Many type classes require higher kinded types
trait MyFunctor[F[_]] {
  def fmap[A, B](fa: F[A])(f: A => B): F[B]
}

object optionFunctor extends MyFunctor[Option] {
  override def fmap[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
    case None => None
    case Some(a) => Some(f(a))
  }

  //f.map(fa)
}

trait MyMonad[F[_]] {
  def bind[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object optionMonad extends MyMonad[Option] {
  override def bind[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa match {
    case None => None
    case Some(a) => f(a)
  }
  //fa.flatMap(f)
}

optionFunctor.fmap(Some(5))(2.*)
optionMonad.bind(Some(5))(a => Some(5*a))
