package improbable.natures.dev

import improbable.abyssal.dev.{EntityInfoModifier, InspectedEntitiesInfo, InspectorEntitySelectorControls}
import improbable.corelib.natures.{NatureApplication, NatureDescription}
import improbable.dev.{ModifyEntityInfoBehaviour, ListenForDevInfoBehaviour}
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object EntityInspector extends NatureDescription {
  def apply(): NatureApplication = {
    application(
      states = Seq(
        InspectorEntitySelectorControls(),
        InspectedEntitiesInfo(Map.empty),
        EntityInfoModifier(canModify = true)
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set.empty

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[ListenForDevInfoBehaviour],
    descriptorOf[ModifyEntityInfoBehaviour]
  )
}
