using UnityEngine;

namespace Assets.Gamelogic.Src.UI
{
    class HealthBarBehaviour : MonoBehaviour
    {
        public RectTransform Rect;

        public void SetHealthPercentage(float percentage)
        {
            Rect.localScale = new Vector3(percentage, 1, 1);
        }
    }
}
