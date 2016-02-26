using UnityEngine;
using UnityEngine.EventSystems;

namespace Assets.Gamelogic.Src.UI
{
    internal class Draggable : MonoBehaviour, IDragHandler
    {
        public RectTransform TopLevelTransform = null;

        public void OnDrag(PointerEventData eventData)
        {
            transform.position += new Vector3(eventData.delta.x, eventData.delta.y);
        }
    }
}