package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class Bullet9mm {
    public String graphics;
    protected Scene scene;
    protected AnimationController animController;


    public Bullet9mm() {
        graphics = "bullet_9_mm/scene.gltf";
    }


    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z).scale(0.06f, 0.06f, 0.06f);

    }

    public Scene createScene(HashMap<String, SceneAsset> m) {
        scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    public Matrix4 setToTranslation(Vector3 vector) {
        return scene.modelInstance.transform.setToTranslation(vector);
    }

    public Matrix4 setToTranslation(float x, float y, float z) {
        return scene.modelInstance.transform.setToTranslation(x, y, z);
    }

  //  public void setAnimation(String id, int loopCnt) {
 //       getAnimationController().setAnimation(id, loopCnt);
  //  }

    public Scene getScene() {
        return scene;
    }

    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z);
    }
}
