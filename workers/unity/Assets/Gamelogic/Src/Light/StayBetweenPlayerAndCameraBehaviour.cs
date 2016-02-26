using UnityEngine;

namespace Assets.Gamelogic.Src.Light
{
    class StayBetweenPlayerAndCameraBehaviour : MonoBehaviour
    {
        public GameObject CameraTarget;

        public float Distance;

        public void OnEnable()
        {
            Distance = (transform.position - CameraTarget.transform.position).magnitude;
        }

        public void Update()
        {
            var cameraPosition = Camera.main.transform.position;
            var targetPosition = CameraTarget.transform.position;
            var dirToCamera = (cameraPosition - targetPosition).normalized;
            var vecToLight = dirToCamera*Distance;
            transform.position = targetPosition + vecToLight;
        }
    }
}
