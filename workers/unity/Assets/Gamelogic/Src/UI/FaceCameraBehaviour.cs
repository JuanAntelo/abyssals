using UnityEngine;

namespace Assets.Gamelogic.Src.UI
{
    class FaceCameraBehaviour : MonoBehaviour
    {
        public void Update()
        {
            if (Camera.main != null)
            {
                transform.forward = Camera.main.transform.forward;
            }
        }
    }
}
