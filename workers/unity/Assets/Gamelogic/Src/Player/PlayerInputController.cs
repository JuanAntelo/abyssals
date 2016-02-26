using Improbable.Abyssal.Controls;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    class PlayerInputController : MonoBehaviour
    {
        [Require] public ControlsWriter Controls;

        public void Update()
        {
            var forward = -Input.GetAxis("Vertical");
            var right = Input.GetAxis("Horizontal");
            var inputVector = new Vector3(right, 0, forward);
            var targetDirection = GetTargetDirection(inputVector);

            Controls.Update
                    .Forward(targetDirection.z)
                    .Right(targetDirection.x)
                    .FinishAndSend();
        }

        private Vector3 GetTargetDirection(Vector3 inputVector)
        {
            var forward = Camera.main.transform.TransformDirection(Vector3.forward);
            forward.y = 0;
            forward = forward.normalized;

            var right = Camera.main.transform.TransformDirection(Vector3.right);
            right.y = 0;
            right = right.normalized;

            return inputVector.x * right + -inputVector.z * forward;
        }
    }
}
