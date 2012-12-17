package oceanrouter.core

import annotation.tailrec
import oceanrouter.conv._
import akka.actor.Actor
import java.lang.IllegalArgumentException
import org.scala_tools.time.Imports._

case class RoutingRequest(oLon: Double, oLat: Double, dLon: Double, dLat: Double, at: String) {
  require(try { new DateTime(at); true } catch { case _: IllegalArgumentException => false })
}
case class RoutingResult(bestRoute: Option[Route], steps: List[Route])

class RoutingActor extends Actor {
  def receive = {
    case r: RoutingRequest => sender ! new Routing(r).run
  }
}

class Routing(val journey: Journey, val at: DateTime) {

  def this(r: RoutingRequest) =
    this(new Journey(new Position(r.oLon, r.oLat), new Position(r.dLon, r.dLat)), new DateTime(r.at))

  val divergenceDelta = 50
  val convergenceDelta = 25
  val deltaSlice = 5
  val poolSize = 100

  val routesPerSlice = poolSize / (divergenceDelta * 2 / deltaSlice)

  val divergenceAngles = (-divergenceDelta to divergenceDelta by deltaSlice)
  val convergenceAngles = (-convergenceDelta to convergenceDelta by deltaSlice)

  val start = journey.start(at)

  def run: RoutingResult = loop(List[Route](start))

  def filterBestRoutes(routeCloud: List[Route]): List[Route] = {
    for {
      (_, slicePool) <- routeCloud.groupBy(s => s.headingFromOrigin.toInt / deltaSlice * deltaSlice).toList
      s <- slicePool.sortBy(_.distanceFromOrigin).takeRight(routesPerSlice)
    } yield s
  }

  def nextAngles(r: Route) = if (r.convergence) convergenceAngles else divergenceAngles

  def expandNextStep(step: List[Route]): List[Route] = {
    step.par.flatMap(s =>
      if (s.convergence)
        convergenceAngles.par.map(a => s.next(ensureDegrees(s.headingToDest + a))).toList
      else
        divergenceAngles.par.map(a => s.next(ensureDegrees(journey.heading + a))).toList
    ).toList
  }

  @tailrec
  private def loop(currentStep: List[Route]): RoutingResult = {
    val next = expandNextStep(currentStep)
    next.filter(_.finished).sortBy(_.time.getMillis) match {
      case winner :: xs => RoutingResult(Some(winner), currentStep)
      case _ => loop(filterBestRoutes(next))
    }
  }
}
