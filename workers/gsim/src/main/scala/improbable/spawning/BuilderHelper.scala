package improbable.spawning

import improbable.abyssal.labels.NatureType
import improbable.abyssal.labels.NatureType.NatureType
import improbable.hierarchy.MountIntoParentOnReady
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.base.BuildableNature
import improbable.natures.creatures.Spider
import improbable.natures.lights.{Bonfire, Torch}
import improbable.natures.weapons.Sword
import improbable.papi.EntityId
import improbable.papi.entity.EntityPrefab
import improbable.papi.world.World

object BuilderHelper {

  val buildableObjects = Map(
    NatureType.NSWORD -> Sword,
    NatureType.NSPIDER -> Spider,
    NatureType.NTORCH -> Torch,
    NatureType.NBONFIRE -> Bonfire
  )

  def spawnNature(world: World, nature: NatureType, position: Coordinates, rotation: Vector3d): EntityId = {
    val natureDescription = getBuildableNature(nature)
    val prefab = EntityPrefab(natureDescription.prefab.toString)
    val template = natureDescription(position, rotation).asEntityRecordTemplate(prefab)
    world.entities.spawnEntity(template)
  }

  def spawnNatureIntoSlot(world: World, nature: NatureType, position: Coordinates, rotation: Vector3d, parent: EntityId, slot: String): EntityId = {
    val natureDescription = getBuildableNature(nature)
    val prefab = EntityPrefab(natureDescription.prefab.toString)
    val template = natureDescription(position, rotation, visual = false, physical = false).asEntityRecordTemplate(prefab)
    val entity = world.entities.spawnEntity(template)
    world.messaging.sendToEntity(entity, MountIntoParentOnReady(parent, slot))
    entity
  }

  private def getBuildableNature(nature: NatureType): BuildableNature = {
    buildableObjects(nature)
  }
}
