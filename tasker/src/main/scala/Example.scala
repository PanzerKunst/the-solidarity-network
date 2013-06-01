import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case "buenos dias"       => println("huh?")
  }
}

object Main extends App {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")


  val cancellable = system.scheduler.schedule(
    0 milliseconds,
    5 seconds,
    helloActor,
    "hello"
  )

  val cancellable2 = system.scheduler.schedule(
    0 milliseconds,
    5 seconds,
    helloActor,
    "buenos dias"
  )

}