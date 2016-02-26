using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Audio
{
    [EngineType(EnginePlatform.Client)]
    public class PlayerAudioFootstep : MonoBehaviour
    {
        public AkEvent RunFootstepAkEvent;
        public AkEvent WalkFootstepAkEvent;
        public float WeightThreshold = 0.5f;

        public void PlayRunFootstep(AnimationEvent AnimationEventInfo)
        {
            if (AnimationEventInfo.animatorClipInfo.weight > WeightThreshold)
            {
                if (RunFootstepAkEvent != null)
                {
                    AkSoundEngine.PostEvent((uint) RunFootstepAkEvent.eventID, gameObject);
                }
            }
        }

        public void PlayWalkFootstep(AnimationEvent AnimationEventInfo)
        {
            if (AnimationEventInfo.animatorClipInfo.weight > WeightThreshold)
            {
                if (WalkFootstepAkEvent != null)
                {
                    AkSoundEngine.PostEvent((uint) WalkFootstepAkEvent.eventID, gameObject);
                }
            }
        }
    }
}