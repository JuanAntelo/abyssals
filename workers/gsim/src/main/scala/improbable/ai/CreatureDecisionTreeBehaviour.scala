package improbable.ai

import improbable.Cancellable
import improbable.abyssal.attributes.Death
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

import scala.concurrent.duration._

class CreatureDecisionTreeBehaviour(world: World, entity: Entity,
                                    lightFinder: LightFinder,
                                    attackTargetFinder: CombatTargetFinder,
                                    pathingController: PathingController,
                                    attacker: AttackTargetInterface) extends EntityBehaviour {

  var decisionProcess = Cancellable()

  override def onReady(): Unit = {

    entity.watch[Death].bind.isDead {
      case true =>
        attackTargetFinder.clearAttackTarget()
        pathingController.stopFollowingTarget()
        decisionProcess.cancel()
      case false =>
        startDecisionProcess()
    }
  }

  private def startDecisionProcess(): Unit = {
    decisionProcess.cancel()
    decisionProcess = world.timing.every(1.second) {
      attackTargetFinder.updateBestAttackTarget()

      val bestAttackTarget = attackTargetFinder.getAttackTarget
//      val bestLightTarget = lightFinder.findBestLightTarget()
      if (bestAttackTarget.isDefined) {
        attacker.attackTarget(bestAttackTarget.get)
      }
//      else if (bestLightTarget.isDefined) {
//        bestLightTarget.foreach(pathingController.startFollowingTarget)
//      }
      else {
        pathingController.stopFollowingTarget()
      }
    }
  }
}
