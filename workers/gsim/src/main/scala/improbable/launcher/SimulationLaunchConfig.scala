package improbable.launcher

import improbable.apps.{BonfireSpawner, CreatureSpawner, PlayerLifeCycleManager}
import improbable.dapi.LaunchConfig
import improbable.fapi.bridge._
import improbable.fapi.engine.CompositeEngineDescriptorResolver
import improbable.fapi.network.RakNetLinkSettings
import improbable.papi.worldapp.WorldApp
import improbable.unity.asset.PrefabContext
import improbable.unity.fabric.bridge.UnityFSimBridgeSettings
import improbable.unity.fabric.engine.{DownloadableUnityConstraintToEngineDescriptorResolver, EnginePlatform}
import improbable.unity.fabric.satisfiers.{AggregateSatisfiers, SatisfySingleConstraint, SatisfySpecificEngine}
import improbable.unity.fabric.{AuthoritativeEntityOnly, VisualEngineConstraint}

/**
 * These are the engine startup configs.
 *
 * ManualEngineStartup will not start an engines when you start the game.
 * AutomaticEngineStartup
 */
object SimulationLaunchWithManualEngineStartupConfig extends SimulationLaunchConfigWithApps(dynamicallySpoolUpEngines = false)

object SimulationLaunchWithAutomaticEngineStartupConfig extends SimulationLaunchConfigWithApps(dynamicallySpoolUpEngines = true)

/**
 * Use this class to specify the list of apps you want to run when the game starts.
 */
class SimulationLaunchConfigWithApps(dynamicallySpoolUpEngines: Boolean) extends SimulationLaunchConfig(
  Seq(
    classOf[PlayerLifeCycleManager],
    classOf[CreatureSpawner],
    classOf[BonfireSpawner]
  ),
  dynamicallySpoolUpEngines)

class SimulationLaunchConfig(appsToStart: Seq[Class[_ <: WorldApp]],
                             dynamicallySpoolUpEngines: Boolean) extends LaunchConfig(
  appsToStart,
  dynamicallySpoolUpEngines,
  DefaultBridgeSettingsResolver,
  DefaultConstraintEngineDescriptorResolver)

object DefaultBridgeSettingsResolver extends CompositeBridgeSettingsResolver(
  ClientBridgeSettings,
  UnityFSimBridgeSettings
)

object DefaultConstraintEngineDescriptorResolver extends CompositeEngineDescriptorResolver(
  DownloadableUnityConstraintToEngineDescriptorResolver
)

object ClientBridgeSettings extends BridgeSettingsResolver {

  private val CLIENT_ENGINE_BRIDGE_SETTINGS = BridgeSettings(
    AlwaysDefaultContextDescriminator(),
    RakNetLinkSettings(),
    EnginePlatform.UNITY_CLIENT_ENGINE,
    AggregateSatisfiers(
      SatisfySpecificEngine,
      SatisfySingleConstraint(VisualEngineConstraint)
    ),
    AuthoritativeEntityOnly(),
    ConstantEngineLoadPolicy(0.5),
    PerEntityOrderedStateUpdateQos
  )

  private val bridgeSettings = Map[String, BridgeSettings](
    EnginePlatform.UNITY_CLIENT_ENGINE -> CLIENT_ENGINE_BRIDGE_SETTINGS
  )

  override def engineTypeToBridgeSettings(engineType: String, metadata: String): Option[BridgeSettings] = {
    bridgeSettings.get(engineType)
  }
}

case class AlwaysDefaultContextDescriminator() extends AssetContextDiscriminator {
  override def assetContextForEntity(entity: EngineEntity): String = {
    PrefabContext.DEFAULT
  }
}
