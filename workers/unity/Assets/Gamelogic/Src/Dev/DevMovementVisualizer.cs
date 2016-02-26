using System.Collections.Generic;
using Assets.Gamelogic.Src.Movement;
using Improbable.Entity.Physical;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Dev
{
    [EngineType(EnginePlatform.Client)]
    public class DevMovementVisualizer : MonoBehaviour
    {
        [Require] public PositionWriter LocalAuthority;

        public enum MovementMode
        {
            RTS,
            Spectator
        }

        private SpectatorMovementControls SpectatorMovement;
        private RTSMovementControls RTSMovement;

        private Dictionary<MovementMode, IMovementControls> MovementControls;

        private MovementMode CurrentMovementMode;
        private IMovementControls CurrentMovementControls;

        private bool Initialized;

        public void Awake()
        {
            SpectatorMovement = new SpectatorMovementControls();
            RTSMovement = new RTSMovementControls();

            MovementControls = new Dictionary<MovementMode, IMovementControls>
            {
                {MovementMode.RTS, RTSMovement},
                {MovementMode.Spectator, SpectatorMovement}
            };

            Initialized = false;
        }

        public void Update()
        {
            if (!Initialized)
            {
                CurrentMovementMode = MovementMode.RTS;
                CurrentMovementControls = MovementControls[CurrentMovementMode];
                CurrentMovementControls.StartControls(transform);
                Initialized = true;
            }

            if (Input.GetKeyDown(KeyCode.C))
            {
                SwitchMovementMode();
            }

            CurrentMovementControls.Update(transform);
        }

        public void SwitchMovementMode()
        {
            CurrentMovementMode = CurrentMovementMode == MovementMode.RTS ? MovementMode.Spectator : MovementMode.RTS;
            CurrentMovementControls = MovementControls[CurrentMovementMode];
            CurrentMovementControls.StartControls(transform);
        }
    }
}