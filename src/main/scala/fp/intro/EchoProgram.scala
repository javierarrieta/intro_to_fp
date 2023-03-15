package fp.intro

import cats.syntax.show._
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.std.Console

object EchoProgram extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    val myConsole: Console[IO] = Console.make[IO]
    val input: IO[String] = myConsole.readLine

    val prompt: IO[Unit] = myConsole.print("Type your input: ")
    val requestInput: IO[String] = prompt.flatMap(_ => input)

    val echo: IO[Unit] = requestInput.flatMap(i => myConsole.println(show"You typed: $i"))

    echo.map(_ => ExitCode.Success)

//    val myConsole: Console[IO] = Console.make[IO]
//
//    for {
//      _ <- myConsole.print("awere")
//      i <- myConsole.readLine
//      _ <- myConsole.print(show"You typed: $i")
//    } yield ExitCode.Success
  }
}
