package improbable.natures.base

import improbable.corelib.natures.{SlotItem, NatureApplication, NatureDescription}
import improbable.hierarchy.ContainableBehaviour
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object ContainableBase extends NatureDescription {
  def apply(): NatureApplication = {
    application(
      natures = Seq(
        SlotItem()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(SlotItem)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[ContainableBehaviour]
  )
}
