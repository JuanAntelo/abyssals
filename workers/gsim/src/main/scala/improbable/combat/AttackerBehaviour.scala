package improbable.combat

import improbable.Cancellable
import improbable.abyssal.ai.CombatTarget
import improbable.abyssal.combat._
import improbable.ai.PathingController
import improbable.attributes.Damage
import improbable.controls.ControlsOwnerHelper
import improbable.math.Coordinates
import improbable.papi.EntityId
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

import scala.concurrent.duration._

trait AttackerController extends EntityBehaviourInterface {
  def attackTarget(target: EntityId): Unit
}

class AttackerBehaviour(world: World, entity: Entity, attackData: AttackDataWriter, controlsOwnerHelper: ControlsOwnerHelper) extends EntityBehaviour with AttackerController {

  val attackSettings = entity.watch[AttackData]

  var cooldown = Cancellable()

  override def onReady(): Unit = {
    controlsOwnerHelper.delegateControlToCurrentOwner[AttackControls]()

    entity.watch[AttackControls].onHitEntity {
      hitEntity =>
        val target = hitEntity.target
        if (target != entity.entityId && !attackData.hitEnemies.contains(target)) {
          attackData.update.hitEnemies(attackData.hitEnemies :+ target).finishAndSend()
          world.messaging.sendToEntity(target, Damage(10))
        }
    }

    entity.watch[AttackControls].onResetHitEnemies {
      reset =>
        attackData.update.hitEnemies(Nil).finishAndSend()
    }
  }

  override def attackTarget(target: EntityId): Unit = {
    if (attackData.cooldownRemaining <= 0) {
      attackData.update
        .hitEnemies(Nil)
        .triggerTriggerAttack()
        .finishAndSend()
      startCooldown()
    }
  }

  private def startCooldown() = {
    cooldown.cancel()
    attackData.update.cooldownRemaining(attackData.cooldown).finishAndSend()
    cooldown = world.timing.every(100.milliseconds) {
      attackData.update.cooldownRemaining(attackData.cooldownRemaining - .1f).finishAndSend()

      if (attackData.cooldownRemaining <= 0) {
        cooldown.cancel()
      }
    }
  }
}
