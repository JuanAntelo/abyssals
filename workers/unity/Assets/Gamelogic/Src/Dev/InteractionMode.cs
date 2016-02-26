
namespace Assets.Gamelogic.Src.Dev
{
    public class InteractionMode
    {
        public enum InteractionModes
        {
            Inspecting,
            Building
        }

        private static InteractionModes Mode = InteractionModes.Inspecting;

        public static void SetInteractionMode(InteractionModes newMode)
        {
            Mode = newMode;
        }

        public static InteractionModes GetInteractionMode()
        {
            return Mode;
        }
    }
}
