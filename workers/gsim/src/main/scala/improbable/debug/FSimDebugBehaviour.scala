package improbable.debug

import com.typesafe.scalalogging.Logger
import improbable.abyssal.debug.FSimDebug
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.unity.fabric.PhysicsEngineConstraint

class FSimDebugBehaviour(entity: Entity, logger: Logger) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateState[FSimDebug](PhysicsEngineConstraint)

    entity.watch[FSimDebug].onDebugMessage {
      msg =>
        logger.info("FSim: " + msg)
    }
  }
}
