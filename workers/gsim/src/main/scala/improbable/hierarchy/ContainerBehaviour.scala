package improbable.hierarchy

import improbable.corelib.physical.SetPhysicality
import improbable.corelib.slots.{SlotContainer, SlotVisibilityChange, VisualizedSlots}
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class UnmountMe(child: EntityId) extends CustomMsg

case class MountMe(child: EntityId, slot: String) extends CustomMsg

class ContainerBehaviour(world: World, entity: Entity, slotContainer: SlotContainer) extends EntityBehaviour {

  private val visualizedSlotsWatcher = entity.watch[VisualizedSlots]

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case UnmountMe(child) =>
        slotContainer.unmount(child)
      case MountMe(child, slot) =>
        slotContainer.mount(slot, child)
        if (visualizedSlotsWatcher.slots.get.contains(slot)) {
          world.messaging.sendToEntity(child, SlotVisibilityChange(entity.entityId, visibility = true))
          world.messaging.sendToEntity(child, SetPhysicality(physical = true))
        }
    }
  }
}
