package improbable.apps

import com.typesafe.scalalogging.Logger
import improbable.abyssal.clients.ClientType
import improbable.abyssal.clients.ClientType.ClientType
import improbable.abyssal.player.ArmorType
import improbable.apps.PlayerLifeCycleManager._
import improbable.dev.{AddEntity, RemoveEntity}
import improbable.gameentity.Destroy
import improbable.math.{Coordinates, Vector3d}
import improbable.natures.dev.Dev
import improbable.natures.gamemaster.GameMaster
import improbable.natures.player.{DummyPlayer, Player}
import improbable.papi._
import improbable.papi.engine.EngineId
import improbable.papi.entity.EntityPrefab
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.{EngineConnected, EngineDisconnected}
import improbable.papi.worldapp.{WorldApp, WorldAppLifecycle}
import improbable.player.RedelegateControl

import scala.pickling._
import scala.util.Random

case class ParsedMetadata(ClientType:Int)
case class MetadataContainer(data:String)

class PlayerLifeCycleManager(world: AppWorld,
                             logger: Logger,
                             lifecycle: WorldAppLifecycle) extends WorldApp {

  private var userIdToEntityIdMap = Map[EngineId, EntityId]()
  private var userIdToDummyPlayerId = Map[String, EntityId]()

  private var numPlayers = 0
  private var devs = List[EntityId]()
  private var entityToClientType = Map.empty[EntityId, ClientType]

  world.messaging.subscribe {
    case engineConnectedMsg: EngineConnected =>
      engineConnected(engineConnectedMsg)

    case engineDisconnectedMsg: EngineDisconnected =>
      engineDisconnected(engineDisconnectedMsg)
  }

  private def engineConnected(msg: EngineConnected): Unit = {
    msg match {
      // For now use the engineName as the userId.
      case EngineConnected(userId, UNITY_CLIENT, metadata) =>
        addEntity(userId, msg.metadata)
      case _ =>
    }
  }

  private def addEntity(engineId: String, metadata: String): Unit = {
    val parsedMeta = getParsedMetadata(metadata)
    parsedMeta.foreach {
      metad =>
        val clientType = ClientType(metad.ClientType)
        val entityId = clientType match {
          case ClientType.PLAYER =>
            makeNewPlayer(engineId)
          case ClientType.GAMEMASTER =>
            makeNewGameMaster(engineId)
          case ClientType.DEV =>
            makeNewDev(engineId)
          case ClientType.DUMMY =>
            makeNewDummyPlayer(engineId)
        }
        userIdToEntityIdMap += engineId -> entityId
        entityToClientType += (entityId -> clientType)

        addClientToDevInfo(clientType, entityId, engineId)
        delegateControlToPlayer(engineId, entityId)
    }
  }

  private def delegateControlToPlayer(engineId: String, entityId: EntityId): Unit = {
    world.messaging.sendToEntity(entityId, RedelegateControl(Some(engineId)))
    logger.info(s"Spawning Player with userId $engineId and entityId $entityId")
  }

  private def getParsedMetadata(metadata: String): Option[ParsedMetadata] = {
    try {
      implicit val e = json.pickleFormat
      val metad = scala.pickling.json.toJSONPickle(metadata).unpickle[MetadataContainer]
      val parsedMd = scala.pickling.json.toJSONPickle(metad.data).unpickle[ParsedMetadata]
      Some(parsedMd)
    } catch {
      case exception: Throwable =>
        logger.error("Tried to unpickle the metadata but was unable to! Are you using an incompatible client version?", exception)
        None
    }
  }

  private def makeNewPlayer(engineId: EngineId): EntityId = {
    val armorTypeIndex = numPlayers % ArmorType.maxId
    val armorType = ArmorType.values.toList(armorTypeIndex)
    val position = Coordinates(0, 0, -10)
    val rotation = Vector3d.zero
    numPlayers += 1
    world.entities.spawnEntity(Player(position, rotation, engineId, armorType).asEntityRecordTemplate(EntityPrefab("Player")))
  }

  private def makeNewGameMaster(engineId: String): EntityId = {
    val position = Coordinates(13.82, 20.25, -.383)
    val rotation = Vector3d(50.32, 289.24, -5.34)
    world.entities.spawnEntity(GameMaster(position, rotation, engineId).asEntityRecordTemplate(EntityPrefab("GameMaster")))
  }

  private def makeNewDev(engineId: String): EntityId = {
    val position = Coordinates(0, 100, 0)
    val rotation = Vector3d.zero
    val id = world.entities.spawnEntity(Dev(position, rotation, engineId).asEntityRecordTemplate(EntityPrefab("Dev")))
    devs = devs :+ id
    entityToClientType.foreach {
      case (clientEntityId, clientType) =>
        val clientEngineId = userIdToEntityIdMap.find(_._2 == clientEntityId).get._1
        println("Adding to dev: " + clientType)
        addClientToDevInfo(clientType, clientEntityId, clientEngineId)
    }
    id
  }

  val rand = Random

  private def makeNewDummyPlayer(engineId: EngineId): EntityId = {
    val armorTypeIndex = rand.nextInt(ArmorType.maxId)
    val armorType = ArmorType.values.toList(armorTypeIndex)
    logger.info("Spawning dummy player without entity id")
    val x = rand.nextFloat() * 500f - 250f
    val z = rand.nextFloat() * 500f - 250f
    val position = Coordinates(x, 0, z)
    world.entities.spawnEntity(DummyPlayer(position, Vector3d.zero, engineId, armorType).asEntityRecordTemplate(EntityPrefab("Player")))
  }

  private def engineDisconnected(msg: EngineDisconnected): Unit = {
    msg match {
      case EngineDisconnected(userId, UNITY_CLIENT) =>
        removeUserIdToEntityIdEntry(userId)
      case _ =>
    }
  }

  private def removeUserIdToEntityIdEntry(userId: EngineId) = {
    userIdToEntityIdMap.get(userId) match {
      case Some(id) =>
        removeEntityFromDevInfo(id)
        world.messaging.sendToEntity(id, Destroy())
        userIdToEntityIdMap = userIdToEntityIdMap.filterNot(_._1 == id)
        logger.info(s"Destroying player: $userId with entityId $id")
      case None =>
        logger.warn(s"User disconnected but could not find entity id for player: $userId")
    }
  }

  private def addClientToDevInfo(clientType: ClientType, entityId: EntityId, engineId: String): Unit = {
    devs.foreach {
      dev =>
        world.messaging.sendToEntity(dev, AddEntity(clientType, entityId, engineId))
    }
  }

  private def removeClientInfoFromDev(clientType: ClientType, entityId: EntityId): Unit = {
    devs.foreach {
      dev =>
        world.messaging.sendToEntity(dev, RemoveEntity(clientType, entityId))
    }
  }

  private def removeEntityFromDevInfo(entityId: EntityId): Unit = {
    entityToClientType.get(entityId).foreach {
      clientType =>
        if (clientType == ClientType.DEV) {
          devs = devs.filterNot(_ == entityId)
        }
        removeClientInfoFromDev(clientType, entityId)
        entityToClientType = entityToClientType - entityId
    }
  }
}

object PlayerLifeCycleManager {
  private val UNITY_CLIENT = "UnityClient"
}