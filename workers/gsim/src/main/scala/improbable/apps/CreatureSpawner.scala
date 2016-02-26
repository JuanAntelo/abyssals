package improbable.apps

import improbable.math.Vector3d
import improbable.natures.creatures.Spider
import improbable.papi.entity.EntityPrefab
import improbable.papi.world.AppWorld
import improbable.papi.worldapp.WorldApp
import improbable.settings.WorldMapSettings

class CreatureSpawner(world: AppWorld) extends WorldApp {

  val maxCreatures = 1

  val position = WorldMapSettings.spawnCenter + Vector3d(0, 0, 10)
  val rotation = Vector3d(0, 180, 0)

  0.until(maxCreatures).foreach { _ =>
    world.entities.spawnEntity(Spider(position, rotation).asEntityRecordTemplate(EntityPrefab("Spider")))
  }
}