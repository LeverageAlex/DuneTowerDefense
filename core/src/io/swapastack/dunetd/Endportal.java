package io.swapastack.dunetd;

import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class Endportal {
    private int x, y;
    public String graphics = "towerSquare_topB.glb";
    private Scene scene;


    public Endportal(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.scene.modelInstance.transform.setToTranslation(x, y, z).scale(1.f, 0.1f, 1.f);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }
}
