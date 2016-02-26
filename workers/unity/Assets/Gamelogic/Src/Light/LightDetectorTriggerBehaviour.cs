using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Light
{
    [EngineType(EnginePlatform.FSim)]
    internal class LightDetectorTriggerBehaviour : MonoBehaviour
    {
        private LightDetectorVisualizer Visualizer;

        public void OnTriggerEnter(Collider other)
        {
            if (Visualizer == null)
            {
                Visualizer = GetComponentInParent<LightDetectorVisualizer>();
            }

            if (Visualizer != null)
            {
                var lightSource = other.gameObject.GetEntityObject();
                Visualizer.LightDetected(lightSource);
            }
        }

        public void OnTriggerExit(Collider other)
        {
            if (Visualizer != null)
            {
                var lightSource = other.gameObject.GetEntityObject();
                Visualizer.LightLeft(lightSource);
            }
        }
    }
}