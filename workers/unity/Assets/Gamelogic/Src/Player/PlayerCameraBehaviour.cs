using System;
using Assets.Gamelogic.Src.Util;
using UnityEngine;

// Based off MouseOrbitImproved by Veli V
// http://wiki.unity3d.com/index.php?title=MouseOrbitImproved
public class PlayerCameraBehaviour : MonoBehaviour
{
    public Transform Target;
    private float XSpeed = 12f;
    private float YSpeed = 12f;

    private float YMinLimit = -0f;
    private float YMaxLimit = 80f;

    private float DistanceMin = .5f;
    private float DistanceMax = 15f;

    private float Distance;
    private GameObject Player;
    private Quaternion Rotation;

    private Vector3 LastMousePosition;

    public void Start()
    {
        Distance = (transform.position - Target.position).magnitude;
        Rotation = transform.rotation;
        Player = HierarchyUtil.GetTopLevelEntity(gameObject);
    }

    public void Update()
    {
        if (Input.GetKeyDown(KeyCode.Q))
        {
            Rotation = Target.rotation;
        }

        if (Input.GetMouseButtonDown(1))
        {
            LastMousePosition = Input.mousePosition;
        }
        else if (Input.GetMouseButton(1))
        {
            var mouseDelta = (Input.mousePosition - LastMousePosition);

            var eulerRotation = Rotation.eulerAngles;
            eulerRotation.y += mouseDelta.x * XSpeed * Distance * 0.02f;
            eulerRotation.x -= mouseDelta.y * YSpeed * 0.02f;

            eulerRotation.x = ClampAngle(eulerRotation.x, YMinLimit, YMaxLimit);

            Rotation = Quaternion.Euler(eulerRotation);

            LastMousePosition = Input.mousePosition;
        }

        var scrollWheelDelta = Input.GetAxis("Mouse ScrollWheel");
        if (Math.Abs(scrollWheelDelta) > 0.005f)
        {
            Distance = Mathf.Clamp(Distance - Input.GetAxis("Mouse ScrollWheel") * 5, DistanceMin, DistanceMax);
        }

        SetCameraPosition();
    }

    private void SetCameraPosition()
    {
        var negDistance = new Vector3(0.0f, 0.0f, -Distance);
        var position = Rotation * negDistance + Target.position;

        transform.rotation = Rotation;
        transform.position = position;
    }

    public static float ClampAngle(float angle, float min, float max)
    {
        if (angle < -360F)
        {
            angle += 360F;
        }
        if (angle > 360F)
        {
            angle -= 360F;
        }
        return Mathf.Clamp(angle, min, max);
    }
}