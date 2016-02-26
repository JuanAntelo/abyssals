package improbable.natures.lights

import improbable.abyssal.light.{LightSource, LightTriggers}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.light.LightBehaviour
import improbable.natures.debug.FSimDebuggable
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Light extends NatureDescription {
  def apply(on: Boolean = false): NatureApplication = {
    application(
      states = Seq(
        LightSource(on = on, players = Nil, parentPlayer = None),
        LightTriggers()
      ),
      natures = Seq(
        FSimDebuggable()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(FSimDebuggable)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[LightBehaviour]
  )
}
