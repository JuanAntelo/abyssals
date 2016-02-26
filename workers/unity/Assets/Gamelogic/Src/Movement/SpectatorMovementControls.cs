using UnityEngine;

namespace Assets.Gamelogic.Src.Movement
{
    class SpectatorMovementControls: IMovementControls
    {
        public float CameraSensitivity = 90;
        public float ClimbSpeed = 4;
        public float FastMoveFactor = 3;
        public float SlowMoveFactor = 0.25f;
        public float NormalMoveSpeed = 10;

        private float RotationX;
        private float RotationY;
        private float MoveFactor = 1;

        public void StartControls(Transform transform)
        {

        }

        public void Update(Transform transform)
        {
            Cursor.lockState = Input.GetMouseButton(1) ? CursorLockMode.Locked : CursorLockMode.None;

            if (Input.GetKey(KeyCode.LeftShift))
            {
                MoveFactor = FastMoveFactor;
            }
            else if (Input.GetKey(KeyCode.LeftControl) || Input.GetKey(KeyCode.RightControl))
            {
                MoveFactor = SlowMoveFactor;
            }

            if (Input.GetKey(KeyCode.Q))
            {
                transform.position += transform.up * MoveFactor * ClimbSpeed * Time.deltaTime;
            }

            if (Input.GetKey(KeyCode.E))
            {
                transform.position -= transform.up * MoveFactor * ClimbSpeed * Time.deltaTime;
            }

            if (Input.GetMouseButton(1))
            {
                RotationX = Input.GetAxis("Mouse X") * CameraSensitivity * Time.deltaTime;
                RotationY = Input.GetAxis("Mouse Y") * CameraSensitivity * Time.deltaTime;
                transform.rotation = Quaternion.AngleAxis(RotationX, Vector3.up) * Quaternion.AngleAxis(RotationY, transform.rotation * Vector3.left) * transform.rotation;
            }

            transform.position += transform.forward * NormalMoveSpeed * MoveFactor * Input.GetAxis("Vertical") * Time.deltaTime;
            transform.position += transform.right * NormalMoveSpeed * MoveFactor * Input.GetAxis("Horizontal") * Time.deltaTime;
        }
    }
}
