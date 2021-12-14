package io.swapastack.dunetd.Towers;

import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Knocker extends Tower{

    public Knocker(GameScreen screen) {
        graphics = "detail_tree.glb";
        type = 0;
        range = 3;
        gameScreen = screen;
        this.cost = 35;
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setTranslation(x, y, z);
        towers[Math.round(x)][Math.round(z)] = this;
    }

    @Override
    public void fire(ArrayList<Enemy> enemiesList) {

    }
}
