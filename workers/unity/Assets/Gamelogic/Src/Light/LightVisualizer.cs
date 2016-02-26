using Improbable.Abyssal.Light;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Light
{
    class LightVisualizer : MonoBehaviour
    {
        [Require] public LightSourceReader LightSource;

        public GameObject Light;

        public void OnEnable()
        {
            LightSource.OnUpdated += LightUpdated;
        }

        private void LightUpdated(bool on)
        {
            Light.SetActive(on);
        }
    }
}
