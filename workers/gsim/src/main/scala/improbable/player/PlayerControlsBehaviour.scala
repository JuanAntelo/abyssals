package improbable.player

import improbable.abyssal.labels.SlotPosition
import improbable.abyssal.player.PlayerControls
import improbable.corelib.slots.SlotContainer
import improbable.corelib.util.EntityOwnerDelegation._
import improbable.equipment.{StopUsingItem, StartUsingItem}
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

class PlayerControlsBehaviour(world: World, entity: Entity, slotContainer: SlotContainer) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateStateToOwner[PlayerControls]

    entity.watch[PlayerControls].onStartUsingLeftHand {
      _ =>
        startUsingItem(SlotPosition.LEFTHAND.toString)
    }

    entity.watch[PlayerControls].onStopUsingLeftHand {
      _ =>
        stopUsingItem(SlotPosition.LEFTHAND.toString)
    }

    entity.watch[PlayerControls].onStartUsingRightHand {
      _ =>
        startUsingItem(SlotPosition.RIGHTHAND.toString)
    }

    entity.watch[PlayerControls].onStopUsingRightHand {
      _ =>
        stopUsingItem(SlotPosition.RIGHTHAND.toString)
    }
  }

  private def startUsingItem(slot: String): Unit = {
    slotContainer.mountedAt(slot).foreach {
      item =>
        world.messaging.sendToEntity(item, StartUsingItem)
    }
  }

  private def stopUsingItem(slot: String): Unit = {
    slotContainer.mountedAt(slot).foreach {
      item =>
        world.messaging.sendToEntity(item, StopUsingItem)
    }
  }
}
