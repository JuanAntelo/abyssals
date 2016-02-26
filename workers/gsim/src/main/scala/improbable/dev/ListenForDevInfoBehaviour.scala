package improbable.dev

import com.typesafe.scalalogging.Logger
import improbable.abyssal.dev.EntityInfoType.EntityInfoType
import improbable.abyssal.dev.EntityInfoType.EntityInfoType
import improbable.abyssal.dev.{EntityInfoType, EntityInfo, InspectedEntitiesInfoWriter, InspectorEntitySelectorControls}
import improbable.corelib.util.EntityOwnerDelegation._
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class AddChangeListener(listener: EntityId) extends CustomMsg

case class RemoveChangeListener(listener: EntityId) extends CustomMsg

case class ValueChanged(source: EntityId, infoType: EntityInfoType, newValue: Any) extends CustomMsg

class ListenForDevInfoBehaviour(world: World, entity: Entity, logger: Logger, inspectedEntitiesInfoWriter: InspectedEntitiesInfoWriter) extends EntityBehaviour {

  private val emptyEntityInfo = EntityInfo("", Map.empty, Map.empty, Map.empty)

  val devEntitySelectorControls = entity.watch[InspectorEntitySelectorControls]

  override def onReady(): Unit = {
    entity.delegateStateToOwner[InspectorEntitySelectorControls]

    world.messaging.onReceive {
      case ValueChanged(source, infoType, newValue) =>
        if (infoType == EntityInfoType.DEVNAME) {
          handleNameChange(source, newValue.asInstanceOf[String])
        }
        else {
          handleValueChange(source, infoType, newValue)
        }
    }

    devEntitySelectorControls.onSelectEntity {
      selectEntity =>
        world.messaging.sendToEntity(selectEntity.entity, AddChangeListener(entity.entityId))
    }

    devEntitySelectorControls.onDeselectEntity {
      deselectEntity =>
        world.messaging.sendToEntity(deselectEntity.entity, RemoveChangeListener(entity.entityId))
        val newInspectedEntities = inspectedEntitiesInfoWriter.inspectedEntities.filterNot(_._1 == deselectEntity.entity)
        inspectedEntitiesInfoWriter.update.inspectedEntities(newInspectedEntities).finishAndSend()
    }
  }

  private def handleNameChange(source: EntityId, newName: String): Unit = {
    val newEntityInfo = inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).copy(entityName = newName)
    val newInspectedEntities = inspectedEntitiesInfoWriter.inspectedEntities.updated(source, newEntityInfo)
    inspectedEntitiesInfoWriter.update.inspectedEntities(newInspectedEntities).finishAndSend()
  }

  private def handleValueChange(source: EntityId, infoType: EntityInfoType, newValue: Any): Unit = {
    val newEntityInfo = newValue match {
      case i: Int =>
        val newIntValues = inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).intValues.updated(infoType, newValue.asInstanceOf[Int])
        inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).copy(intValues = newIntValues)
      case f: Float =>
        val newFloatValues = inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).floatValues.updated(infoType, newValue.asInstanceOf[Float])
        inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).copy(floatValues = newFloatValues)
      case d: Double =>
        val newDoubleValues = inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).doubleValues.updated(infoType, newValue.asInstanceOf[Double])
        inspectedEntitiesInfoWriter.inspectedEntities.getOrElse(source, emptyEntityInfo).copy(doubleValues = newDoubleValues)
      case _ =>
        logger.warn("Unable to sync information " + newValue + " to inspector because we can't handle it's type!")
        emptyEntityInfo
    }

    val newInspectedEntities = inspectedEntitiesInfoWriter.inspectedEntities.updated(source, newEntityInfo)
    inspectedEntitiesInfoWriter.update.inspectedEntities(newInspectedEntities).finishAndSend()
  }
}
