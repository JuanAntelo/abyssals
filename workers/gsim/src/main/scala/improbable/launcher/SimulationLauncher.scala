package improbable.launcher

import improbable.dapi.{Launcher, LaunchConfig}

/**
 * Run this to start your Simulation!
 */
object SimulationLauncherWithManualEngines extends SimulationLauncher(SimulationLaunchWithManualEngineStartupConfig)
object SimulationLauncherWithAutomaticEngines extends SimulationLauncher(SimulationLaunchWithAutomaticEngineStartupConfig)

class SimulationLauncher(launchConfig: LaunchConfig) extends App {
  val options = Seq(
    "--entity_activator=improbable.corelib.entity.CoreLibraryEntityActivator",
    "--use_spatial_build_workflow=true",
    "--resource_based_config_name=one-gsim-one-jvm"
  )
  Launcher.startGame(launchConfig, options: _*)
}