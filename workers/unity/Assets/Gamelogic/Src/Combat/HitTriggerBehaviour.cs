using UnityEngine;

namespace Assets.Gamelogic.Src.Combat
{
    class HitTriggerBehaviour : MonoBehaviour
    {
        private WeaponAttackVisualizer WeaponAttackVisualizer;

        public void OnEnable()
        {
            WeaponAttackVisualizer = GetComponentInParent<WeaponAttackVisualizer>();
        }

        public void OnTriggerEnter(Collider other)
        {
            WeaponAttackVisualizer.Hit(other.gameObject);
        }
    }
}
