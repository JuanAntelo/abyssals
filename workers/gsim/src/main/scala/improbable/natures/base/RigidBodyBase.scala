package improbable.natures.base

import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.entity.physical.RigidbodyDataData.CollisionDetectionMode.CollisionDetectionMode
import improbable.entity.physical.RigidbodyDataData.InterpolationMode.InterpolationMode
import improbable.entity.physical.RigidbodyDataData.{CollisionDetectionMode, InterpolationMode}
import improbable.entity.physical._
import improbable.math.{Coordinates, Vector3d}

object RigidBodyBase extends NatureDescription {
  def apply(position: Coordinates,
            rotation: Vector3d,
            name: String,
            visual: Boolean = true,
            physical: Boolean = true,
            tags: List[String],
            mass: Float = 1.0f,
            drag: Float = 0.0f,
            angularDrag: Float = 0.0f,
            velocity: Vector3d = Vector3d.zero,
            angularVelocity: Vector3d = Vector3d.zero,
            positionConstraints: FreezeConstraints = FreezeConstraints(x = false, y = false, z = false),
            rotationConstraints: FreezeConstraints = FreezeConstraints(x = false, y = false, z = false),
            isKinematic: Boolean = false,
            hasGravity: Boolean = true,
            interpolationMode: InterpolationMode = InterpolationMode.None,
            collisionMode: CollisionDetectionMode = CollisionDetectionMode.Discrete): NatureApplication = {
    application(
      states = Seq(
        RigidbodyData(mass, force = Vector3d.zero, Vector3d.zero, drag, angularDrag, positionConstraints, rotationConstraints, hasGravity, isKinematic, interpolationMode, collisionMode),
        RigidbodyEngineData(velocity, angularVelocity, Vector3d.zero)
      ),
      natures = Seq(
        NatureBase(
          position = position,
          rotation = rotation,
          name = name,
          isPhysical = physical,
          isVisual = visual,
          tags)
      )
    )
  }

  override val dependencies: Set[NatureDescription] = Set(NatureBase)

  override val activeBehaviours = Set(
    descriptorOf[RigidbodyBehaviour]
  )
}