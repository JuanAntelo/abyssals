package improbable.combat

import improbable.abyssal.combat.AttackeeWriter
import improbable.attributes.{Damage, HealthInterface}
import improbable.papi.entity.EntityBehaviour
import improbable.papi.world.World

class AttackedBehaviour(world: World, attackeeWriter: AttackeeWriter, healthInterface: HealthInterface) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case Damage(amount) =>
        healthInterface.changeHealth(-amount)
        attackeeWriter.update.triggerAttacked().finishAndSend()
    }
  }
}
