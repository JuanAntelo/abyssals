package improbable.natures.player

import improbable.abyssal.clients.ClientType
import improbable.abyssal.controls.ControlsOwnerType
import improbable.abyssal.hierarchy.StartingEquipment
import improbable.abyssal.labels.{NatureType, SlotPosition}
import improbable.abyssal.player.ArmorType.ArmorType
import improbable.abyssal.player.{Appearance, PlayerControls}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.corelib.util.EntityOwner
import improbable.entity.physical.RigidbodyDataData.CollisionDetectionMode
import improbable.entity.physical.FreezeConstraints
import improbable.hierarchy.StartingEquipmentBehaviour
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.attributes.HasHealth
import improbable.natures.base.{ContainerBase, RigidBodyBase}
import improbable.natures.combat.{Attackable, Attacker}
import improbable.natures.controls.Controllable
import improbable.natures.debug.FSimDebuggable
import improbable.papi.engine.EngineId
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.player.{ClientSideMovementControlsBehaviour, PlayerControlsBehaviour}

object Player extends NatureDescription {
  val playerTag = "Player"
  val visualizedSlots = List(SlotPosition.RIGHTHAND.toString, SlotPosition.LEFTHAND.toString)

  def apply(position: Coordinates, rotation: Vector3d, engineId: EngineId, armorType: ArmorType): NatureApplication = {
    application(
      states = Seq(
        EntityOwner(Some(engineId)),
        StartingEquipment(
          initialized = false,
          equipment = Map(
            SlotPosition.RIGHTHAND.toString -> NatureType.NSWORD,
            SlotPosition.LEFTHAND.toString -> NatureType.NTORCH
          )
        ),
        PlayerControls(),
        Appearance(armorType)
      ),
      natures = Seq(
        ContainerBase(visualizedSlots),
        RigidBodyBase(
          position = position,
          rotation = rotation,
          name = ClientType.PLAYER + "." + engineId,
          tags = List(playerTag),
          mass = 70.0f,
          drag = 0.0f,
          rotationConstraints = FreezeConstraints(x = true, y = true, z = true),
          collisionMode = CollisionDetectionMode.Continuous),
        HasHealth(100),
        FSimDebuggable(),
        Attacker(attackRange = 2f, cooldown = 1f),
        Attackable(),
        Controllable(ControlsOwnerType.CLIENT)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(ContainerBase, RigidBodyBase, HasHealth, FSimDebuggable, Attacker, Attackable, Controllable)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[ClientSideMovementControlsBehaviour],
    descriptorOf[StartingEquipmentBehaviour],
    descriptorOf[PlayerControlsBehaviour]
  )
}