package improbable.dev

import improbable.abyssal.dev.EntityInfoListenersWriter
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

class RegisterDevListenersBehaviour(world: World, entity: Entity, devInfoListenersWriter: EntityInfoListenersWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case AddChangeListener(listener) =>
        devInfoListenersWriter.update
          .listeners(devInfoListenersWriter.listeners :+ listener)
          .triggerListenerAdded(listener)
          .finishAndSend()
      case RemoveChangeListener(listener) =>
        devInfoListenersWriter.update
          .listeners(devInfoListenersWriter.listeners.filterNot(_ == listener))
          .finishAndSend()
    }
  }
}
