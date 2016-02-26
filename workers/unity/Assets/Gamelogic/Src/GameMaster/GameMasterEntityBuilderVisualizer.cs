using System;
using System.Collections;
using Assets.Gamelogic.Src.Dev;
using Assets.Gamelogic.Src.Util;
using Improbable.Abyssal.Gamemaster;
using Improbable.Abyssal.Labels;
using Improbable.Unity;
using Improbable.Unity.Common.Core.Math;
using Improbable.Unity.Entity;
using Improbable.Unity.Visualizer;
using IoC;
using UnityEngine;

namespace Assets.Gamelogic.Src.GameMaster
{
    [EngineType(EnginePlatform.Client)]
    internal class GameMasterEntityBuilderVisualizer : MonoBehaviour
    {
        [Inject]
        public IEntityTemplateProvider EntityTemplateProvider { get; set; }

        [Require] public GameMasterControlsWriter Controls;

        public LayerMask IgnoreRaycastMask;

        private GameObject EntityToBuild;

        private Vector3? LastPosition;
        private bool FirstBuildFrame;

        public void BuildSpider()
        {
            InstantiateCursor();
            FirstBuildFrame = true;
            InteractionMode.SetInteractionMode(InteractionMode.InteractionModes.Building);
        }

        public void BuildEntities(int amt)
        {
            StartCoroutine("BuildEntitiesCoroutine", amt);
        }

        IEnumerator BuildEntitiesCoroutine(int amt)
        {
            const float distanceBetweenSpawnPointsOnRing = 5f;
            const float distanceBetweenRings = 5f;
            var numEntitiesProduced = 0;
            var center = Vector3.zero;

            var currentRing = 1;
            var currentDegree = 0f;
            while (numEntitiesProduced < amt)
            {
                var entitiesOnRing = 2f * Mathf.PI * currentRing * distanceBetweenRings / distanceBetweenSpawnPointsOnRing;
                var degreesBetweenSpawnPointsOnCurrentRing = 360f / entitiesOnRing;

                var spoke = Quaternion.Euler(new Vector3(0, currentDegree, 0)) * Vector3.right;
                var position = center + spoke * currentRing * distanceBetweenRings;
                var rotation = Quaternion.LookRotation(center - position);

                Controls.Update.TriggerBuildEntity(NatureType.Nspider, position.ToCoordinates(), rotation.eulerAngles.ToNativeVector()).FinishAndSend();

                currentDegree += degreesBetweenSpawnPointsOnCurrentRing;
                if (currentDegree >= 360f)
                {
                    currentDegree = 0;
                    currentRing++;
                }

                numEntitiesProduced++;
                yield return new WaitForSeconds(.01f);
            }
        }
            
        public void Update()
        {
            if (EntityToBuild != null)
            {
                var bounds = GetEntityBounds();
                var placementLocation = GetCursorLocation();
                var yOffset = bounds.center.y - EntityToBuild.transform.position.y;
                EntityToBuild.transform.position = placementLocation + new Vector3(0, yOffset, 0);

                if (Math.Abs(Input.mouseScrollDelta.y) > 0.05f)
                {
                    var amountToRotate = 720f * Input.mouseScrollDelta.y * Time.deltaTime;
                    var rotationQuat = Quaternion.Euler(new Vector3(0, amountToRotate, 0));
                    EntityToBuild.transform.rotation *= rotationQuat;
                }

                if (!FirstBuildFrame && Input.GetMouseButtonUp(0))
                {
                    BuildEntity();
                }

                FirstBuildFrame = false;
            }
        }

        private void BuildEntity()
        {
            var eulerRotation = EntityToBuild.transform.rotation.eulerAngles.ToNativeVector();
            var coordinatePosition = EntityToBuild.transform.position.ToCoordinates();
            Controls.Update.TriggerBuildEntity(NatureType.Nspider, coordinatePosition, eulerRotation)
                .FinishAndSend();
            Destroy(EntityToBuild);
            EntityToBuild = null;
            InteractionMode.SetInteractionMode(InteractionMode.InteractionModes.Inspecting);
        }

        private Bounds GetEntityBounds()
        {
            return EntityToBuild.GetComponentInChildren<Collider>().bounds;
        }

        private Vector3 GetCursorLocation()
        {
            RaycastHit hit;
            var ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            if (Physics.Raycast(ray, out hit, Mathf.Infinity, ~IgnoreRaycastMask))
            {
                LastPosition = hit.point;
                return hit.point;
            }

            if (LastPosition == null)
            {
                LastPosition = transform.position;
            }

            return LastPosition.Value;
        }

        private void InstantiateCursor()
        {
            var entityAssetId = new EntityAssetId("Spider", EntityAssetId.DEFAULT_CONTEXT);
            try
            {
                EntityToBuild = EntityTemplateProvider.GetEntityTemplate(entityAssetId);
            }
            catch (MissingComponentException)
            {
                EntityToBuild = null;
                EntityTemplateProvider.PrepareTemplate(entityAssetId, NoOpAction, Debug.LogError);
                return;
            }

            EntityToBuild = Instantiate(EntityToBuild, GetCursorLocation(), Quaternion.identity) as GameObject;

            if (EntityToBuild != null)
            {
                var spawnedColliders = EntityToBuild.GetComponentsInChildren<Collider>();
                for (var i = 0; i < spawnedColliders.Length; i++)
                {
                    spawnedColliders[i].gameObject.layer = Layers.IgnoreRaycast;
                }
            }
        }

        private static Action<EntityAssetId> NoOpAction
        {
            get { return _ => { }; }
        }
    }
}