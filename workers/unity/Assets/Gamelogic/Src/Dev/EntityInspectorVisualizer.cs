using Assets.Gamelogic.Src.Util;
using Improbable.Abyssal.Dev;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using UnityEngine.EventSystems;
using Improbable;

namespace Assets.Gamelogic.Src.Dev
{
    [EngineType(EnginePlatform.Client)]
    internal class EntityInspectorVisualizer : MonoBehaviour
    {
        [Require] public InspectorEntitySelectorControlsWriter Controls;

        public LayerMask RaycastMask;

        public void StopInspecting(EntityId entityId)
        {
            Controls.Update.TriggerDeselectEntity(entityId).FinishAndSend();
        }

        public void Update()
        {
            if (Input.GetMouseButtonDown(0) && InteractionMode.GetInteractionMode() == InteractionMode.InteractionModes.Inspecting)
            {
                if (!IsHittingUIElement())
                {
                    var ray = Camera.main.ScreenPointToRay(Input.mousePosition);
                    RaycastHit hit;
                    if (Physics.Raycast(ray, out hit, Mathf.Infinity, RaycastMask))
                    {
                        Debug.DrawLine(ray.origin, hit.point, Color.white);
                        var other = HierarchyUtil.GetTopLevelEntity(hit.collider.gameObject);
                        if (other.IsEntityObject())
                        {
                            Controls.Update.TriggerSelectEntity(other.EntityId()).FinishAndSend();
                        }
                    }
                }
            }
        }

        private bool IsHittingUIElement()
        {
            return EventSystem.current.IsPointerOverGameObject();
        }
    }
}