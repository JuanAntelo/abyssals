package improbable.natures.dev

import improbable.abyssal.dev.EntityInfoListeners
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.dev.RegisterDevListenersBehaviour
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object InspectableByDevs extends NatureDescription {
  def apply(): NatureApplication = {
    application(
      states = Seq(
        EntityInfoListeners(Nil)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[RegisterDevListenersBehaviour]
  )
}
