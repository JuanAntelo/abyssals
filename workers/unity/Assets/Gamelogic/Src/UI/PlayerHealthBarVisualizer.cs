using Improbable.Abyssal.Attributes;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.UI
{
    [EngineType(EnginePlatform.Client)]
    class PlayerHealthBarVisualizer : MonoBehaviour, ICanvasListener
    {
        [Require] public HealthReader Health;

        private RectTransform HealthBar;

        public void OnEnable()
        {
            Health.CurrentUpdated += CurrentHealthUpdated;
            Health.MaxUpdated += MaxHealthUpdated;

            GetComponent<UICreatorBehaviour>().AddCanvasListener(this);
        }

        public void InitializeUI(Canvas rootCanvas, GameObject localPlayer)
        {
            SetHealthBarRect(rootCanvas);
            if (Health != null)
            {
                SetHealthbar(Health.Current/(float) Health.Max);
            }
        }

        private void MaxHealthUpdated(int max)
        {
            SetHealthbar(Health.Current/(float) max);
        }

        private void CurrentHealthUpdated(int current)
        {
            SetHealthbar(current/(float) Health.Max);
        }

        private void SetHealthbar(float percentage)
        {
            if (HealthBar != null)
            {
                HealthBar.localScale = new Vector3(percentage, 1, 1);
            }
        }

        private void SetHealthBarRect(Canvas rootCanvas)
        {
            if (rootCanvas != null)
            {
                var healthBar = rootCanvas.transform.Find("HealthBar");
                if (healthBar != null)
                {
                    HealthBar = healthBar.GetComponent<RectTransform>();
                }
            }
        }
    }
}
