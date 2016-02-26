package improbable.dev

import improbable.abyssal.clients.ClientType.ClientType
import improbable.abyssal.dev._
import improbable.papi.EntityId
import improbable.papi.engine.EngineId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class AddEntity(clientType: ClientType, entityId: EntityId, engineId: EngineId) extends CustomMsg

case class RemoveEntity(clientType: ClientType, entityId: EntityId) extends CustomMsg

class DevClientInfoBehaviour(world: World, entity: Entity, devClientInfoWriter: DevClientInfoWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case AddEntity(clientType, entityId, engineId) =>
        addNewConnectedClient(clientType, entityId, engineId)
      case RemoveEntity(clientType, entityId) =>
        removeConnectedClient(clientType, entityId)
    }
  }

  private def removeConnectedClient(clientType: ClientType, entityId: EntityId): Unit = {
    val newClients = devClientInfoWriter.clients.getOrElse(clientType, ClientTypeInfo(Map.empty)).clients.filterNot(_._1 == entityId)
    val newDevClientInfo = devClientInfoWriter.clients.updated(clientType, ClientTypeInfo(newClients))
    devClientInfoWriter.update.clients(newDevClientInfo).finishAndSend()
  }

  private def addNewConnectedClient(clientType: ClientType, entityId: EntityId, engineId: EngineId): Unit = {
    val clientInfo = ClientInfo(engineId)
    val newClients = devClientInfoWriter.clients.getOrElse(clientType, ClientTypeInfo(Map.empty)).clients.updated(entityId, clientInfo)
    val newDevClientInfo = devClientInfoWriter.clients.updated(clientType, ClientTypeInfo(newClients))
    devClientInfoWriter.update.clients(newDevClientInfo).finishAndSend()
  }
}
