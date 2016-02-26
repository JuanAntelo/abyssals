package bossa.travellers.launcher

import improbable.abyssal.clients.ClientType
import improbable.configuration.FlagzApp
import improbable.entity.physical.Position
import improbable.fapi.engine.EngineStartConfig
import improbable.fapi.network.{LinkSettings, MultiplexTcpLinkSettings, RakNetLinkSettings}
import improbable.papi._
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.state._
import improbable.player.tiny.TinyClientWalkerBehaviour
import improbable.tinyengine._
import improbable.tinyengine.fabric.TinyEngineDescriptor
import org.flagz
import org.flagz.{FlagContainer, FlagInfo, ScalaFlagz}

import scala.util.Random

object TinyDummyPlayer {
  private val WALKER_BEHAVIOURS = Map(
    (EntityPrefab("Player").name, classOf[Position].getName) -> ((entityId: EntityId) => new TinyClientWalkerBehaviour(entityId).asInstanceOf[TinyBehaviour[_ <: improbable.papi.entity.state.EntityStateUpdate]])
  )
  private val linkSettings = TinyDummyPlayerFlags.linkSettings.get match {
    case LinkSettings.RAKNET =>
      RakNetLinkSettings()
    case LinkSettings.TCP =>
      MultiplexTcpLinkSettings(TinyDummyPlayerFlags.tcpMultiplexLevel.get)
  }
  private var randomAlphanumStream = Random.alphanumeric

  def launchTinyRandomWalker(): Unit = {
    launch(s"Tiny-${TinyDummyPlayerFlags.aiTypeToSpawn.get()}-${randomString(20)}", TinyDummyPlayerFlags.aiTypeToSpawn.get(), behaviours())
  }

  private def launch(name: String, aiType: String, behaviours: Map[(PrefabName, CanonicalEntityStateName), BehaviourCreator]): Unit = {
    new TinyEngineDescriptor(
      enginePlatform = "UnityClient",
      behaviourCreators = behaviours,
      useInternalBridgeIp = false,
      linkSettings = linkSettings,
      metaData = metaData(name, aiType),
      streamUniverse = true).start(config(name))
  }

  private def config(name: String) = new EngineStartConfig(
    TinyDummyPlayerFlags.targetReceptionistIp.get,
    "Abyssal",
    "Abyssal",
    TinyDummyPlayerFlags.targetAssetDbUrl.get,
    TinyDummyPlayerFlags.apiHostname.get,
    name)

  private def metaData(name: String, aiType: String): String = {
    //Work around for https://issues.scala-lang.org/browse/SI-6476 -- can't escape double quotes in interpolated strings
    "{\"ClientType\":" + ClientType.DUMMY.id + "}"
  }

  private def randomString(len: Int): String = {
    val res = randomAlphanumStream.take(len).mkString
    randomAlphanumStream = randomAlphanumStream.drop(len)
    res
  }

  private def behaviours(): Map[(PrefabName, CanonicalEntityStateName), BehaviourCreator] = {
    WALKER_BEHAVIOURS
  }
}

object SpawnTinyAiPlayers extends FlagzApp {
  (0 until TinyDummyPlayerFlags.numberOfPlayersToSpawn.get()).foreach {
    _ =>
      TinyDummyPlayer.launchTinyRandomWalker()
      Thread.sleep(TinyDummyPlayerFlags.millisToWait.get())
  }
}

object TinyDummyPlayerFlags extends FlagContainer {
  val RANDOM_WALKER = "random_walker"

  @FlagInfo(name = "tiny_dummy_client_receptionist_ip", help = "The receptionist to connect to")
  val targetReceptionistIp = ScalaFlagz.valueOf("localhost")

  @FlagInfo(name = "tiny_dummy_client_assetDatabaseUrl", help = "The URL of the target asset database")
  val targetAssetDbUrl = ScalaFlagz.valueOf("localhost")

  @FlagInfo(name = "tiny_dummy_client_apiHostname", help = "The api hostname")
  val apiHostname = ScalaFlagz.valueOf("localhost")

  @FlagInfo(name = "tiny_dummy_client_link_settings", help = "The LinkSettings of the Universal Client (RAKNET or TCP)")
  val linkSettings = ScalaFlagz.valueOf("TCP")
    .withValidator(flagz.FuncToJavaPredicate(Set(LinkSettings.RAKNET, LinkSettings.TCP).contains))

  @FlagInfo(name = "tiny_dummy_client_tcp_multiplex_level", help = "Number of connections for Universal Client TCP connection")
  val tcpMultiplexLevel = ScalaFlagz.valueOf(1)

  @FlagInfo(name = "tiny_dummy_client_launch_delay", help = "Number of milliseconds to wait between spawning tiny clients")
  val millisToWait = ScalaFlagz.valueOf(2000L)

  @FlagInfo(name = "tiny_dummy_client_spawn_count", help = "Number of tiny dummy players to spawn")
  val numberOfPlayersToSpawn = ScalaFlagz.valueOf(20)

  @FlagInfo(name = "tiny_dummy_client_ai_type", help = "Which type of ai to spawn, i.e. `random_walker`")
  val aiTypeToSpawn = ScalaFlagz.valueOf("random_walker")
    .withValidator(flagz.FuncToJavaPredicate(Set(RANDOM_WALKER).contains))

  @FlagInfo(name = "tiny_dummy_client_walker_meters_per_second", help = "Speed of tiny walkers in meters per second")
  val walkerSpeed = ScalaFlagz.valueOf(5d)
}
