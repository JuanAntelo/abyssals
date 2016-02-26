package improbable.natures.combat

import improbable.abyssal.combat.Attackee
import improbable.combat.AttackedBehaviour
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Attackable extends NatureDescription {
  def apply(): NatureApplication = {
    application(
      states = Seq(
        Attackee()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[AttackedBehaviour]
  )
}