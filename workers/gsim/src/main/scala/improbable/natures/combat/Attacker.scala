package improbable.natures.combat

import improbable.abyssal.combat.{AttackControls, AttackData}
import improbable.combat.AttackerBehaviour
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Attacker extends NatureDescription {
  def apply(attackRange: Float, cooldown: Float): NatureApplication = {
    application(
      states = Seq(
        AttackControls(),
        AttackData(
          attackRange = attackRange,
          cooldown = cooldown,
          cooldownRemaining = 0f,
          hitEnemies = Nil
        )
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[AttackerBehaviour]
  )
}
