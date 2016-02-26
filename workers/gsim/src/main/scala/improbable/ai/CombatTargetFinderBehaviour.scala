package improbable.ai

import improbable.abyssal.ai.{CombatAIWriter, CombatTarget}
import improbable.abyssal.light.{LightSource, VisibleLightSources}
import improbable.natures.player.Player
import improbable.papi.EntityId
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour, EntitySnapshot}
import improbable.papi.world.World

trait CombatTargetFinder extends EntityBehaviourInterface {
  def updateBestAttackTarget(): Unit

  def getAttackTarget: Option[CombatTarget]

  def clearAttackTarget(): Unit
}

class CombatTargetFinderBehaviour(world: World, entity: Entity, combatAIWriter: CombatAIWriter) extends EntityBehaviour with CombatTargetFinder {

  private val lightSources = entity.watch[VisibleLightSources]
  private val lastKnownLocationDistance = 3
  private val senseDistance = 5d

  override def updateBestAttackTarget(): Unit = {
    val visibleTargets = findVisibleTargets
    val sensedTargets = findSensedTargets

    val targetSnapshot = combatAIWriter.target.flatMap(target => world.entities.find(target.targetId))
    if (targetSnapshot.isEmpty) {
      combatAIWriter.update.target(None).finishAndSend()
    }

    for {
      target <- combatAIWriter.target
      targetId = target.targetId
      targetSnapshot <- world.entities.find(targetId)
    } yield {
      if (visibleTargets.contains(targetId) || sensedTargets.exists(_.entityId == targetId)) {
        combatAIWriter.update.target(Some(
          CombatTarget(
            targetId = targetId,
            targetSnapshot.position,
            isVisible = true
          )))
          .finishAndSend()
      }
      else {
        if (isCloseToLastKnownLocation) {
          combatAIWriter.update.target(None).finishAndSend()
        }
        else {
          val newTarget = combatAIWriter.target.map(_.copy(isVisible = false))
          combatAIWriter.update.target(newTarget).finishAndSend()
        }
      }
    }

    if (combatAIWriter.target.isEmpty) {
      val newTarget = findBestTarget(visibleTargets, sensedTargets)
      val combatTarget = newTarget.map {
        target =>
          CombatTarget(target.entityId, target.position, isVisible = true)
      }
      combatAIWriter.update.target(combatTarget).finishAndSend()
    }
  }

  override def getAttackTarget: Option[CombatTarget] = {
    combatAIWriter.target
  }

  override def clearAttackTarget(): Unit = {
    combatAIWriter.update.target(None).finishAndSend()
  }

  private def findVisibleTargets: List[EntityId] = {
    lightSources.lightSources.get.flatMap {
      light =>
        val lightSource = world.entities.find(light)
        lightSource.flatMap(_.get[LightSource]).map(_.players).getOrElse(Nil)
    }
  }

  private def findSensedTargets: Seq[EntitySnapshot] = {
    world.entities.find(entity.position, senseDistance, Set(Player.playerTag))
  }

  private def findBestTarget(visibleTargets: List[EntityId], sensedTargets: Seq[EntitySnapshot]): Option[EntitySnapshot] = {
    val snapshots = visibleTargets.flatMap(target => world.entities.find(target).toList) ++ sensedTargets
    if (snapshots.nonEmpty) {
      Some(
        snapshots.minBy {
          snapshot =>
            snapshot.position.distanceTo(entity.position)
        }
      )
    }
    else {
      None
    }
  }

  private def isCloseToLastKnownLocation: Boolean = {
    combatAIWriter.target.exists(_.lastKnownLocation.distanceTo(entity.position) < lastKnownLocationDistance)
  }
}
