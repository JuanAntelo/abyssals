using System.Collections.Generic;
using System.Globalization;
using Assets.Gamelogic.Src.UI.Knowledge;
using Improbable.Abyssal.Dev;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using UnityEngine.UI;
using Improbable.Util.Collections;
using Improbable;

namespace Assets.Gamelogic.Src.UI.EntityInspector
{
    [EngineType(EnginePlatform.Client)]
    internal class EntityInfoVisualizer : MonoBehaviour, ICanvasListener
    {
        [Require] public InspectedEntitiesInfoReader EntitiesInfo;
        [Require] public EntityInfoModifierReader Modifier;

        private GameObject LocalPlayer;
        private Canvas UI;
        
        private const string EntityInspectorResourcePath = "UI/Dev/EntityInspector/EntityInspector";
        private const string CategoryResourcePath = "UI/Dev/EntityInspector/Category";
        private const string InfoLineResourcePath = "UI/Dev/EntityInspector/InfoLine";

        private const string PathToTitleFromRoot = "TitlePanel/Title";
        private const string PathToResizeHandleFromRoot = "ResizeHandle";
        private const string PathToContentFromRoot = "ScrollPane/Viewport/Content";
        
        private const string PathToTitleTextFromCategory = "Title/Text";
        private const string PathToInfoFromCategory = "Info";

        private const string PathToTitleFromInfoLine = "Title";
        private const string PathToValueFromInfoLine = "Value";
        

        private readonly Dictionary<EntityInfoType, EntityInfoCategory> Categories = new Dictionary
            <EntityInfoType, EntityInfoCategory>
        {
            {EntityInfoType.Healthcurrent, EntityInfoCategory.Stats},
            {EntityInfoType.Healthmax, EntityInfoCategory.Stats},
            {EntityInfoType.Scale, EntityInfoCategory.Appearance}
        };

        private Dictionary<EntityId, GameObject> InfoPanes;

        public void OnEnable()
        {
            InfoPanes = new Dictionary<EntityId, GameObject>();

            EntitiesInfo.InspectedEntitiesUpdated += InspectedEntitiesUpdated;

            GetComponent<UICreatorBehaviour>().AddCanvasListener(this);
        }

        public void OnDisable()
        {
            DeleteOldUI();
        }

        public void InitializeUI(Canvas rootCanvas, GameObject localPlayer)
        {
            LocalPlayer = localPlayer;
            UI = rootCanvas;

            if (EntitiesInfo != null)
            {
                ReloadUI(EntitiesInfo.InspectedEntities);
            }
        }

        private void InspectedEntitiesUpdated(IReadOnlyDictionary<EntityId, EntityInfo> info)
        {
            if (UI != null)
            {
                ReloadUI(info);
            }
        }

        private EntityInfoCategory GetCategory(EntityInfoType infoType)
        {
            if (Categories.ContainsKey(infoType))
            {
                return Categories[infoType];
            }

            return EntityInfoCategory.Other;
        }

        private void ReloadUI(IReadOnlyDictionary<EntityId, EntityInfo> info)
        {
            var oldPositions = GetOldPositions();
            var oldSizeDeltas = GetOldSizeDeltas();

            DeleteOldUI();

            var entitiesInfoEnumerator = info.GetEnumerator();
            while (entitiesInfoEnumerator.MoveNext())
            {
                var entityIdToInfo = entitiesInfoEnumerator.Current;

                var entityId = entityIdToInfo.Key;
                var entityInfo = entityIdToInfo.Value;

                var infoPane = CreateInfoPane(entityId, entityInfo.EntityName);

                if (oldPositions.ContainsKey(entityId))
                {
                    infoPane.transform.position = oldPositions[entityId];
                }
                else
                {
                    var rectTransform = infoPane.GetComponent<RectTransform>();
                    var xOffset = - rectTransform.sizeDelta.x * 0.5f;
                    const float yOffset = 15f;
                    infoPane.transform.position = Input.mousePosition + new Vector3(xOffset, yOffset, 0f);
                }

                if (oldSizeDeltas.ContainsKey(entityId))
                {
                    var oldSizeDelta = oldSizeDeltas[entityId];
                    var resizePanel = infoPane.transform.Find(PathToResizeHandleFromRoot).GetComponent<ResizeUIPanel>();
                    resizePanel.SetSize(oldSizeDelta);
                }

                var infoPaneContentPane = infoPane.transform.Find(PathToContentFromRoot);

                AddValues(entityInfo.IntValues, infoPaneContentPane);
                AddValues(entityInfo.FloatValues, infoPaneContentPane);
                AddValues(entityInfo.DoubleValues, infoPaneContentPane);
            }
        }

        private Dictionary<EntityId, Vector3> GetOldPositions()
        {
            var oldPositions = new Dictionary<EntityId, Vector3>();
            var paneEnumerator = InfoPanes.GetEnumerator();
            while (paneEnumerator.MoveNext())
            {
                var pane = paneEnumerator.Current;
                oldPositions[pane.Key] = pane.Value.transform.position;
            }
            return oldPositions;
        }

        private Dictionary<EntityId, Vector2> GetOldSizeDeltas()
        {
            var oldSizeDeltas = new Dictionary<EntityId, Vector2>();
            var paneEnumerator = InfoPanes.GetEnumerator();
            while (paneEnumerator.MoveNext())
            {
                var pane = paneEnumerator.Current;
                var resizePanel = pane.Value.transform.Find(PathToResizeHandleFromRoot).GetComponent<ResizeUIPanel>();
                oldSizeDeltas[pane.Key] = resizePanel.GetSize();
            }
            return oldSizeDeltas;
        } 

        private void AddValues<T>(IReadOnlyDictionary<EntityInfoType, T> typeValues, Transform infoPaneContentPane)
        {
            var enumerator = typeValues.GetEnumerator();
            while (enumerator.MoveNext())
            {
                var current = enumerator.Current;

                var infoType = current.Key;
                var value = current.Value;
                var category = GetCategory(current.Key);

                var categoryPane = infoPaneContentPane.Find(category.ToString());
                if (categoryPane == null)
                {
                    categoryPane = CreateCategoryPane(infoPaneContentPane, category).transform;
                }

                CreateInfoLine(categoryPane.Find(PathToInfoFromCategory).transform, infoType, value);
            }
        }

        private GameObject CreateInfoPane(EntityId entityId, string entityName)
        {
            var pane = (GameObject) Instantiate(Resources.Load(EntityInspectorResourcePath));
            pane.transform.Find(PathToTitleFromRoot).GetComponent<Text>().text = entityName + ":" + entityId.ToString();
            pane.transform.SetParent(UI.transform, false);
            
            pane.GetComponent<LocalPlayerKnowledgeBehaviour>().LocalPlayer = LocalPlayer;
            pane.GetComponent<EntityIdKnowledgeBehaviour>().EntityId = entityId;

            var rectTransform = pane.GetComponent<RectTransform>();
            rectTransform.anchoredPosition = new Vector2(1, 0);
            InfoPanes[entityId] = pane;
            return pane;
        }

        private GameObject CreateCategoryPane(Transform parent, EntityInfoCategory category)
        {
            var pane = (GameObject) Instantiate(Resources.Load(CategoryResourcePath));
            pane.name = category.ToString();
            pane.transform.SetParent(parent, false);
            var titleText = pane.transform.Find(PathToTitleTextFromCategory).GetComponent<Text>();
            titleText.text = category.ToString();
            return pane;
        }

        private void CreateInfoLine<T>(Transform parent, EntityInfoType infoType, T infoValue)
        {
            var pane = (GameObject) Instantiate(Resources.Load(InfoLineResourcePath));
            pane.transform.SetParent(parent, false);
            var title = pane.transform.Find(PathToTitleFromInfoLine).GetComponent<Text>();
            var value = pane.transform.Find(PathToValueFromInfoLine).GetComponent<Text>();
            title.text = infoType.ToString();
            value.text = infoValue.ToString();

            pane.GetComponent<InfoLineBehaviour>().InfoType = infoType;

            var valueTypeKnowledge = pane.GetComponent<ValueTypeKnowledgeBehaviour>();
            var inputField = pane.GetComponentInChildren<InputField>();
            if (typeof(T) == typeof(int))
            {
                inputField.contentType = InputField.ContentType.IntegerNumber;
                valueTypeKnowledge.ValueType = HandledValueTypes.Int;
            }
            else if (typeof(T) == typeof(float))
            {
                inputField.contentType = InputField.ContentType.DecimalNumber;
                valueTypeKnowledge.ValueType = HandledValueTypes.Float;
            }
            else if (typeof (T) == typeof (double))
            {
                inputField.contentType = InputField.ContentType.DecimalNumber;
                valueTypeKnowledge.ValueType = HandledValueTypes.Double;
            }
        }

        private void DeleteOldUI()
        {
            var infoPaneEnumerator = InfoPanes.Values.GetEnumerator();
            while (infoPaneEnumerator.MoveNext())
            {
                var scrollRect = infoPaneEnumerator.Current;
                Destroy(scrollRect);
            }

            InfoPanes.Clear();
        }
    }
}