package improbable.attributes

import improbable.abyssal.attributes.{Health, DeathWriter}
import improbable.papi.entity.{Entity, EntityBehaviour}

class DeathBehaviour(entity: Entity, deathWriter: DeathWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.watch[Health].bind.current {
      current =>
        if (current <= 0) {
          deathWriter.update.isDead(true).finishAndSend()
        }
    }
  }
}
