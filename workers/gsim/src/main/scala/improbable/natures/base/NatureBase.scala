package improbable.natures.base

import improbable.abyssal.labels.Name
import improbable.corelib.natures.{NatureApplication, NatureDescription, Transformable}
import improbable.corelib.physical.{Physicality, PhysicalityControllerBehaviour}
import improbable.corelib.visual.{Visuality, VisualityControllerBehaviour}
import improbable.entity.physical.TagsData
import improbable.gameentity.DestructionBehaviour
import improbable.labels.NameBehaviour
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.dev.InspectableByDevs
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object NatureBase extends NatureDescription {
  def apply(position: Coordinates, rotation: Vector3d, name: String, isPhysical: Boolean, isVisual: Boolean, tags: List[String]): NatureApplication = {
    application(
      states = Seq(
        Name(name),
        TagsData(tags),
        Physicality(isPhysical),
        Visuality(isVisual)
      ),
      natures = Seq(
        Transformable(position, rotation),
        InspectableByDevs()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(Transformable, InspectableByDevs)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[DestructionBehaviour],
    descriptorOf[PhysicalityControllerBehaviour],
    descriptorOf[VisualityControllerBehaviour],
    descriptorOf[NameBehaviour]
  )
}
