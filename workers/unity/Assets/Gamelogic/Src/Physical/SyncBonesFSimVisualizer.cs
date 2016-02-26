using System;
using System.Collections.Generic;
using Improbable.Abyssal.Physical;
using Improbable.Corelib.Util;
using Improbable.Unity;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Visualizer;
using UnityEngine;
using Improbable.Util.Collections;

namespace Assets.Gamelogic.Src.Physical
{
    [EngineType(EnginePlatform.FSim)]
    class SyncBonesFSimVisualizer : MonoBehaviour
    {
        [Require] public SyncedBonesReader SyncedBones;

        private Dictionary<String, GameObject> Bones;

        public void OnEnable()
        {
            Bones = new Dictionary<string, GameObject>();
            SyncedBones.BonesToSyncUpdated += BonesToSyncUpdated;
        }

        private void BonesToSyncUpdated(IReadOnlyList<string> bones)
        {
            for (var i = 0; i < bones.Count; i++)
            {
                var bonePath = bones[i];
                var child = transform.Find(bonePath);
                if (child != null)
                {
                    Bones.Add(bonePath, child.gameObject);
                }
            }
        }

        public void FixedUpdate()
        {
            foreach (var boneInfo in SyncedBones.SyncedInfo)
            {
                var bonePath = boneInfo.Key;
                if (Bones.ContainsKey(bonePath))
                {
                    var syncInfo = boneInfo.Value;
                    var bone = Bones[bonePath];
                    bone.transform.localPosition = syncInfo.LocalPosition.ToUnityVector();
                    bone.transform.localRotation = syncInfo.LocalRotation.ToUnityQuaternion();
                }
            }
        }
    }
}
