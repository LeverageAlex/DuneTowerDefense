package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.ConfigMgr;
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
        range = ConfigMgr.sonicTowRange;
        gameScreen = screen;
        this.cost = ConfigMgr.sonicTowCost;
        towerDmg = ConfigMgr.sonicTowDmg;
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, ArrayList<Tower> towerList, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setTranslation(x, y, z);
        towers[Math.round(x)][Math.round(z)] = this;
        towerList.add(this);
        this.towerList = towerList;
    }

    //Take care of HarvestMachine gaining less Damage than other Enemys
    @Override
    public Enemy fire(ArrayList<Enemy> enemiesList) {
        for (Enemy enemy : enemiesList) {
            if (isInRange(enemy.getCoords())) {
                enemy.gainDamage(towerDmg);
                //to fire
             //   System.out.println("SonicTower shooting at: " + enemy.name );
            }
        }
        return null;
    }
}
