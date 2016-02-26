package improbable.gamemaster

import improbable.abyssal.gamemaster.GameMasterControls
import improbable.abyssal.labels.NatureType.NatureType
import improbable.math.{Coordinates, Vector3d}
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.spawning.BuilderHelper

class GameMasterBuilderBehaviour(world: World, entity: Entity) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.watch[GameMasterControls].onBuildEntity {
      buildEntityEvent =>
        buildEntity(buildEntityEvent.nature, buildEntityEvent.position, buildEntityEvent.rotation)
    }
  }

  private def buildEntity(nature: NatureType, position: Coordinates, rotation: Vector3d): Unit = {
    BuilderHelper.spawnNature(world, nature, position, rotation)
  }
}
