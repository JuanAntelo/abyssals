using UnityEngine;

namespace Assets.Gamelogic.Src.Combat
{
    internal class WeaponAttackAnimationBehaviour : StateMachineBehaviour
    {
        private WeaponAttackVisualizer WeaponAttack;

        public override void OnStateEnter(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
        {
            if (WeaponAttack == null)
            {
                WeaponAttack = animator.gameObject.GetComponent<WeaponAttackVisualizer>();
            }

            if (WeaponAttack != null)
            {
                WeaponAttack.StartUsingWeapon();
            }
        }

        public override void OnStateExit(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
        {
            if (WeaponAttack != null)
            {
                WeaponAttack.StopUsingWeapon();
            }
        }
    }
}