package improbable.gamemaster

import improbable.abyssal.gamemaster.GameMasterControls
import improbable.corelib.util.EntityOwnerDelegation._
import improbable.papi.entity.{Entity, EntityBehaviour}

class GameMasterControlsBehaviour(entity: Entity) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateStateToOwner[GameMasterControls]
  }

}
