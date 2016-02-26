package improbable.controls

import improbable.abyssal.controls.{Controls, ControlsOwnerType, ControlsOwnerWriter}
import improbable.corelib.util.EntityOwnerDelegation._
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.state.EntityStateAccessor
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.unity.fabric.PhysicsEngineConstraint

import scala.reflect.ClassTag

trait ControlsOwnerHelper extends EntityBehaviourInterface {
  def delegateControlToCurrentOwner[State <: EntityStateAccessor : ClassTag](): Unit
}

class ControlsOwnerBehaviour(entity: Entity, controlsOwnerWriter: ControlsOwnerWriter) extends EntityBehaviour with ControlsOwnerHelper {

  override def onReady(): Unit = {
    delegateControlToCurrentOwner[Controls]()
  }

  override def delegateControlToCurrentOwner[State <: EntityStateAccessor : ClassTag](): Unit = {
    controlsOwnerWriter.ownerType match {
      case ControlsOwnerType.CLIENT =>
        entity.delegateStateToOwner[State]
      case ControlsOwnerType.FSIM =>
        entity.delegateState[State](PhysicsEngineConstraint)
    }
  }
}
