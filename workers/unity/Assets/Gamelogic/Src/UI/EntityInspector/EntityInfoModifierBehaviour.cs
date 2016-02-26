using Assets.Gamelogic.Src.Dev;
using Assets.Gamelogic.Src.UI.Knowledge;
using Improbable.Abyssal.Dev;
using UnityEngine;
using UnityEngine.UI;

namespace Assets.Gamelogic.Src.UI.EntityInspector
{
    internal class EntityInfoModifierBehaviour : MonoBehaviour
    {
        public void Close()
        {
            var localPlayer = GetComponent<LocalPlayerKnowledgeBehaviour>().LocalPlayer;
            var inspector = localPlayer.GetComponent<EntityInspectorVisualizer>();
            var entityId = GetComponent<EntityIdKnowledgeBehaviour>().EntityId;
            inspector.StopInspecting(entityId);
        }

        public void SetChangedValues()
        {
            var localPlayer = GetComponent<LocalPlayerKnowledgeBehaviour>().LocalPlayer;
            var entityId = GetComponent<EntityIdKnowledgeBehaviour>().EntityId;
            var modifier = localPlayer.GetComponent<EntityInfoModifierVisualizer>();

            var inputFields = GetComponentsInChildren<InputField>();
            for (var i = 0; i < inputFields.Length; i++)
            {
                var inputField = inputFields[i];
                if (inputField.text.Length > 0)
                {
                    var valueToChange = inputField.GetComponentInParent<InfoLineBehaviour>().InfoType;
                    var valueType = inputField.GetComponentInParent<ValueTypeKnowledgeBehaviour>().ValueType;

                    switch (valueType)
                    {
                        case HandledValueTypes.Int:
                            modifier.UpdateIntValue(entityId, valueToChange, int.Parse(inputField.text));
                            break;
                        case HandledValueTypes.Float:
                            modifier.UpdateFloatValue(entityId, valueToChange, float.Parse(inputField.text));
                            break;
                        case HandledValueTypes.Double:
                            modifier.UpdateDoubleValue(entityId, valueToChange, double.Parse(inputField.text));
                            break;
                    }
                }
            }
        }
    }
}