using System;
using UnityEngine;

namespace Assets.Gamelogic.Src.Movement
{
    class RTSMovementControls : IMovementControls
    {
        private const float NormalMoveSpeed = .5f;
        private const float FastMoveFactor = 3f;
        private const float SlowMoveFactor = 0.25f;
        private const float ZoomSpeed = 10f;
        private const float HeightMin = 10f;
        private const float HeightMax = 100f;

        private float Height;
        private float MoveFactor = 1;

        private readonly Vector3 ForwardMovement = Vector3.forward;
        private readonly Vector3 RightMovement = Vector3.right;

        public void StartControls(Transform transform)
        {
            transform.forward = new Vector3(0, -1, 0);
            transform.up = new Vector3(0, 0, 1);
            Height = Mathf.Clamp(transform.position.y, HeightMin, HeightMax);
        }

        public void Update(Transform transform)
        {
            if (Input.GetKey(KeyCode.LeftShift))
            {
                MoveFactor = FastMoveFactor;
            }
            else if (Input.GetKey(KeyCode.LeftControl) || Input.GetKey(KeyCode.RightControl))
            {
                MoveFactor = SlowMoveFactor;
            }
            else
            {
                MoveFactor = 1;
            }

            var scrollWheelDelta = Input.GetAxis("Mouse ScrollWheel");
            if (Math.Abs(scrollWheelDelta) > 0.005f)
            {
                Height = Mathf.Clamp(Height - Input.GetAxis("Mouse ScrollWheel") * ZoomSpeed, HeightMin, HeightMax);
            }

            var moveSpeed = (NormalMoveSpeed*MoveFactor)*Height;

            transform.position += ForwardMovement * moveSpeed * Input.GetAxis("Vertical") * Time.deltaTime;
            transform.position += RightMovement * moveSpeed * Input.GetAxis("Horizontal") * Time.deltaTime;
            transform.position = new Vector3(transform.position.x, Height, transform.position.z);
        }
    }
}
