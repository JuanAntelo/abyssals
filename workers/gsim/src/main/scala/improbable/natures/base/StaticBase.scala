package improbable.natures.base

import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.math.{Coordinates, Vector3d}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object StaticBase extends NatureDescription {
  def apply(position: Coordinates,
            rotation: Vector3d = Vector3d.zero,
            name: String,
            tags: List[String] = Nil): NatureApplication = {
    application(
      natures = Seq(
        NatureBase(
          position = position,
          rotation = rotation,
          name = name,
          isPhysical = true,
          isVisual = true,
          tags)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(NatureBase)

  override val activeBehaviours: Set[EntityBehaviourDescriptor] = Set.empty
}