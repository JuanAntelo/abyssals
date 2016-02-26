using System;

namespace Assets.Gamelogic.Src.Util
{
    class VectorUtil
    {
        public static bool FloatEquals(float a, float b, float epsilon = 0.05f)
        {
            return Math.Abs(a - b) <= epsilon;
        }
    }
}
