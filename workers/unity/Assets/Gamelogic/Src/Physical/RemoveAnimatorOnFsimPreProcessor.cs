using System.Linq;
using Improbable.Unity;
using Improbable.Unity.Export;
using UnityEngine;

namespace Assets.Gamelogic.Src.Physical
{
    class RemoveAnimatorOnFsimPreProcessor : MonoBehaviour, IPrefabExportProcessor
    {
        public void ExportProcess(EnginePlatform enginePlatform)
        {
            if (enginePlatform == EnginePlatform.FSim)
            {
                gameObject.GetComponent<Animator>().enabled = false;
                var animators = gameObject.GetComponentsInChildren<Animator>();
                for (var i = 0; i < animators.Count(); i++)
                {
                    animators[i].enabled = false;
                }
            }
        }
    }
}
