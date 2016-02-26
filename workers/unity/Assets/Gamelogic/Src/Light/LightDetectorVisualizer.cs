using Improbable.Abyssal.Light;
using Improbable.Unity;
using Improbable.Unity.Entity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Light
{
    [EngineType(EnginePlatform.FSim)]
    internal class LightDetectorVisualizer : MonoBehaviour
    {
        [Require] public VisibleLightSourcesTriggersWriter LightTriggers;

        public void LightDetected(IEntityObject lightSource)
        {
            var lightId = lightSource.EntityId;
            LightTriggers.Update.TriggerDetectedLight(lightId).FinishAndSend();
        }

        public void LightLeft(IEntityObject lightSource)
        {
            var lightId = lightSource.EntityId;
            LightTriggers.Update.TriggerLostLight(lightId).FinishAndSend();
        }
    }
}