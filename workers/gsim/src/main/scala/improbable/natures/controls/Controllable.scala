package improbable.natures.controls

import improbable.abyssal.controls.{Controls, ControlsOwner}
import improbable.abyssal.controls.ControlsOwnerType.ControlsOwnerType
import improbable.controls.ControlsOwnerBehaviour
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Controllable extends NatureDescription {
  def apply(controlsOwnerType: ControlsOwnerType): NatureApplication = {
    application(
      states = Seq(
        ControlsOwner(controlsOwnerType),
        Controls(0f, 0f)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[ControlsOwnerBehaviour]
  )
}
