using System.Collections;
using Improbable.Abyssal.Light;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.VFX
{
    [EngineType(EnginePlatform.Client)]
    class LightFXVisualizer : MonoBehaviour
    {
        [Require] public LightSourceReader LightSource;

        public PKFxFX PopcornEffect;

        public void OnEnable()
        {
            LightSource.OnUpdated += LightUpdated;
            StartCoroutine("WaitForEffectToBeLoaded");
        }

        public void OnDisable()
        {
            PopcornEffect.StopEffect();
        }

        private void LightUpdated(bool on)
        {
            if (on)
            {
                PopcornEffect.StartEffect();
            }
            else
            {
                PopcornEffect.StopEffect();
            }
        }

        IEnumerator WaitForEffectToBeLoaded()
        {
            yield return new WaitForSeconds(.1f);
            LightUpdated(LightSource.On);
        }
    }
}
