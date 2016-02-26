package improbable.attributes

import improbable.abyssal.attributes.{Health, HealthWriter}
import improbable.abyssal.dev.{EntityInfoListeners, EntityInfoType}
import improbable.dev.StateListenerUpdaterHelper
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class Damage(amount: Int) extends CustomMsg

trait HealthInterface extends EntityBehaviourInterface {
  def changeHealth(delta: Int): Unit
}

class HealthBehaviour(world: World, entity: Entity, healthWriter: HealthWriter) extends EntityBehaviour with HealthInterface {

  override def onReady(): Unit = {
    createHealthListenerUpdaters()
  }

  override def changeHealth(delta: Int): Unit = {
    val newHealth = Math.max(healthWriter.current + delta, 0)
    healthWriter.update.current(newHealth).finishAndSend()
  }

  private def createHealthListenerUpdaters(): Unit = {
    val stateListenerUpdaterHelper = new StateListenerUpdaterHelper(world, entity.entityId, entity.watch[EntityInfoListeners])

    stateListenerUpdaterHelper.addUpdaterForInfoType(
      EntityInfoType.HEALTHCURRENT,
      entity.watch[Health].bind.current,
      () => {
        entity.watch[Health].current.get
      },
      (newCurrent: Any) => {
        healthWriter.update.current(newCurrent.asInstanceOf[Int]).finishAndSend()
      }
    )

    stateListenerUpdaterHelper.addUpdaterForInfoType(
      EntityInfoType.HEALTHMAX,
      entity.watch[Health].bind.max,
      () => {
        entity.watch[Health].max.get
      },
      (newMax: Any) => {
        healthWriter.update.max(newMax.asInstanceOf[Int]).finishAndSend()
      }
    )
  }
}
