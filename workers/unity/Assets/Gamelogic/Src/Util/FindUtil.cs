using UnityEngine;

namespace Assets.Gamelogic.Src.Util
{
    class FindUtil
    {
        public static GameObject FindLocalPlayer()
        {
            return HierarchyUtil.GetTopLevelEntity(Camera.main.gameObject);
        }
    }
}
