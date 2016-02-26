using Improbable.Entity.Physical;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Light
{
    class EnableOnLocalPlayerVisualizer : MonoBehaviour
    {
        [Require] public PositionWriter Position;

        public GameObject ToEnable;

        public void OnEnable()
        {
            ToEnable.SetActive(true);
        }
    }
}
