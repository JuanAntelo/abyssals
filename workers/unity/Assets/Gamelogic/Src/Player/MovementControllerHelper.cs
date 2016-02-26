using Assets.Gamelogic.Src.Util;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    class MovementControllerHelper
    {
        private const float RotationSpeed = 30;

        public static void UpdateMovementWithAnimator(Vector3 targetDirection, Transform transform, Animator animator)
        {
            animator.SetFloat("Input X", targetDirection.z);
            animator.SetFloat("Input Z", -(targetDirection.x));

            if (!VectorUtil.FloatEquals(targetDirection.x, 0) || !VectorUtil.FloatEquals(targetDirection.z, 0))  //if there is some input
            {
                animator.SetBool("Moving", true);
                animator.SetBool("Running", true);
            }
            else
            {
                animator.SetBool("Moving", false);
                animator.SetBool("Running", false);
            }

            if (targetDirection != Vector3.zero)
            {
                RotateTowardsTarget(Quaternion.LookRotation(targetDirection), transform);
            }
        }

        public static void UpdateMovementWithController(Vector3 targetDirection, float desiredSpeed, CharacterController controller, Transform transform)
        {
            controller.Move(targetDirection*desiredSpeed*Time.deltaTime);

            if (targetDirection != Vector3.zero)
            {
                RotateTowardsTarget(Quaternion.LookRotation(targetDirection), transform);
            }
        }

        public static void UpdateRotation(Quaternion targetRotation, Transform transform)
        {
            RotateTowardsTarget(targetRotation, transform);
        }

        private static void RotateTowardsTarget(Quaternion targetRotation, Transform transform)
        {
            transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, Time.deltaTime * RotationSpeed);
        }
    }
}
