
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Improbable.Abyssal.Combat;

namespace Assets.Gamelogic.Src.Combat
{
    [EngineType(EnginePlatform.Client)]
    internal class AttackedVisualizer : MonoBehaviour
    {
        [Require] public AttackeeReader Attackee;

        public AkEvent AttackedAkEvent;

        public void OnEnable()
        {
            Attackee.Attacked += WasAttacked;
        }

        private void WasAttacked(Attacked att)
        {
            if (AttackedAkEvent != null)
            {
                AkSoundEngine.PostEvent((uint) AttackedAkEvent.eventID, gameObject);
            }
        }
    }
}