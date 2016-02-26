package improbable.attributes

import improbable.abyssal.attributes.{Scale, ScaleWriter}
import improbable.abyssal.dev.{EntityInfoListeners, EntityInfoType}
import improbable.dev.StateListenerUpdaterHelper
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

class ScaleBehaviour(world: World, entity: Entity, scaleWriter: ScaleWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    val stateListenerUpdaterHelper = new StateListenerUpdaterHelper(world, entity.entityId, entity.watch[EntityInfoListeners])

    stateListenerUpdaterHelper.addUpdaterForInfoType(
      EntityInfoType.SCALE,
      entity.watch[Scale].bind.current,
      () => {
        entity.watch[Scale].current.get
      },
      (newCurrent: Any) => {
        scaleWriter.update.current(newCurrent.asInstanceOf[Float]).finishAndSend()
      }
    )
  }
}
