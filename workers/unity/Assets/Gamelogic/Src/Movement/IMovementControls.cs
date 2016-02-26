using UnityEngine;

namespace Assets.Gamelogic.Src.Movement
{
    interface IMovementControls
    {
        void StartControls(Transform transform);

        void Update(Transform transform);
    }
}
