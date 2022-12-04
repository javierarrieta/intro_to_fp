package fp.intro

import cats.Monad
import cats.effect.std.Console
import cats.effect.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*

object Effects extends IOApp:

  case class DbConfig(unused: String)

  object DbConfig:
    def fromEnvironment: Either[Throwable, DbConfig] =
      Right(DbConfig("unused"))

  trait DBConnection

  object DBConnection:
    def apply(dbConfig: DbConfig): IO[DBConnection] =
      IO.delay(new DBConnection {})

    def find: DBConnection => String => IO[Option[(String, String)]] = dbConn =>
      case "exit" => IO.pure(None)
      case "error" => IO.raiseError(new IllegalArgumentException)
      case s => IO.pure(Some((s, s"result-$s")))

  object BusinessLogic:
    final case class BusinessObject(id: String, name: String)

    val fromTuple: ((String, String)) => BusinessObject = (a,b) => BusinessObject(a,b)

    val findBusinessObject: DBConnection => String => IO[Option[BusinessObject]] =
      dbConn => id => DBConnection.find(dbConn)(id).map(_.map(fromTuple))


  import BusinessLogic.BusinessObject

  override def run(args: List[String]): IO[ExitCode] =
    val finder: IO[String => IO[Option[BusinessObject]]] = for {
      dbConfig <- IO.fromEither(DbConfig.fromEnvironment)
      dbConnection <- DBConnection(dbConfig)
    } yield BusinessLogic.findBusinessObject(dbConnection)

    val console = Console.make[IO]
    val consoleReader: IO[String] = console.readLine

    val businessInput: IO[Option[BusinessObject]] = for {
      _ <- console.println("""Please input id ["exit" to finish]:""")
      l <- consoleReader
      f <- finder
      r <- f(l)
    } yield r

    val ioBo = for {
      bo <- businessInput
      _ <- bo.map(console.println).sequence
    } yield bo

    Monad[IO].iterateWhile(ioBo)(_.nonEmpty) *> IO.pure(ExitCode.Success)