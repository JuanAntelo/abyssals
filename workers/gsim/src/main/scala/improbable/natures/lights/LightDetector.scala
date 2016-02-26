package improbable.natures.lights

import improbable.abyssal.light.{VisibleLightSourcesTriggers, VisibleLightSources}
import improbable.ai.LightFinderBehaviour
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object LightDetector extends NatureDescription {
  def apply(): NatureApplication = {
    application(
      states = Seq(
        VisibleLightSources(Nil),
        VisibleLightSourcesTriggers()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[LightFinderBehaviour]
  )
}
