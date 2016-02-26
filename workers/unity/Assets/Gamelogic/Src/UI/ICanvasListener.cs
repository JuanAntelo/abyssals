using UnityEngine;

namespace Assets.Gamelogic.Src.UI
{
    interface ICanvasListener
    {
        void InitializeUI(Canvas canvas, GameObject localPlayer);
    }
}
