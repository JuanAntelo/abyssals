using Improbable.Abyssal.Attributes;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Audio
{
    [EngineType(EnginePlatform.Client)]
    class ScaleAudioVisualizer : MonoBehaviour
    {
        [Require] public ScaleReader Scale;

        public void OnEnable()
        {
            Scale.CurrentUpdated += ScaleUpdated;
        }

        private void ScaleUpdated(float scale)
        {
            AkSoundEngine.SetRTPCValue("Spider_Size", scale, gameObject);
        }
    }
}
