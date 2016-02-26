using System.Collections;
using Improbable.Abyssal.Attributes;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Physical
{
    class ScaleVisualizer : MonoBehaviour
    {
        [Require] public ScaleReader Scale;

        private const float TotalScaleTimeSeconds = 1f;

        private bool Initialized;
        private float StartingScale;
        private float CurrentScale;        
        private float ScaleTime;

        public void OnEnable()
        {
            Scale.CurrentUpdated += ScaleUpdated;
            Initialized = true;
        }

        public void OnDisable()
        {
            Initialized = false;
        }

        private void ScaleUpdated(float scale)
        {
            if (Initialized)
            {
                StopCoroutine("ScaleUp");                
                StartingScale = CurrentScale;
                StartCoroutine("ScaleUp");
            }
            else
            {
                CurrentScale = scale;
                gameObject.transform.localScale = new Vector3(scale, scale, scale);
            }
        }

        protected IEnumerator ScaleUp()
        {
            ScaleTime = 0f;
            var desiredScale = Scale.Current;
            while (ScaleTime < TotalScaleTimeSeconds)
            {
                CurrentScale = Mathf.Lerp(StartingScale, desiredScale, ScaleTime / TotalScaleTimeSeconds);
                gameObject.transform.localScale = new Vector3(CurrentScale, CurrentScale, CurrentScale);
                ScaleTime += Time.deltaTime;
                yield return new WaitForEndOfFrame();
            }
            gameObject.transform.localScale = new Vector3(desiredScale, desiredScale, desiredScale);
        }
    }
}
