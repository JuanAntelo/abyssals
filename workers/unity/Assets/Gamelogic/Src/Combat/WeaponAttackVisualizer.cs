using Assets.Gamelogic.Src.Util;
using Improbable.Abyssal.Combat;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Combat
{
    internal class WeaponAttackVisualizer : MonoBehaviour
    {
        [Require] public AttackControlsWriter AttackControls;

        public Collider HitBox;

        public void StartUsingWeapon()
        {
            if (AttackControls != null)
            {
                AttackControls.Update.TriggerResetHitEnemies().FinishAndSend();
                HitBox.enabled = true;
            }
        }

        public void StopUsingWeapon()
        {
            if (AttackControls != null)
            {
                HitBox.enabled = false;
            }
        }

        public void Hit(GameObject other)
        {
            if (AttackControls != null)
            {
                var topLevelEntity = HierarchyUtil.GetTopLevelEntity(other);
                var targetEntityObject = topLevelEntity.gameObject.GetEntityObject();
                if (targetEntityObject != null)
                {
                    var targetId = targetEntityObject.EntityId;
                    AttackControls.Update.TriggerHitEntity(targetId).FinishAndSend();
                }
            }
        }
    }
}