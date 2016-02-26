package improbable.dev

import improbable.Cancellable
import improbable.abyssal.dev.EntityInfoListeners
import improbable.abyssal.dev.EntityInfoType.EntityInfoType
import improbable.papi.EntityId
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class ChangeValue(infoType: EntityInfoType, newValue: Any) extends CustomMsg

class StateListenerUpdaterHelper(world: World, entityId: EntityId, listeners: EntityInfoListeners#WatcherType) {

  type ValueWatcher = ((Any) => Unit) => Cancellable
  type ValueGetter = () => Any
  type ValueSetter = (Any) => Unit

  var typeToGetter = Map.empty[EntityInfoType, ValueGetter]
  var typeToSetter = Map.empty[EntityInfoType, ValueSetter]

  listeners.onListenerAdded {
    listenerAdded =>
      val listener = listenerAdded.listener
      typeToGetter.foreach {
        case (infoType, getter) =>
          world.messaging.sendToEntity(listener, ValueChanged(entityId, infoType, getter()))
      }
  }

  world.messaging.onReceive {
    case ChangeValue(infoType, newValue) =>
      typeToSetter.get(infoType).foreach(setter => setter(newValue))
  }

  def addUpdaterForInfoType(infoType: EntityInfoType, watcher: ValueWatcher, getter: ValueGetter, setter: ValueSetter): Unit = {
    typeToGetter = typeToGetter.updated(infoType, getter)
    typeToSetter = typeToSetter.updated(infoType, setter)

    watchForInfoTypeChange(infoType, watcher)
  }

  private def watchForInfoTypeChange(infoType: EntityInfoType, watcher: ValueWatcher): Cancellable = {
    watcher {
      value =>
        listeners.listeners.get.foreach {
          listener =>
            world.messaging.sendToEntity(listener, ValueChanged(entityId, infoType, value))
        }
    }
  }
}
