package fr.skiffr.oceanrouter.core

import org.joda.time.DateTime
import annotation.tailrec
import fr.skiffr.oceanrouter.conv
import fr.skiffr.oceanrouter.core.Route

class Explorer(val journey: Journey, val at: DateTime) {

  val divergenceDelta = 60
  val convergenceDelta = 15
  val deltaSlice = 5
  val poolSize = 100

  val routesPerSlice = poolSize / (divergenceDelta * 2 / deltaSlice)

  val divergenceAngles = (-divergenceDelta to divergenceDelta by deltaSlice)
  val convergenceAngles = (-convergenceDelta to convergenceDelta by deltaSlice)

  val start = journey.start(at)

  def run(loops: Int): (Option[Route], List[Route]) = loop(List[Route](start), loops)

  def takeBestRoutes(routeCloud: List[Route]): List[Route] = {
    for {
      (_, slicePool) <- routeCloud.groupBy(s => s.headingFromOrigin.toInt / deltaSlice * deltaSlice).toList
      s <- slicePool.sortBy(_.distanceFromOrigin).takeRight(routesPerSlice)
    } yield s
  }

  def nextAngles(s: Route) = if (s.convergence) convergenceAngles else divergenceAngles

  @tailrec
  private def loop(routesStep: List[Route], count: Int): (Option[Route], List[Route]) = {
    print(".")
    if (count == 0) (None, routesStep)
    else {
      val cloud = routesStep.par.flatMap(s =>
        if (s.convergence)
          convergenceAngles.par.map(a => s.next(conv.ensureDegrees(s.headingToDest + a))).toList
        else
          divergenceAngles.par.map(a => s.next(conv.ensureDegrees(journey.heading + a))).toList
      ).toList

      cloud.filter(_.finished).sortBy(_.time.getMillis) match {
        case winner :: xs => (Some(winner), routesStep)
        case _ => loop(takeBestRoutes(cloud), count - 1)
      }
    }
  }

  def printIteration(i: List[Route]): Unit = {
    println(i.length + "\n" + i.sortBy(_.headingFromOrigin).map(_.position).mkString(";"))
  }
}
