package fp.intro

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.std.Console

object EchoProgram extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    val myConsole = Console.make[IO]
    val deferredA = myConsole.readLine

    val echo = deferredA.flatMap(myConsole.println)

    echo.map(_ => ExitCode.Success)
  }
}
