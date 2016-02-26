using Assets.Gamelogic.Src.GameMaster;
using Improbable.Abyssal.Gamemaster;
using Improbable.Unity.Visualizer;
using UnityEngine;

namespace Assets.Gamelogic.Src.UI
{
    class GameMasterUIControls : MonoBehaviour
    {
        [Require] public GameMasterControlsWriter Controls;

        private GameObject GameMaster;

        public void Awake()
        {
            GameMaster = GameObject.FindGameObjectWithTag("GameMaster");
        }

        public void BuildSpider()
        {
            GameMaster.GetComponent<GameMasterEntityBuilderVisualizer>().BuildSpider();            
        }

        public void BuildATonOfSpiders()
        {
            GameMaster.GetComponent<GameMasterEntityBuilderVisualizer>().BuildEntities(200);
        }
    }
}
