package improbable.natures.creatures

import improbable.abyssal.controls.ControlsOwnerType
import improbable.abyssal.labels.{NatureType, PrefabType}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.ai.AIBase
import improbable.natures.attributes.HasHealth
import improbable.natures.base.{StaticBase, BuildableNature}
import improbable.natures.controls.Controllable
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Spider extends BuildableNature(PrefabType.SPIDER, NatureType.NSPIDER) {
  override def apply(position: Coordinates, rotation: Vector3d, visual: Boolean = true, physical: Boolean = true): NatureApplication = {
    application(
      natures = Seq(
        StaticBase(
          position = position,
          rotation = rotation,
          name = NatureType.NSPIDER.toString,
          tags = Nil),
        HasHealth(100),
        AIBase(attackRange = 3f, stoppingDistance = 2.5f, cooldown = 1.2f),
        Controllable(ControlsOwnerType.FSIM)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(StaticBase, HasHealth, AIBase, Controllable)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set.empty
}
