using Improbable.Unity;
using Improbable.Unity.Core;
using Improbable.Unity.Export;
using UnityEngine;

namespace Assets.Gamelogic.Src.Audio
{
    [KeepOnExportedPrefab]
    public class PlayWwiseEventOnAnim : MonoBehaviour
    {
        private bool IsFSim;

        public void OnEnable()
        {
            IsFSim = EngineConfiguration.Instance.EngineType != "UnityFSim";
        }

        public void PlayEvent(AnimationEvent animationEventInfo)
        {
            if (IsFSim)
            {
                AkSoundEngine.PostEvent(animationEventInfo.stringParameter, gameObject);
            }
        }
    }
}
