package improbable.physical

import improbable.abyssal.physical.SyncedBones
import improbable.corelib.util.EntityOwnerDelegation._
import improbable.papi.entity.{Entity, EntityBehaviour}

class SyncedBonesBehaviour(entity: Entity) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateStateToOwner[SyncedBones]
  }
}
