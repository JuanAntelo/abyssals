using Improbable.Entity.Physical;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Physical
{
    [EngineType(EnginePlatform.Client)]
    public class ClientSidePositionVisualizer : MonoBehaviour
    {
        [Require] public PositionWriter Position;

        public void FixedUpdate()
        {
            var currentPosition = transform.position.ToCoordinates();
            Position.Update.Value(currentPosition).FinishAndSend();
        }

    }
}
