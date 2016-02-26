using Improbable.Unity;
using Improbable.Unity.Export;
using UnityEngine;

namespace Assets.Gamelogic.Src.Player
{
    class PlayerPreProcessor : MonoBehaviour, IPrefabExportProcessor
    {
        public void ExportProcess(EnginePlatform enginePlatform)
        {
            AddVisualizers(gameObject, enginePlatform);
        }

        private void AddVisualizers(GameObject targetGameObject, EnginePlatform enginePlatform)
        {
            if (enginePlatform == EnginePlatform.Client)
            {
                AddClientVisualizers(targetGameObject);                
            }
        }

        private void AddClientVisualizers(GameObject target)
        {
            target.AddComponent<PlayerControllerVisualizer>();
        }

        private static T AddDisabledComponent<T>(GameObject target) where T : MonoBehaviour
        {
            target.AddComponent<T>();
            var component = target.GetComponent<T>();
            component.enabled = false;
            return component;
        }
    }
}
