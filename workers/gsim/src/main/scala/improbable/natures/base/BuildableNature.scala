package improbable.natures.base

import improbable.abyssal.labels.NatureType.NatureType
import improbable.abyssal.labels.PrefabType.PrefabType
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.math.{Coordinates, Vector3d}

abstract class BuildableNature(val prefab: PrefabType, val nature: NatureType) extends NatureDescription {

  def apply(position: Coordinates, rotation: Vector3d, visual: Boolean = true, physical: Boolean = true): NatureApplication

}
