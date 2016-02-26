using Improbable.Abyssal.Player;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    internal class ArmorTypeVisualizer : MonoBehaviour
    {
        [Require] public AppearanceReader Appearance;

        public Renderer Armor;

        public Material WhiteArmor;        
        public Material BlackArmor;
        public Material RedArmor;

        public void OnEnable()
        {
            switch (Appearance.ArmorType)
            {
                case ArmorType.White:
                    SetArmorTo(WhiteArmor);
                    break;
                case ArmorType.Black:
                    SetArmorTo(BlackArmor);
                    break;
                case ArmorType.Red:
                    SetArmorTo(RedArmor);
                    break;
            }
        }

        private void SetArmorTo(Material material)
        {
            Armor.material = material;
        }
    }
}