package improbable.natures.ai

import improbable.abyssal.ai.{AIControls, CombatAI, MovementTarget}
import improbable.abyssal.attributes.Scale
import improbable.ai._
import improbable.attributes.ScaleBehaviour
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.math.Vector3d
import improbable.natures.combat.{Attackable, Attacker}
import improbable.natures.debug.FSimDebuggable
import improbable.natures.lights.LightDetector
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object AIBase extends NatureDescription {
  def apply(attackRange: Float, stoppingDistance: Float, cooldown: Float): NatureApplication = {
    application(
      states = Seq(
        CombatAI(None),
        MovementTarget(None, stoppingDistance = stoppingDistance),
        Scale(current = 1f),
        AIControls(Vector3d.zero)
      ),
      natures = Seq(
        LightDetector(),
        Attacker(
          attackRange = attackRange,
          cooldown = cooldown
        ),
        Attackable(),
        FSimDebuggable()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(LightDetector, Attacker, Attackable, FSimDebuggable)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[CreatureDecisionTreeBehaviour],
    descriptorOf[CombatTargetFinderBehaviour],
    descriptorOf[MovementTargetBehaviour],
    descriptorOf[ScaleBehaviour],
    descriptorOf[TargetAttackerBehaviour],
    descriptorOf[AIControlsBehaviour]
  )
}
