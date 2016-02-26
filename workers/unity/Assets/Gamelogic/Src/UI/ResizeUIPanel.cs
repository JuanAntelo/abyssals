using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace Assets.Gamelogic.Src.UI
{
    class ResizeUIPanel : MonoBehaviour, IPointerDownHandler, IDragHandler
    {
        public bool UsePreferredWidth = false;
        public bool UsePreferredHeight = false;

        public RectTransform WidthRectTransform;
        public RectTransform HeightRectTransform;

        public Vector2 MinSize = new Vector2(100, 100);
        public Vector2 MaxSize = new Vector2(400, 400);

        private RectTransform Parent;

        private Vector2 OriginalLocalPointerPosition;
        private float OriginalWidthDelta;
        private float OriginalHeightDelta;

        public void Awake()
        {
            Parent = transform.parent.GetComponent<RectTransform>();
        }

        public void OnPointerDown(PointerEventData data)
        {
            OriginalWidthDelta = WidthRectTransform.sizeDelta.x;
            OriginalHeightDelta = HeightRectTransform.sizeDelta.y;
            RectTransformUtility.ScreenPointToLocalPointInRectangle(Parent, data.position, data.pressEventCamera, out OriginalLocalPointerPosition);
        }

        public void SetSize(Vector2 size)
        {
            if (UsePreferredWidth)
            {
                WidthRectTransform.GetComponent<LayoutElement>().preferredWidth = size.x;
            }
            else
            {
                OriginalWidthDelta = size.x;
                WidthRectTransform.sizeDelta = new Vector2(OriginalWidthDelta, WidthRectTransform.sizeDelta.x);
            }

            if (UsePreferredHeight)
            {
                HeightRectTransform.GetComponent<LayoutElement>().preferredHeight = size.y;
            }
            else
            {
                OriginalHeightDelta = size.y;
                HeightRectTransform.sizeDelta = new Vector2(HeightRectTransform.sizeDelta.x, OriginalHeightDelta);
            }
        }

        public Vector2 GetSize()
        {
            var size = Vector2.zero;
            if (UsePreferredWidth)
            {
                size.x = WidthRectTransform.GetComponent<LayoutElement>().preferredWidth;
            }
            else
            {
                size.x = WidthRectTransform.sizeDelta.x;
            }

            if (UsePreferredHeight)
            {
                size.y = HeightRectTransform.GetComponent<LayoutElement>().preferredHeight;
            }
            else
            {
                size.y = HeightRectTransform.sizeDelta.y;

            }
            return size;
        }

        public void OnDrag(PointerEventData data)
        {
            if (Parent == null)
                return;

            Vector2 localPointerPosition;
            RectTransformUtility.ScreenPointToLocalPointInRectangle(Parent, data.position, data.pressEventCamera, out localPointerPosition);
            Vector3 offsetToOriginal = localPointerPosition - OriginalLocalPointerPosition;

            var widthDelta = Mathf.Clamp(OriginalWidthDelta + offsetToOriginal.x, MinSize.x, MaxSize.y);
            var heightDelta = Mathf.Clamp(OriginalHeightDelta - offsetToOriginal.y, MinSize.y, MaxSize.y);

            if (UsePreferredWidth)
            {
                WidthRectTransform.GetComponent<LayoutElement>().preferredWidth = widthDelta;
            }
            else
            {
                WidthRectTransform.sizeDelta = new Vector2(widthDelta, WidthRectTransform.sizeDelta.y);
            }

            if (UsePreferredHeight)
            {
                HeightRectTransform.GetComponent<LayoutElement>().preferredHeight = heightDelta;
            }
            else
            {
                HeightRectTransform.sizeDelta = new Vector2(HeightRectTransform.sizeDelta.x, heightDelta);
                
            }
        }
    }
}
