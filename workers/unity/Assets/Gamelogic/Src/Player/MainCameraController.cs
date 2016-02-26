using Improbable.Entity.Physical;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    class MainCameraController : MonoBehaviour
    {
        [Require] public PositionWriter Position;

        public GameObject Cam;

        public void OnEnable()
        {
            Cam.SetActive(true);
        }
    }
}
