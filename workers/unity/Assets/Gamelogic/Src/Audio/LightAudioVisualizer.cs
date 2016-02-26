using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Improbable.Abyssal.Light;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Audio
{
    [EngineType(EnginePlatform.Client)]
    class LightAudioVisualizer : MonoBehaviour
    {
        [Require] public LightSourceReader LightSource;

        public AkEvent StartSoundEvent;
        public AkEvent StopSoundEvent;

        private uint PlayingSoundID = 0;

        public void OnEnable()
        {
            LightSource.OnUpdated += LightUpdated;
        }

        private void LightUpdated(bool on)
        {
            if (on)
            {
                if (StartSoundEvent != null)
                {
                    AkSoundEngine.PostEvent((uint)StartSoundEvent.eventID, gameObject);
                }
            }
            else
            {
                if (StopSoundEvent != null)
                {
                    PlayingSoundID = AkSoundEngine.PostEvent((uint)StopSoundEvent.eventID, gameObject);
                }
                else if (StartSoundEvent != null)
                {
                    AkSoundEngine.StopPlayingID(PlayingSoundID);
                }
            }
        }
    }
}
