package improbable.hierarchy

import improbable.abyssal.hierarchy.StartingEquipmentWriter
import improbable.entity.physical.Rotation
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.spawning.BuilderHelper

class StartingEquipmentBehaviour(world: World, entity: Entity, startingEquipmentWriter: StartingEquipmentWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    if (!startingEquipmentWriter.initialized) {
      spawnEquipment()
      startingEquipmentWriter.update.initialized(true).finishAndSend()
    }
  }

  private def spawnEquipment(): Unit = {
    val rotation = entity.watch[Rotation].euler.get
    startingEquipmentWriter.equipment.foreach {
      case (slot, nature) =>
        BuilderHelper.spawnNatureIntoSlot(world, nature, entity.position, rotation, entity.entityId, slot)
    }
  }

}
