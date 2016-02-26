using Improbable.Corelib.Interpolation;
using Improbable.Entity.Physical;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Vector3 = Improbable.Math.Vector3d;

namespace Assets.Gamelogic.Src.Physical
{
    class PlayerInterpolatingRotationVisualizer : MonoBehaviour
    {
        [Require] protected RotationReader Rotation;

        private readonly RotationInterpolator RotationInterpolator = new RotationInterpolator();
        private Transform CachedTransform;

        private Quaternion StateRotation
        {
            get { return Quaternion.Euler(Rotation.Euler.ToUnityVector()); }
        }

        public void OnEnable()
        {
            CachedTransform = transform;
            ResetInterpolators();
            RegisterOnStateUpdates();
            InitializePosition();
        }

        public void Update()
        {
            if (!Rotation.IsAuthoritativeHere)
            {
                var deltaTimeToAdvance = Time.deltaTime;
                SetRotation(RotationInterpolator.GetInterpolatedValue(deltaTimeToAdvance));
            }
        }

        private void ResetInterpolators()
        {
            var initialValueAbsoluteTime = Rotation.Timestamp;
            RotationInterpolator.Reset(StateRotation, initialValueAbsoluteTime);
        }

        private void InitializePosition()
        {
            SetRotation(StateRotation);
        }

        private void RegisterOnStateUpdates()
        {
            Rotation.EulerUpdated += UpdateRotation;
        }

        private void UpdateRotation(Vector3 rotation)
        {
            RotationInterpolator.AddValue(StateRotation, Rotation.Timestamp);
        }

        private void SetRotation(Quaternion targetRotation)
        {
            CachedTransform.rotation = targetRotation;
        }
    }
}
