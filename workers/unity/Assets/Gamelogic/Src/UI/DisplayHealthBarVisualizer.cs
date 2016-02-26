using System;
using Assets.Gamelogic.Src.UI;
using Assets.Gamelogic.Src.Util;
using Improbable.Abyssal.Attributes;
using Improbable.Unity;
using Improbable.Unity.Visualizer;
using UnityEngine;
using UnityEngine.UI;

namespace Assets.Gamelogic.Src.Combat
{
    [EngineType(EnginePlatform.Client)]
    internal class DisplayHealthBarVisualizer : MonoBehaviour
    {
        [Require] public HealthReader Health;
        [Require] public DeathReader Death;

        public GameObject HealthBarParent;
        private GameObject HealthBar;

        private const float MaxDistanceFromPlayer = 10f;
        private const float FarFadeDistanceFromPlayer = 8f;
        private const float CloseFadeDistanceFromCamera = 6f;
        private const float MinFadeDistanceFromCamera = 3.5f;

        private GameObject LocalPlayer;
        private bool WithinRange;

        public void OnEnable()
        {
            LocalPlayer = FindUtil.FindLocalPlayer();
            Health.CurrentUpdated += HealthUpdated;
            Health.MaxUpdated += MaxHealthUpdated;
            Death.IsDeadUpdated += IsDeadUpdated;
        }

        public void Update()
        {
            if (Death.IsDead)
            {
                return;
            }

            if (Camera.main != null)
            {
                var distanceFromPlayer = (LocalPlayer.transform.position - transform.position).magnitude;
                if (distanceFromPlayer > MaxDistanceFromPlayer)
                {
                    if (HealthBar != null)
                    {
                        WithinRange = false;
                        Destroy(HealthBar);
                        HealthBar = null;
                    }
                }
                else if (HealthBar == null)
                {
                    WithinRange = true;
                    UpdateHealthBar(Health.Current, Health.Max);
                }

                if (HealthBar != null)
                {
                    var distanceFromCamera = (Camera.main.transform.position - transform.position).magnitude;
                    var closeFadeFraction = (distanceFromCamera - MinFadeDistanceFromCamera) / (CloseFadeDistanceFromCamera - MinFadeDistanceFromCamera);
                    var farFadeFraction = 1f - (distanceFromPlayer - FarFadeDistanceFromPlayer) / (MaxDistanceFromPlayer - FarFadeDistanceFromPlayer);
                    var fraction = Math.Min(closeFadeFraction, farFadeFraction);
                    var alpha = Math.Min(fraction, 1f);
                    var images = HealthBar.GetComponentsInChildren<Image>();
                    for (var i = 0; i < images.Length; i++)
                    {
                        var image = images[i];
                        image.color = new Color(image.color.r, image.color.g, image.color.b, alpha);
                    }
                }
            }
        }

        private void HealthUpdated(int currentHealth)
        {
            if (!WithinRange)
            {
                return;
            }

            if (Death.IsDead)
            {
                return;
            }

            UpdateHealthBar(currentHealth, Health.Max);
        }

        private void MaxHealthUpdated(int maxHealth)
        {
            if (!WithinRange)
            {
                return;
            }

            if (Death.IsDead)
            {
                return;
            }

            UpdateHealthBar(Health.Current, maxHealth);
        }

        private void UpdateHealthBar(int currentHealth, int maxHealth)
        {
            if (currentHealth < maxHealth && HealthBar == null)
            {
                var healthBar = Resources.Load<GameObject>("UI/HealthBar");
                HealthBar = (GameObject) Instantiate(healthBar, HealthBarParent.transform.position, HealthBarParent.transform.rotation);
                HealthBar.transform.SetParent(HealthBarParent.transform, false);
                HealthBar.transform.localPosition = Vector3.zero;
            }
            else if (currentHealth == maxHealth && HealthBar != null)
            {
                Destroy(HealthBar);
                HealthBar = null;
            }

            if (HealthBar != null)
            {
                HealthBar.GetComponent<HealthBarBehaviour>().SetHealthPercentage(currentHealth / (float) maxHealth);
            }
        }

        private void IsDeadUpdated(bool isDead)
        {
            if (isDead && HealthBar != null)
            {
                Destroy(HealthBar);
                HealthBar = null;
            }
        }
    }
}