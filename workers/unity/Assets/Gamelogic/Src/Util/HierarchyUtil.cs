using UnityEngine;

namespace Assets.Gamelogic.Src.Util
{
    class HierarchyUtil
    {
        public static GameObject GetTopLevelEntity(GameObject gameObject)
        {
            var current = gameObject.transform;
            while (current.parent != null && !current.parent.name.Contains("[Pool]"))
            {
                current = current.parent;
            }
            return current.gameObject;
        }
    }
}
