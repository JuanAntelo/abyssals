package improbable.labels

import improbable.abyssal.dev.{EntityInfoListeners, EntityInfoType}
import improbable.abyssal.labels.{Name, NameWriter}
import improbable.dev.StateListenerUpdaterHelper
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

trait NameController extends EntityBehaviourInterface {
  def SetName(newName: String): Unit
}

class NameBehaviour(world: World, entity: Entity, nameWriter: NameWriter) extends EntityBehaviour with NameController {

  override def onReady(): Unit = {
    val stateListenerUpdaterHelper = new StateListenerUpdaterHelper(world, entity.entityId, entity.watch[EntityInfoListeners])

    stateListenerUpdaterHelper.addUpdaterForInfoType(
      EntityInfoType.DEVNAME,
      entity.watch[Name].bind.devName,
      () => {
        entity.watch[Name].devName.get
      },
      (newCurrent: Any) => {}
    )
  }

  override def SetName(newName: String): Unit = {
    nameWriter.update.devName(newName).finishAndSend()
  }
}
