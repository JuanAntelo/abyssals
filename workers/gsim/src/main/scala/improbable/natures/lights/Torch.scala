package improbable.natures.lights

import improbable.abyssal.labels.{NatureType, PrefabType}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.entity.physical.RigidbodyDataData.CollisionDetectionMode
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.base.{BuildableNature, ContainableBase, RigidBodyBase}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Torch extends BuildableNature(PrefabType.WEP_TORC_WOODENTORCH01, NatureType.NTORCH) {

  override def apply(position: Coordinates, rotation: Vector3d, visual: Boolean = true, physical: Boolean = true): NatureApplication = {
    application(
      natures = Seq(
        RigidBodyBase(
          position = position,
          rotation = rotation,
          name = NatureType.NTORCH.toString,
          visual = visual,
          physical = physical,
          tags = Nil,
          mass = 5.0f,
          drag = 0.0f,
          collisionMode = CollisionDetectionMode.Continuous),
        ContainableBase(),
        Light()
      ),
      states = Seq(

      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(RigidBodyBase, ContainableBase, Light)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(

  )

}
