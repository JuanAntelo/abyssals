using System.Collections.Generic;
using System.Globalization;
using Improbable.Abyssal.Clients;
using Improbable.Abyssal.Dev;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Text = UnityEngine.UI.Text;
using Improbable.Util.Collections;
using Improbable;

namespace Assets.Gamelogic.Src.UI.DevClientInfo
{
    [EngineType(EnginePlatform.Client)]
    internal class DevClientInfoVisualizer : MonoBehaviour, ICanvasListener
    {
        private const string ClientTypePanePath = "UI/Dev/DevInfo/ClientType";
        private const string ClientInfoPanePath = "UI/Dev/DevInfo/ClientInfo";

        private const string PathToClientTypeFromRoot = "ClientInfo/Viewport/Content";
        private const string PathToTitleFromClientType = "TitlePane/ClientType";
        private const string PathToCountFromClientType = "ClientCountPane/Count";
        private const string PathToClientInfoFromClientType = "Content";
        private const string PathToEntityIdFromClientInfo = "EntityId";
        private const string PathToEngineIdFromClientInfo = "EngineId";

        [Require] public DevClientInfoReader ClientInfo;

        private GameObject LocalPlayer;
        private Canvas UI;

        private Dictionary<ClientType, GameObject> ClientTypePanes;

        public void OnEnable()
        {
            ClientTypePanes = new Dictionary<ClientType, GameObject>();

            ClientInfo.ClientsUpdated += ClientsUpdated;

            GetComponent<UICreatorBehaviour>().AddCanvasListener(this);
        }

        private void ClientsUpdated(IReadOnlyDictionary<ClientType, ClientTypeInfo> clients)
        {
            if (UI != null)
            {
                ReloadUI(clients);
            }
        }

        public void OnDisable()
        {
            DeleteOldUI();
        }

        public void InitializeUI(Canvas rootCanvas, GameObject localPlayer)
        {
            LocalPlayer = localPlayer;
            UI = rootCanvas;

            if (ClientInfo != null)
            {
                ReloadUI(ClientInfo.Clients);
            }
        }

        private void DeleteOldUI()
        {
            var infoPaneEnumerator = ClientTypePanes.Values.GetEnumerator();
            while (infoPaneEnumerator.MoveNext())
            {
                var scrollRect = infoPaneEnumerator.Current;
                Destroy(scrollRect);
            }

            ClientTypePanes.Clear();
        }

        private void ReloadUI(IReadOnlyDictionary<ClientType, ClientTypeInfo> devClientInfo)
        {
            DeleteOldUI();

            var clientTypeParent = UI.transform.Find(PathToClientTypeFromRoot);

            var infoEnumerator = devClientInfo.GetEnumerator();
            while (infoEnumerator.MoveNext())
            {
                var info = infoEnumerator.Current;

                var clientType = info.Key;

                var clients = info.Value.Clients;
                var numClients = clients.Count;

                var clientTypePane = CreateClientTypePane(clientType, numClients, clientTypeParent);
                
                var clientParent = clientTypePane.transform.Find(PathToClientInfoFromClientType);

                var clientEnumerator = clients.GetEnumerator();
                while(clientEnumerator.MoveNext())
                {
                    var client = clientEnumerator.Current;
                    CreateClientInfoPane(client.Key, client.Value, clientParent);
                    
                }
            }
        }

        private GameObject CreateClientTypePane(ClientType clientType, int numClients, Transform parent)
        {
            var pane = (GameObject)Instantiate(Resources.Load(ClientTypePanePath));
            pane.transform.Find(PathToCountFromClientType).GetComponent<Text>().text = numClients.ToString(CultureInfo.InvariantCulture);
            pane.transform.SetParent(parent, true);
            pane.transform.localScale = Vector3.one;
            pane.transform.Find(PathToTitleFromClientType).GetComponent<Text>().text = clientType.ToString();
            ClientTypePanes.Add(clientType, pane);
            return pane;
        }

        private void CreateClientInfoPane(EntityId entityId, ClientInfo info, Transform parent)
        {
            var pane = (GameObject) Instantiate(Resources.Load(ClientInfoPanePath));
            pane.transform.SetParent(parent, true);
            pane.transform.localScale = Vector3.one;
            pane.transform.Find(PathToEntityIdFromClientInfo).GetComponent<Text>().text = entityId.ToString();
            pane.transform.Find(PathToEngineIdFromClientInfo).GetComponent<Text>().text = info.EngineId;
        }
    }
}
