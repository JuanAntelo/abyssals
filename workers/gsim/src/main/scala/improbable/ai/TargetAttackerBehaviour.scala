package improbable.ai

import improbable.abyssal.ai.CombatTarget
import improbable.abyssal.combat.{AttackData, AttackDataWriter}
import improbable.combat.AttackerController
import improbable.controls.ControlsOwnerHelper
import improbable.math.Coordinates
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

trait AttackTargetInterface extends EntityBehaviourInterface {
  def attackTarget(target: CombatTarget): Unit
}

class TargetAttackerBehaviour(world: World, entity: Entity, attackData: AttackDataWriter, controlsOwnerHelper: ControlsOwnerHelper, pathingController: PathingController, attackerController: AttackerController) extends EntityBehaviour with AttackTargetInterface {
  val attackSettings = entity.watch[AttackData]

  override def attackTarget(target: CombatTarget): Unit = {
    if (withinRangeForAttack(target.lastKnownLocation)) {
      attackerController.attackTarget(target.targetId)
    }
    else if (target.isVisible) {
      pathingController.startFollowingTarget(target.targetId)
    }
    else {
      pathingController.goToCoordinates(target.lastKnownLocation)
    }
  }

  private def withinRangeForAttack(target: Coordinates): Boolean = {
    target.distanceTo(entity.position) < attackSettings.attackRange.get
  }
}
