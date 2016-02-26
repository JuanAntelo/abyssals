package improbable.hierarchy

import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class MountIntoParentOnReady(parent: EntityId, slot: String) extends CustomMsg

class ContainableBehaviour(world: World, entity: Entity) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case MountIntoParentOnReady(parent, slot) =>
        world.messaging.sendToEntity(parent, MountMe(entity.entityId, slot))
    }
  }
}
