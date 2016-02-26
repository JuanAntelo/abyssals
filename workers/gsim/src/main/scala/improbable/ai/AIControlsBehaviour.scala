package improbable.ai

import improbable.abyssal.ai.AIControls
import improbable.controls.ControlsOwnerHelper
import improbable.papi.entity.{Entity, EntityBehaviour}

class AIControlsBehaviour(entity: Entity, controlsOwnerHelper: ControlsOwnerHelper) extends EntityBehaviour {

  override def onReady(): Unit = {
    controlsOwnerHelper.delegateControlToCurrentOwner[AIControls]()
  }
}
