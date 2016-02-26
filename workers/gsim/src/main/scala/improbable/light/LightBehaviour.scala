package improbable.light

import improbable.abyssal.light.{LightSourceWriter, LightTriggers}
import improbable.corelib.slots.HierarchyNode
import improbable.equipment.StartUsingItem
import improbable.natures.player.Player
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.unity.fabric.PhysicsEngineConstraint

class LightBehaviour(world: World, entity: Entity, lightSourceWriter: LightSourceWriter) extends EntityBehaviour {

  private val lightTriggers = entity.watch[LightTriggers]

  override def onReady(): Unit = {
    entity.delegateState[LightTriggers](PhysicsEngineConstraint)

    world.messaging.onReceive {
      case StartUsingItem =>
        toggleLight()
    }

    lightTriggers.onEntityEnteredLight {
      event =>
        entityEnteredLight(event.entityId)
    }

    lightTriggers.onEntityExitedLight {
      event =>
        entityExitedLight(event.entityId)
    }

    entity.watch[HierarchyNode].bind.parent {
      case Some(parent) =>
        addParent(parent)
      case None =>
        removeParent()
    }
  }

  private def toggleLight(): Unit = {
    lightSourceWriter.update.on(!lightSourceWriter.on).finishAndSend()
    if (lightSourceWriter.on && lightSourceWriter.parentPlayer.isDefined) {
      lightSourceWriter.update.players(lightSourceWriter.players :+ lightSourceWriter.parentPlayer.get).finishAndSend()
    }
    else if (!lightSourceWriter.on) {
      lightSourceWriter.update.players(Nil).finishAndSend()
    }
  }

  private def entityEnteredLight(other: EntityId): Unit = {
    val otherSnapshot = world.entities.find(other)
    val isPlayer = otherSnapshot.exists(_.tags.contains(Player.playerTag))
    if (isPlayer) {
      lightSourceWriter.update.players(lightSourceWriter.players :+ other).finishAndSend()
    }
  }

  private def entityExitedLight(other: EntityId): Unit = {
    lightSourceWriter.update.players(lightSourceWriter.players.filterNot(_ == other)).finishAndSend()
  }

  private def addParent(parent: EntityId): Unit = {
    val parentSnapshot = world.entities.find(parent)
    val parentIsPlayer = parentSnapshot.exists(_.tags.contains(Player.playerTag))
    if (parentIsPlayer) {
      lightSourceWriter.update.parentPlayer(Some(parent)).finishAndSend()
      if (lightSourceWriter.on) {
        lightSourceWriter.update.players(lightSourceWriter.players :+ parent)
      }
    }
  }

  private def removeParent(): Unit = {
    if (lightSourceWriter.parentPlayer.isDefined) {
      lightSourceWriter.update
        .players(lightSourceWriter.players.filterNot(_ == lightSourceWriter.parentPlayer.get))
        .parentPlayer(None)
        .finishAndSend()
    }
  }
}
