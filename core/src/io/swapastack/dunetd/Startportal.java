package io.swapastack.dunetd;

import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class Startportal {
    public String graphics = "towerRound_base.glb";
    private Scene scene;
    private float x, y, z;


    public Startportal(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.scene.modelInstance.transform.setToTranslation(x, y, z).scale(1.f, 0.1f, 1.f);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPos(Vector3 pos) {
        this.scene.modelInstance.transform.setTranslation(pos);
    }

    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
