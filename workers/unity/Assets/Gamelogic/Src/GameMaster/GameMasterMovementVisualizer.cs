using Assets.Gamelogic.Src.Movement;
using Improbable.Entity.Physical;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.GameMaster
{
    [EngineType(EnginePlatform.Client)]
    public class GameMasterMovementVisualizer : MonoBehaviour
    {
        [Require] public PositionWriter LocalAuthority;

        private SpectatorMovementControls MovementControls;

        public void OnEnable()
        {
            MovementControls = new SpectatorMovementControls();
            MovementControls.StartControls(transform);
        }

        public void Update()
        {
            MovementControls.Update(transform);
        }
    }
}
