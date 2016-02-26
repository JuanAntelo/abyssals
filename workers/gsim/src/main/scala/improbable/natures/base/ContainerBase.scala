package improbable.natures.base

import improbable.corelib.natures.{SlotsContainer, DynamicSlotsContainer, NatureDescription, NatureApplication}
import improbable.corelib.slots._
import improbable.hierarchy.ContainerBehaviour
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object ContainerBase extends NatureDescription {
  def apply(visualizedSlots: List[String]): NatureApplication = {
    application(
      states = Seq(
        HierarchyNode(None, Nil),
        VisualizedSlots(visualizedSlots)
      ),
      natures = Seq(
        SlotsContainer()
      )
    )
  }

  override def dependencies: Set[NatureDescription] = Set(SlotsContainer)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[SendsPositionToSlottedChildrenBehaviour],
    descriptorOf[HierarchyBehaviour],
    descriptorOf[ContainerBehaviour]
  )
}
