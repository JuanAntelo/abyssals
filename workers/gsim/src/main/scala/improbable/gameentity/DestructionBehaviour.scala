package improbable.gameentity

import improbable.corelib.slots.{HierarchyNode, Slots}
import improbable.hierarchy.UnmountMe
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class Destroy() extends CustomMsg

trait DestructionController extends EntityBehaviourInterface {
  def destroy(dropChildren: Boolean = false): Unit
}

class DestructionBehaviour(world: World, entity: Entity) extends EntityBehaviour with DestructionController {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case Destroy() =>
        destroy()
    }
  }

  override def destroy(dropChildren: Boolean = false): Unit = {
    unmountFromParent()
    if (dropChildren) {
      doDropChildren()
    }
    else {
      destroyChildren()
    }
    entity.destroy()
  }

  private def doDropChildren(): Unit = {
    entity.watch[Slots].slotToEntityId.foreach(_.foreach {
      case (_, child) =>
        // have to send message to self since we can't inject an optional interface
        world.messaging.sendToEntity(entity.entityId, UnmountMe(child))
    })
  }

  private def destroyChildren(): Unit = {
    entity.watch[Slots].slotToEntityId.foreach(_.foreach {
      case (_, child) =>
        world.messaging.sendToEntity(child, Destroy())
    })
  }

  private def unmountFromParent(): Unit = {
    entity.watch[HierarchyNode].parent.flatten.foreach(world.messaging.sendToEntity(_, UnmountMe(entity.entityId)))
  }
}
