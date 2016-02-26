using Improbable.Abyssal.Player;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    [EngineType(EnginePlatform.Client)]
    class PlayerAttackVisualizer : MonoBehaviour
    {
        [Require] public PlayerControlsReader PlayerControls;

        private Animator Animator;

        public void OnEnable()
        {
            Animator = GetComponent<Animator>();
            PlayerControls.StartUsingRightHand += UseRightHand;
        }

        private void UseRightHand(StartUsingRightHand obj)
        {
            Animator.SetTrigger("Attack1Trigger");
        }
    }
}
