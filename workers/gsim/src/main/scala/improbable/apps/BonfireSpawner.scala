package improbable.apps

import improbable.abyssal.labels.NatureType
import improbable.math.{Coordinates, Vector3d}
import improbable.papi.world.AppWorld
import improbable.papi.worldapp.WorldApp
import improbable.spawning.BuilderHelper

class BonfireSpawner(world: AppWorld) extends WorldApp {

  BuilderHelper.spawnNature(world, NatureType.NBONFIRE, Coordinates.zero, Vector3d.zero)
}
