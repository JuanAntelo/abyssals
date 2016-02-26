package improbable.ai

import improbable.abyssal.light.{LightSource, VisibleLightSourcesTriggers, VisibleLightSourcesWriter}
import improbable.papi.EntityId
import improbable.papi.entity.behaviour.EntityBehaviourInterface
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.unity.fabric.PhysicsEngineConstraint

trait LightFinder extends EntityBehaviourInterface {
  def findBestLightTarget(): Option[EntityId]
}

class LightFinderBehaviour(world: World, entity: Entity, visibleLightSourcesWriter: VisibleLightSourcesWriter) extends EntityBehaviour with LightFinder {

  private val lightTriggers = entity.watch[VisibleLightSourcesTriggers]

  override def onReady(): Unit = {
    entity.delegateState[VisibleLightSourcesTriggers](PhysicsEngineConstraint)

    lightTriggers.onDetectedLight {
      detected =>
        val newLights = visibleLightSourcesWriter.lightSources :+ detected.entityId
        visibleLightSourcesWriter.update.lightSources(newLights).finishAndSend()
    }

    lightTriggers.onLostLight {
      lost =>
        val newLights = visibleLightSourcesWriter.lightSources.filterNot(_ == lost.entityId)
        visibleLightSourcesWriter.update.lightSources(newLights).finishAndSend()
    }
  }

  override def findBestLightTarget(): Option[EntityId] = {
    cleanLightSources()
    val snapshots = visibleLightSourcesWriter.lightSources.flatMap(light => world.entities.find(light).toList)
    if (snapshots.nonEmpty) {
      Some(snapshots.minBy {
        light =>
          light.position.distanceTo(entity.position)
      }.entityId)
    }
    else {
      None
    }
  }

  private def cleanLightSources(): Unit = {
    val newLights = visibleLightSourcesWriter.lightSources.filter {
      light =>
        val snapshot = world.entities.find(light)
        snapshot.exists(_.get[LightSource].exists(_.on))
    }
    visibleLightSourcesWriter.update.lightSources(newLights).finishAndSend()
  }
}
