package improbable.player

import com.typesafe.scalalogging.Logger
import improbable.Cancellable
import improbable.entity.physical.PhysicsSimulationController
import improbable.papi.engine.EngineId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class RedelegateControl(engineId: Option[EngineId]) extends CustomMsg

class ClientSideMovementControlsBehaviour(world: World, entity: Entity, logger: Logger, physicsSimulationController: PhysicsSimulationController) extends EntityBehaviour {

  var controlsCancellable = Cancellable()

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case RedelegateControl(engineId) =>
        logger.info(s"Redelegating control for entityId: ${entity.entityId} to engineId $engineId.")
        redelegateControls(engineId)
    }
  }

  private def redelegateControls(controller: Option[EngineId]): Unit = {
    controlsCancellable.cancel()
    controller.foreach {
      engine =>
        delegateControls(engine)
    }
  }

  private def delegateControls(engine: EngineId): Unit = {
    logger.info("Using client side physics.")
    physicsSimulationController.useClientSidePhysics(engine)
  }
}
