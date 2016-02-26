using Assets.Gamelogic.Src.Util;
using Improbable.Abyssal.Light;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Light
{
    [EngineType(EnginePlatform.FSim)]
    internal class LightTargetsVisualizer : MonoBehaviour
    {
        [Require] public LightTriggersWriter LightTriggers;
        [Require] public LightSourceReader LightSource;

        public Collider LitArea;

        public void OnEnable()
        {
            LightSource.OnUpdated += OnUpdated;
        }

        private void OnUpdated(bool isOn)
        {
            LitArea.enabled = isOn;
        }

        public void OnTriggerEnter(Collider other)
        {
            var otherGameObject = HierarchyUtil.GetTopLevelEntity(other.gameObject);
            if (otherGameObject.IsEntityObject() && otherGameObject.tag == "Player")
            {
                var otherEntityID = otherGameObject.EntityId();
                LightTriggers.Update
                    .TriggerEntityEnteredLight(otherEntityID)
                    .FinishAndSend();
            }
        }

        public void OnTriggerExit(Collider other)
        {
            var otherGameObject = HierarchyUtil.GetTopLevelEntity(other.gameObject);
            if (otherGameObject.IsEntityObject())
            {
                var otherEntityID = otherGameObject.EntityId();
                LightTriggers.Update
                    .TriggerEntityExitedLight(otherEntityID)
                    .FinishAndSend();
            }
        }
    }
}