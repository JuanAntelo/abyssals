package improbable.natures.player

import improbable.abyssal.clients.ClientType
import improbable.abyssal.controls.Controls
import improbable.abyssal.hierarchy.StartingEquipment
import improbable.abyssal.labels.{NatureType, SlotPosition}
import improbable.abyssal.player.Appearance
import improbable.abyssal.player.ArmorType._
import improbable.ai.RandomWalkBehaviour
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.corelib.util.EntityOwner
import improbable.entity.physical.RigidbodyDataData.CollisionDetectionMode
import improbable.entity.physical.FreezeConstraints
import improbable.hierarchy.StartingEquipmentBehaviour
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.attributes.HasHealth
import improbable.natures.base.{ContainerBase, RigidBodyBase}
import improbable.natures.combat.Attackable
import improbable.papi.engine._
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object DummyPlayer extends NatureDescription {
  val visualizedSlots = List(SlotPosition.RIGHTHAND.toString, SlotPosition.LEFTHAND.toString)

  def apply(position: Coordinates, rotation: Vector3d, engineId: EngineId, armorType: ArmorType): NatureApplication = {
    application(
      states = Seq(
        EntityOwner(Some(engineId)),
        Controls(0, 0),
        StartingEquipment(
          initialized = false,
          equipment = Map(
            SlotPosition.RIGHTHAND.toString -> NatureType.NSWORD,
            SlotPosition.LEFTHAND.toString -> NatureType.NTORCH
          )
        ),
        Appearance(armorType)
      ),
      natures = Seq(
        RigidBodyBase(
          position = position,
          rotation = rotation,
          name = ClientType.PLAYER + "." + engineId,
          tags = List(Player.playerTag),
          mass = 70.0f,
          drag = 0.0f,
          rotationConstraints = FreezeConstraints(x = true, y = true, z = true),
          collisionMode = CollisionDetectionMode.Continuous),
        ContainerBase(visualizedSlots),
        HasHealth(100),
        Attackable()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(RigidBodyBase, ContainerBase, HasHealth, Attackable)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[RandomWalkBehaviour],
    descriptorOf[StartingEquipmentBehaviour]
  )
}
