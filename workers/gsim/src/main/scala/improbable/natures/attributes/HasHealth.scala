package improbable.natures.attributes

import improbable.abyssal.attributes.{Death, Health}
import improbable.attributes.{DeathBehaviour, HealthBehaviour}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object HasHealth extends NatureDescription {
  def apply(maxHealth: Int): NatureApplication = {
    application(
      states = Seq(
        Health(current = maxHealth, max = maxHealth),
        Death(isDead = false)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[HealthBehaviour],
    descriptorOf[DeathBehaviour]
  )
}
