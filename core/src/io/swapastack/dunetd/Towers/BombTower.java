package io.swapastack.dunetd.Towers;

import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class BombTower extends Tower{
    public BombTower() {
        graphics = "weapon_blaster.glb";
        type = 0;
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z);
        if(isEligibleToPlace(towers, Math.round(x), Math.round(z))) {
            towers[Math.round(x)][Math.round(z)] = this;
        }
    }
}
