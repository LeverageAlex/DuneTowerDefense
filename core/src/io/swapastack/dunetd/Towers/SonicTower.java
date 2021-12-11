package io.swapastack.dunetd.Towers;

import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class SonicTower extends Tower{
    public SonicTower() {
        graphics = "towerRound_crystals.glb";
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z);
    }
}
