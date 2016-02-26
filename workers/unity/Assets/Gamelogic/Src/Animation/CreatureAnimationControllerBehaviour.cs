using Assets.Gamelogic.Src.Combat;
using Improbable.Abyssal.Ai;
using Improbable.Abyssal.Attributes;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Improbable.Abyssal.Combat;

namespace Assets.Gamelogic.Src.Animation
{
    class CreatureAnimationControllerBehaviour : MonoBehaviour
    {
        [Require] public DeathReader Death;
        [Require] public AttackDataReader AttackData;
        [Require] public AIControlsReader AIControls;

        public Animator AnimationController;
        private WeaponAttackVisualizer WeaponAttack;
        private NavMeshAgent Agent;

        public void OnEnable()
        {
            WeaponAttack = GetComponent<WeaponAttackVisualizer>();
            Agent = GetComponent<NavMeshAgent>();

            if (Death.IsDead)
            {
                DoDeath();
            }

            Death.IsDeadUpdated += IsDeadUpdated;
            AttackData.TriggerAttack += AttackTriggered;
        }
        
        public void Update()
        {
            var speed = AIControls.MovementVector.ToUnityVector().magnitude;
            AnimationController.SetFloat("speed", speed);
        }

        private void AttackTriggered(TriggerAttack obj)
        {
            AnimationController.SetTrigger("attack");
        }

        private void IsDeadUpdated(bool isDead)
        {
            if (isDead)
            {
                DoDeath();
            }
        }

        private void DoDeath()
        {
            AnimationController.SetBool("death", true);
        }
    }
}
