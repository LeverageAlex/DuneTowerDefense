package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class HarvestMachine extends Enemy{
    public HarvestMachine() {
        graphics = "spaceship_orion/scene.gltf";
    }

    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z)
                .scale(0.2f, 0.2f, 0.2f);

    }

    @Override
    public void destroyDamage() {

    }

    @Override
    public void onKill() {

    }

}
