using Improbable.Abyssal.Dev;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Improbable;

namespace Assets.Gamelogic.Src.Dev
{
    [EngineType(EnginePlatform.Client)]
    class EntityInfoModifierVisualizer : MonoBehaviour
    {
        [Require] public EntityInfoModifierWriter Modifier;

        public void UpdateIntValue(EntityId entity, EntityInfoType infoType, int newValue)
        {
            Modifier.Update.TriggerSetIntValue(entity, infoType, newValue).FinishAndSend();
        }

        public void UpdateFloatValue(EntityId entity, EntityInfoType infoType, float newValue)
        {
            Modifier.Update.TriggerSetFloatValue(entity, infoType, newValue).FinishAndSend();
        }

        public void UpdateDoubleValue(EntityId entity, EntityInfoType infoType, double newValue)
        {
            Modifier.Update.TriggerSetDoubleValue(entity, infoType, newValue).FinishAndSend();
        }
    }
}
