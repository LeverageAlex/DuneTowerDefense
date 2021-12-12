package io.swapastack.dunetd.Towers;

import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public class SonicTower extends Tower{
    public SonicTower(GameScreen screen) {
        graphics = "towerRound_crystals.glb";
        type = 1;
        range = 1.7;
        gameScreen = screen;
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z);
        towers[Math.round(x)][Math.round(z)] = this;
    }

    //Take care of HarvestMachine gaining less Damage than other Enemys
    @Override
    public void fire(ArrayList<Enemy> enemiesList) {
        for (Enemy enemy : enemiesList) {
            if (isInRange(enemy.getCoords())) {
                //to fire
             //   System.out.println("SonicTower shooting at: " + enemy.name );
            }
        }
    }
}
