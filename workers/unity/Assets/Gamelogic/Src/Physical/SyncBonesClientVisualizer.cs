using System;
using System.Collections.Generic;
using Improbable.Abyssal.Physical;
using Improbable.Corelib.Util;
using Improbable.Unity;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.Physical
{
    [EngineType(EnginePlatform.Client)]
    class SyncBonesClientVisualizer : MonoBehaviour
    {
        [Require] public SyncedBonesWriter SyncedBones;

        public List<GameObject> BonesToSync;

        private Dictionary<String, GameObject> BonePathToGameObjects; 

        public void OnEnable()
        {
            BonePathToGameObjects = new Dictionary<string, GameObject>();
            var bonePaths = new List<String>();
            for (var i = 0; i < BonesToSync.Count; i++)
            {
                var bone = BonesToSync[i];
                var path = BuildChildPath(bone);
                bonePaths.Add(path);
                BonePathToGameObjects.Add(path, bone);
            }
            SyncedBones.Update.BonesToSync(bonePaths).FinishAndSend();
        }

        public void FixedUpdate()
        {
            var newSyncedBones = new Dictionary<String, SyncedBone>();
            foreach (var pathBone in BonePathToGameObjects)
            {
                var bone = pathBone.Value;
                var bonePath = pathBone.Key;
                var position = bone.transform.localPosition.ToNativeVector3f();
                var rotation = bone.transform.localRotation.ToNativeQuaternion();
                var synchedBone = new SyncedBone(position, rotation);
                newSyncedBones.Add(bonePath, synchedBone);
            }
            SyncedBones.Update.SyncedInfo(newSyncedBones).FinishAndSend();
        }

        private static string BuildChildPath(GameObject child)
        {
            var pathName = child.name;
            var current = child.transform.parent;
            while (current.parent != null && !current.parent.name.Contains("[Pool]"))
            {
                pathName = current.name + "/" + pathName;
                current = current.parent;
            }
            return pathName;
        }
    }
}
