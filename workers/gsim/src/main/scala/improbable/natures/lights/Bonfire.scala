package improbable.natures.lights

import improbable.abyssal.labels.{NatureType, PrefabType}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.entity.physical.RigidbodyDataData.CollisionDetectionMode
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.base.{BuildableNature, RigidBodyBase}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Bonfire extends BuildableNature(PrefabType.BONFIRE, NatureType.NBONFIRE) {

  override def apply(position: Coordinates, rotation: Vector3d, visual: Boolean = true, physical: Boolean = true): NatureApplication = {
    application(
      natures = Seq(
        RigidBodyBase(
          position = position,
          rotation = rotation,
          name = NatureType.NBONFIRE.toString,
          visual = visual,
          physical = physical,
          tags = Nil,
          mass = 5.0f,
          drag = 0.0f,
          collisionMode = CollisionDetectionMode.Continuous),
        Light(on = true)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(RigidBodyBase, Light)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set.empty
}
