package improbable.ai

import improbable.Cancellable
import improbable.abyssal.ai.MovementTargetWriter
import improbable.math.Coordinates
import improbable.papi.EntityId
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

import scala.concurrent.duration._

trait PathingController extends EntityBehaviourInterface {
  def goToCoordinates(target: Coordinates): Unit
  def startFollowingTarget(target: EntityId): Unit
  def stopFollowingTarget(): Unit
}

class MovementTargetBehaviour(world: World, entity: Entity, movementTargetWriter: MovementTargetWriter) extends EntityBehaviour with PathingController {

  private var pathing: Cancellable = Cancellable()

  override def stopFollowingTarget(): Unit = {
    pathing.cancel()
    movementTargetWriter.update.target(None).finishAndSend()
  }

  override def startFollowingTarget(target: EntityId): Unit = {
    pathing.cancel()
    pathing = world.timing.every(100.milliseconds) {
      val movementTarget = world.entities.find(target).map(_.position)
      movementTargetWriter.update.target(movementTarget).finishAndSend()
    }
  }

  override def goToCoordinates(target: Coordinates): Unit = {
    pathing.cancel()
    movementTargetWriter.update.target(Some(target)).finishAndSend()
  }
}
