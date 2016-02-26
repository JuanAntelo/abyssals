using Improbable.Corelib.Interpolation;
using Improbable.Entity.Physical;
using Improbable.Math;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Physical
{
    class PlayerInterpolatingPositionVisualizer : MonoBehaviour
    {
        private readonly PositionInterpolator PositionInterpolator = new PositionInterpolator();
        [Require] protected PositionReader Position;

        private Transform CachedTransform;

        private Coordinates StatePosition
        {
            get { return Position.Value; }
        }

        public void OnEnable()
        {
            CachedTransform = transform;
            ResetInterpolator();
            RegisterOnStateUpdates();
            InitializePosition();
        }

        public void Update()
        {
            if (!Position.IsAuthoritativeHere)
            {
                var deltaTimeToAdvance = Time.deltaTime;
                SetPosition(PositionInterpolator.GetInterpolatedValue(deltaTimeToAdvance));
            }
        }

        private void ResetInterpolator()
        {
            var initialValueAbsoluteTime = Position.Timestamp;
            PositionInterpolator.Reset(StatePosition, initialValueAbsoluteTime);
        }

        private void InitializePosition()
        {
            SetPosition(StatePosition);
        }

        private void RegisterOnStateUpdates()
        {
            Position.ValueUpdated += UpdatePosition;
        }

        private void UpdatePosition(Coordinates position)
        {
            PositionInterpolator.AddValue(Position.Value, Position.Timestamp);
        }

        private void SetPosition(Coordinates unityVector)
        {
            CachedTransform.position = unityVector.ToUnityVector();
        }
    }
}
