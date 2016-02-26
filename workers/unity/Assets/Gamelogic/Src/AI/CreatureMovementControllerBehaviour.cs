using System;
using Assets.Gamelogic.Src.Player;
using Improbable.Abyssal.Ai;
using Improbable.Math;
using Improbable.Unity;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Vector3 = UnityEngine.Vector3;

namespace Assets.Gamelogic.Src.AI
{
    [EngineType(EnginePlatform.FSim)]
    class CreatureMovementControllerBehaviour : MonoBehaviour
    {
        [Require] public MovementTargetReader MovementTarget;
        [Require] public AIControlsWriter Controls;

//        private const float Speed = 10f;

        private NavMeshAgent Agent;
        private CharacterController Controller;

        public void OnEnable()
        {
            Controller = GetComponent<CharacterController>();

            Agent = GetComponent<NavMeshAgent>();
            Agent.Stop();
//            Agent.updatePosition = false;
//            Agent.updateRotation = false;

            MovementTarget.TargetUpdated += TargetUpdated;
        }

        public void Update()
        {
            if (MovementTarget.Target.HasValue)
            {
                if (WithinStoppingDistance(MovementTarget.Target.Value.ToUnityVector()))
                {
                    Agent.Stop();
                    var lookVector = (MovementTarget.Target.Value.ToUnityVector() - transform.position);
                    lookVector.y = 0;
                    lookVector = lookVector.normalized;

                    var targetRotation = Quaternion.LookRotation(lookVector);
                    if (Math.Abs(Quaternion.Angle(targetRotation, transform.rotation)) > 5)
                    {
                        MovementControllerHelper.UpdateRotation(targetRotation, transform);
                    }
                }
                else
                {
                    Agent.Resume();
                }

                Controls.Update.MovementVector(Agent.velocity.ToNativeVector()).FinishAndSend();
            }

//                print("Creature movement updating with target: " + transform.position + " " + Agent.destination + " " + Agent.desiredVelocity);
//                MovementControllerHelper.UpdateMovementWithController(Agent.desiredVelocity, Speed, Controller, transform);
        }

        private bool WithinStoppingDistance(Vector3 target)
        {
            return Vector3.Distance(target, transform.position) < MovementTarget.StoppingDistance;
        }

        private void TargetUpdated(Coordinates? target)
        {
            if (target.HasValue)
            {
                Agent.SetDestination(target.Value.ToUnityVector());
            }
            else
            {
                Agent.ResetPath();
            }
        }
    }
}
