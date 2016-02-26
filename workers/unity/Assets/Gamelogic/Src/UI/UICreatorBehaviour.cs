using System.Collections.Generic;
using Improbable.Entity.Physical;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.UI
{
    class UICreatorBehaviour : MonoBehaviour
    {
        [Require] public PositionWriter Position;

        private Canvas RootCanvas;

        public string CanvasName;

        private List<ICanvasListener> Listeners;

        public void Awake()
        {
            Listeners = new List<ICanvasListener>();
        }

        public void OnEnable()
        {
            var canvas = Resources.Load<Canvas>("UI/" + CanvasName);
            RootCanvas = Instantiate(canvas);

            for (var i = 0; i < Listeners.Count; i++)
            {
                Listeners[i].InitializeUI(RootCanvas, gameObject);
            }
        }

        public void OnDisable()
        {
            Destroy(RootCanvas);
            RootCanvas = null;
        }

        public void AddCanvasListener(ICanvasListener listener)
        {
            Listeners.Add(listener);
        }

        public void RemoveCanvasListener(ICanvasListener listener)
        {
            Listeners.Remove(listener);
        }
    }
}
