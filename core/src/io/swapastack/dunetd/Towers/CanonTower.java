package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class CanonTower extends Tower{
    public CanonTower() {
        graphics = "weapon_cannon.glb";
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z);
    }



}
