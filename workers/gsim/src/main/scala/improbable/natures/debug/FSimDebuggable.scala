package improbable.natures.debug

import improbable.abyssal.debug.FSimDebug
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.debug.FSimDebugBehaviour
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object FSimDebuggable extends NatureDescription {
  def apply(): NatureApplication = {
    application(
      states = Seq(
        FSimDebug()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[FSimDebugBehaviour]
  )
}
