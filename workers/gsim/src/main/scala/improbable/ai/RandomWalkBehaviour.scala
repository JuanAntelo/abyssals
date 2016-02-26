package improbable.ai

import com.typesafe.scalalogging.Logger
import improbable.abyssal.controls.ControlsWriter
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

import scala.concurrent.duration._
import scala.util.Random

class RandomWalkBehaviour(world: World, entity: Entity, logger: Logger, controlsWriter: ControlsWriter) extends EntityBehaviour {

  val rand = Random

  override def onReady(): Unit = {
    world.timing.every(1.second) {
      val x = (rand.nextFloat() - 0.5f) * 2f
      val z = (rand.nextFloat() - 0.5f) * 2f
      controlsWriter.update.forward(z).right(x).finishAndSend()
    }
  }
}
