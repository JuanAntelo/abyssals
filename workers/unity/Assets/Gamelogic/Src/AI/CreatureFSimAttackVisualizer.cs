using Assets.Gamelogic.Src.Combat;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Improbable.Abyssal.Combat;

namespace Assets.Gamelogic.Src.AI
{
    [EngineType(EnginePlatform.FSim)]
    class CreatureFSimAttackVisualizer : MonoBehaviour
    {
        [Require] public AttackDataReader AttackData;

        private WeaponAttackVisualizer WeaponAttack;

        public void OnEnable()
        {
            AttackData.TriggerAttack += AttackTriggered;
        }

        private void AttackTriggered(TriggerAttack obj)
        {
            if (WeaponAttack == null)
            {
                WeaponAttack = GetComponent<WeaponAttackVisualizer>();
            }

            if (WeaponAttack != null)
            {
                WeaponAttack.StartUsingWeapon();
            }
        }
    }
}
