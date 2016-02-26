using Assets.Gamelogic.Src.Physical;
using Improbable.Core.GameLogic.Visualizers;
using Improbable.Corelib.Physical;
using Improbable.CoreLib.Physical.Visualizers;
using Improbable.Unity;
using Improbable.Unity.Export;
using UnityEngine;

public class ClientSideAuthorityPreprocessor : MonoBehaviour, IPrefabExportProcessor
{
    public void ExportProcess(EnginePlatform enginePlatform)
    {
        AddVisualizers(gameObject, enginePlatform);
    }

    public static void AddVisualizers(GameObject targetGameObject, EnginePlatform enginePlatform)
    {
        targetGameObject.AddComponent<InitialPositionVisualizer>();
        targetGameObject.AddComponent<InitialRotationVisualizer>();
        targetGameObject.AddComponent<TeleportVisualizer>();
        targetGameObject.AddComponent<PlayerInterpolatingPositionVisualizer>();
        targetGameObject.AddComponent<PlayerInterpolatingRotationVisualizer>();
        targetGameObject.AddComponent<PhysicalPosition>();
        targetGameObject.AddComponent<PhysicalRotation>();
    }
}