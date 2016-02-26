package improbable.player.tiny

import improbable.entity.physical.PositionUpdate
import improbable.math.Coordinates
import improbable.papi._
import improbable.tinyengine.TickingTinyBehaviour

import scala.concurrent.duration.FiniteDuration

class TinyClientWalkerBehaviour(entityId: EntityId) extends TickingTinyBehaviour[PositionUpdate] {
  private var currentPosition: Coordinates = _

  addStateCallbacks {
    case state: PositionUpdate =>
      state.value.foreach(currentPosition = _)
  }

  override def tick(timeElapsed: FiniteDuration): Unit = {
    updateState(PositionUpdate(entityId, value = Some(currentPosition)))
  }

  override protected def initializeInternal(initializeState: PositionUpdate): Unit = {
    initializeState.value.foreach(currentPosition = _)
  }
}
