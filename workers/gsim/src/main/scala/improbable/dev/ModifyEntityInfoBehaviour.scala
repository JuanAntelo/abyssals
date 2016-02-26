package improbable.dev

import improbable.abyssal.dev.EntityInfoModifier
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.corelib.util.EntityOwnerDelegation._

class ModifyEntityInfoBehaviour(world: World, entity: Entity) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateStateToOwner[EntityInfoModifier]

    entity.watch[EntityInfoModifier].onSetIntValue {
      setInt =>
        world.messaging.sendToEntity(setInt.entityId, ChangeValue(setInt.infoType, setInt.newValue))
    }

    entity.watch[EntityInfoModifier].onSetFloatValue {
      setFloat =>
        world.messaging.sendToEntity(setFloat.entityId, ChangeValue(setFloat.infoType, setFloat.newValue))
    }

    entity.watch[EntityInfoModifier].onSetDoubleValue {
      setDouble =>
        world.messaging.sendToEntity(setDouble.entityId, ChangeValue(setDouble.infoType, setDouble.newValue))
    }
  }
}
