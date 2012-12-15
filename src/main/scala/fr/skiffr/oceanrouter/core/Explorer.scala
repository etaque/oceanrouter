package fr.skiffr.oceanrouter.core

import org.joda.time.DateTime
import annotation.tailrec
import fr.skiffr.oceanrouter.conv
import akka.actor.Actor

case class RoutingRequest(origin: Position, dest: Position, at: DateTime)
case class RoutingResult(bestRoute: Option[Route], steps: List[Route])

class ExplorerActor extends Actor {
  def receive = {
    case r: RoutingRequest => sender ! new Explorer(r).run
  }
}

class Explorer(val journey: Journey, val at: DateTime) {

  def this(r: RoutingRequest) = this(new Journey(r.origin, r.dest, 60*60*3), r.at)

  val divergenceDelta = 60
  val convergenceDelta = 15
  val deltaSlice = 5
  val poolSize = 100

  val routesPerSlice = poolSize / (divergenceDelta * 2 / deltaSlice)

  val divergenceAngles = (-divergenceDelta to divergenceDelta by deltaSlice)
  val convergenceAngles = (-convergenceDelta to convergenceDelta by deltaSlice)

  val start = journey.start(at)

  def run: RoutingResult = loop(List[Route](start), 50)

  def takeBestRoutes(routeCloud: List[Route]): List[Route] = {
    for {
      (_, slicePool) <- routeCloud.groupBy(s => s.headingFromOrigin.toInt / deltaSlice * deltaSlice).toList
      s <- slicePool.sortBy(_.distanceFromOrigin).takeRight(routesPerSlice)
    } yield s
  }

  def nextAngles(s: Route) = if (s.convergence) convergenceAngles else divergenceAngles

  @tailrec
  private def loop(routesStep: List[Route], count: Int): RoutingResult = {
    if (count == 0) RoutingResult(None, routesStep)
    else {
      val cloud = routesStep.par.flatMap(s =>
        if (s.convergence)
          convergenceAngles.par.map(a => s.next(conv.ensureDegrees(s.headingToDest + a))).toList
        else
          divergenceAngles.par.map(a => s.next(conv.ensureDegrees(journey.heading + a))).toList
      ).toList

      cloud.filter(_.finished).sortBy(_.time.getMillis) match {
        case winner :: xs => RoutingResult(Some(winner), routesStep)
        case _ => loop(takeBestRoutes(cloud), count - 1)
      }
    }
  }
}
