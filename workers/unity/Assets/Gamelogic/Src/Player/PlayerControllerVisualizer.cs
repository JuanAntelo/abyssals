using Improbable.Abyssal.Combat;
using Improbable.Abyssal.Player;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    [EngineType(EnginePlatform.Client)]
    internal class PlayerControllerVisualizer : MonoBehaviour
    {
        private Animator Animator;
        [Require] public PlayerControlsWriter Controls;
        [Require] public AttackDataReader AttackData;

        private float Cooldown;

        public void OnEnable()
        {
            Animator = GetComponent<Animator>();
        }

        public void Update()
        {
            Cooldown += Time.deltaTime;

            if (Cooldown >= AttackData.Cooldown)
            {
                if (Input.GetButtonDown("Fire1"))
                {
                    Controls.Update.TriggerStartUsingRightHand().FinishAndSend();
                    Cooldown = 0;
                }                
            }

            if (Input.GetButtonDown("Fire2"))
            {
                Controls.Update.TriggerStartUsingLeftHand().FinishAndSend();
            }

            if (Input.GetButtonUp("Fire1"))
            {
                Controls.Update.TriggerStopUsingRightHand().FinishAndSend();
            }

            if (Input.GetButtonUp("Fire2"))
            {
                Controls.Update.TriggerStopUsingLeftHand().FinishAndSend();
            }
        }
    }
}