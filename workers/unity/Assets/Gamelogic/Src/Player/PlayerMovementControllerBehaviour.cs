using Improbable.Abyssal.Controls;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    class PlayerMovementControllerBehaviour : MonoBehaviour
    {
        [Require] public ControlsReader Controls;

        private Animator Animator;

        public void OnEnable()
        {
            Animator = GetComponent<Animator>();
        }

        public void Update()
        {
            if (Camera.main == null)
            {
                return;
            }

            var targetDirection = GetTargetDirection();
            MovementControllerHelper.UpdateMovementWithAnimator(targetDirection, transform, Animator);
        }

        private Vector3 GetTargetDirection()
        {
            var x = Controls.Right;
            var z = Controls.Forward;
            var inputVector = new Vector3(x, 0, z);
            return inputVector;
        }

        
    }
}
