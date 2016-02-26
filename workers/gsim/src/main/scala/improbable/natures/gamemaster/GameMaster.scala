package improbable.natures.gamemaster

import improbable.abyssal.clients.ClientType
import improbable.abyssal.gamemaster.GameMasterControls
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.corelib.util.EntityOwner
import improbable.gamemaster.{GameMasterBuilderBehaviour, GameMasterControlsBehaviour}
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.base.NatureBase
import improbable.natures.dev.EntityInspector
import improbable.papi.engine.EngineId
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.player.ClientSideMovementControlsBehaviour

object GameMaster extends NatureDescription {
  def apply(position: Coordinates, rotation: Vector3d, engineId: EngineId): NatureApplication = {
    application(
      states = Seq(
        EntityOwner(Some(engineId)),
        GameMasterControls()
      ),
      natures = Seq(
        NatureBase(
          position = position,
          rotation = rotation,
          name = ClientType.GAMEMASTER + "." + engineId,
          tags = Nil,
          isPhysical = true,
          isVisual = true
        ),
        EntityInspector()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(NatureBase, EntityInspector)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[ClientSideMovementControlsBehaviour],
    descriptorOf[GameMasterBuilderBehaviour],
    descriptorOf[GameMasterControlsBehaviour]
  )
}